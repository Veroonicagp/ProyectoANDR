package com.example.readytoenjoy.ui.activity.info

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.readytoenjoy.databinding.FragmentActivityInfoBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@AndroidEntryPoint
class ActivityInfoFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentActivityInfoBinding
    private val vm: ActivityInfoViewModel by activityViewModels()
    private val args: ActivityInfoFragmentArgs by navArgs()

    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null
    private var activityLocation: String? = null
    private var activityTitle: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityInfoBinding.inflate(
            inflater,
            container,
            false
        )

        // Inicializar MapView
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        vm.loadActivity(args.activityId)
        lifecycleScope.launch {
            vm.uiState.collect { uiState ->
                when (uiState) {
                    is InfoActivityUiState.Loading -> {
                        // Mostrar loading si es necesario
                    }

                    is InfoActivityUiState.Success -> {
                        val activity = uiState.activity
                        binding.apply {
                            topAppBar.title = activity.title
                            location.text = activity.location
                            crdImg.load(activity.img)
                            price.text = activity.price
                            description.text = activity.description
                        }

                        // Guardar datos para el mapa
                        activityLocation = activity.location
                        activityTitle = activity.title

                        // Si el mapa ya está listo, mostrar la ubicación
                        googleMap?.let { map ->
                            showLocationOnMap(map, activity.location, activity.title)
                        }
                    }

                    is InfoActivityUiState.Error -> {
                        // Manejar error si es necesario
                    }
                }
            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Configurar el mapa
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = false

        // Si ya tenemos la ubicación, mostrarla
        activityLocation?.let { location ->
            showLocationOnMap(map, location, activityTitle ?: "Actividad")
        }
    }

    private fun showLocationOnMap(map: GoogleMap, locationName: String, title: String) {
        lifecycleScope.launch {
            try {
                val latLng = getLocationFromAddress(locationName)
                latLng?.let { coordinates ->
                    withContext(Dispatchers.Main) {
                        // Agregar marcador
                        map.addMarker(
                            MarkerOptions()
                                .position(coordinates)
                                .title(title)
                                .snippet(locationName)
                        )

                        // Mover cámara a la ubicación
                        map.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(coordinates, 15f)
                        )
                    }
                }
            } catch (e: Exception) {
                // Si no se puede geocodificar, mostrar ubicación por defecto (ej: Granada)
                withContext(Dispatchers.Main) {
                    val defaultLocation = LatLng(37.1773, -3.5986) // Granada, España
                    map.addMarker(
                        MarkerOptions()
                            .position(defaultLocation)
                            .title(title)
                            .snippet("Ubicación aproximada")
                    )
                    map.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f)
                    )
                }
            }
        }
    }

    private suspend fun getLocationFromAddress(locationName: String): LatLng? {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocationName(locationName, 1)

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    LatLng(address.latitude, address.longitude)
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    // Métodos del ciclo de vida del MapView
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}