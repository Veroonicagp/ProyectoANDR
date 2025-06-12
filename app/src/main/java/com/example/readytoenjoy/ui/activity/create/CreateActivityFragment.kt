package com.example.readytoenjoy.ui.activity.create

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.readytoenjoy.worker.NotificationWorker
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.readytoenjoy.core.utils.ConnectivityHelper
import com.example.readytoenjoy.databinding.FragmentCreateActivityBinding
import com.example.readytoenjoy.ui.utils.OfflineUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreateActivityFragment : Fragment() {

    private var _img: Uri? = null
    private lateinit var binding: FragmentCreateActivityBinding
    private val vm: CreateActivityViewModel by activityViewModels()

    @Inject
    lateinit var connectivityHelper: ConnectivityHelper

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            loadPhoto(uri)
        }
    }

    private fun loadPhoto(uri: Uri?) {
        binding.imagenAct.load(uri)
        _img = uri
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateActivityBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }


        checkConnectivity()
        vm.resetState()
        setupClickListeners()
        observeUiState()
    }

    private fun checkConnectivity() {
        if (!connectivityHelper.isNetworkAvailable()) {
            OfflineUtils.showNeedConnectionMessage(binding.root, "crear actividades")
        }
    }

    private fun setupClickListeners() {
        binding.crearBttn.setOnClickListener {
            if (connectivityHelper.isNetworkAvailable()) {
                createActivity()
            } else {
                OfflineUtils.showNeedConnectionMessage(binding.root, "crear actividades")
            }
        }

        binding.photoBttn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun createActivity() {
        val title = binding.title.text.toString()
        val location = binding.location.text.toString()
        val img = _img
        val price = binding.price.text.toString()
        val description = binding.description.text.toString()

        if (validateInputs(title, location, price, description)) {
            viewLifecycleOwner.lifecycleScope.launch {
                vm.create(title, img, location, price, description, "")
            }
        }
    }

    private fun validateInputs(title: String, location: String, price: String, description: String): Boolean {
        if (title.isBlank()) {
            binding.title.error = "El título es requerido"
            return false
        }

        if (location.isBlank()) {
            binding.location.error = "La localización es requerida"
            return false
        }

        if (price.isBlank()) {
            binding.price.error = "El precio es requerido"
            return false
        }

        if (description.isBlank()) {
            binding.description.error = "La descripción es requerida"
            return false
        }

        return true
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect { uiState ->
                    when(uiState) {
                        is UiState.Created -> {
                            lanzarNotificacion("Actividad creada", "Has creado una nueva actividad exitosamente.")
                            findNavController().popBackStack()
                        }
                        is UiState.Error -> {
                            Toast.makeText(context, "Error: ${uiState.message}", Toast.LENGTH_SHORT).show()
                        }
                        is UiState.Loading -> {
                            binding.crearBttn.isEnabled = false
                        }
                        is UiState.Ready -> {
                            binding.crearBttn.isEnabled = true
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun lanzarNotificacion(titulo: String, mensaje: String) {
        val data = Data.Builder()
            .putString("title", titulo)
            .putString("message", mensaje)
            .build()

        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(request)
    }

}