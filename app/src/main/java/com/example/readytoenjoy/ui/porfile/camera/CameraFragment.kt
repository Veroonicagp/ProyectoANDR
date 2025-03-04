package com.example.readytoenjoy.ui.porfile.camera

import android.content.ContentValues
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.readytoenjoy.databinding.FragmentCameraBinding
import com.example.readytoenjoy.ui.porfile.MyProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors

@AndroidEntryPoint
class CameraFragment : Fragment() {
    private lateinit var binding: FragmentCameraBinding
    private val viewModel: MyProfileViewModel by activityViewModels()

    private lateinit var cameraController: LifecycleCameraController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración de la vista previa de cámara
        val previewView = binding.profilePreview
        cameraController = LifecycleCameraController(requireContext())
        cameraController.bindToLifecycle(this)
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        cameraController.setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        previewView.controller = cameraController

        binding.captureProfileBtn.setOnClickListener {
            captureImageToDisk()
        }
    }

    private fun captureImageToDisk() {
        // Formato para nombre de archivo
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault())
            .format(System.currentTimeMillis())

        // Configuración para guardar en MediaStore
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            // Para Android 10+ guardar en Pictures/ReadyToEnjoy
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ReadyToEnjoy")
        }

        // Opciones de salida para la foto
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            requireContext().contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        // Crear ejecutor para procesar la captura
        val cameraExecutor = Executors.newSingleThreadExecutor()

        try {
            Log.d("CameraFragment", "Iniciando captura de imagen...")

            cameraController.takePicture(
                outputOptions,
                cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri = outputFileResults.savedUri

                        if (savedUri != null) {
                            Log.d("CameraFragment", "Imagen capturada con URI: $savedUri")
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewModel.onImageCaptured(savedUri)
                                Log.d("CameraFragment", "URI enviada al ViewModel: $savedUri")
                                findNavController().popBackStack()
                            }
                        } else {
                            Log.e("CameraFragment", "Error: La URI de la imagen es null")
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e("CameraFragment", "Error al capturar imagen: ${exception.message}")
                        activity?.runOnUiThread {
                            Toast.makeText(
                                requireContext(),
                                "Error: ${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            )
        } catch (e: Exception) {
            Log.e("CameraFragment", "Error general: ${e.message}")
            Toast.makeText(
                requireContext(),
                "Error al iniciar la captura: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}