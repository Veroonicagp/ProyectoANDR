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
        binding = FragmentActivityInfoBinding.inflate(inflater, container, false)
        setupMapView(savedInstanceState)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        loadActivityData()
        observeUiState()
    }

    private fun setupMapView(savedInstanceState: Bundle?) {
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    private fun setupClickListeners() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadActivityData() {
        vm.loadActivity(args.activityId)
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            vm.uiState.collect { uiState ->
                when (uiState) {
                    is InfoActivityUiState.Loading -> {
                    }
                    is InfoActivityUiState.Success -> {
                        handleActivityLoaded(uiState.activity)
                    }
                    is InfoActivityUiState.Error -> {
                    }
                }
            }
        }
    }

    private fun handleActivityLoaded(activity: com.example.readytoenjoy.core.model.Activity) {
        updateUI(activity)
        saveActivityDataForMap(activity)
        updateMapIfReady(activity)
    }

    private fun updateUI(activity: com.example.readytoenjoy.core.model.Activity) {
        binding.apply {
            collapsingToolbar.title = activity.title
            location.text = activity.location
            crdImg.load(activity.img)
            price.text = "${activity.price}€"
            description.text = activity.description
        }
    }

    private fun saveActivityDataForMap(activity: com.example.readytoenjoy.core.model.Activity) {
        activityLocation = activity.location
        activityTitle = activity.title
    }

    private fun updateMapIfReady(activity: com.example.readytoenjoy.core.model.Activity) {
        googleMap?.let { map ->
            showLocationOnMap(map, activity.location, activity.title)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        setupMapConfiguration(map)
        showLocationIfAvailable(map)
    }

    private fun setupMapConfiguration(map: GoogleMap) {
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = false
    }

    private fun showLocationIfAvailable(map: GoogleMap) {
        activityLocation?.let { location ->
            showLocationOnMap(map, location, activityTitle ?: "Actividad")
        }
    }

    private fun showLocationOnMap(map: GoogleMap, locationName: String, title: String) {
        lifecycleScope.launch {
            try {
                val latLng = getLocationFromAddress(locationName)
                if (latLng != null) {
                    showLocationMarker(map, latLng, title, locationName)
                } else {
                    showDefaultLocation(map, title)
                }
            } catch (e: Exception) {
                showDefaultLocation(map, title)
            }
        }
    }

    private suspend fun showLocationMarker(map: GoogleMap, coordinates: LatLng, title: String, locationName: String) {
        withContext(Dispatchers.Main) {
            map.addMarker(
                MarkerOptions()
                    .position(coordinates)
                    .title(title)
                    .snippet(locationName)
            )
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15f))
        }
    }

    private suspend fun showDefaultLocation(map: GoogleMap, title: String) {
        withContext(Dispatchers.Main) {
            val defaultLocation = LatLng(37.1773, -3.5986)
            map.addMarker(
                MarkerOptions()
                    .position(defaultLocation)
                    .title(title)
                    .snippet("Ubicación aproximada")
            )
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
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