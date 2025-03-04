package com.example.readytoenjoy.ui.porfile.camera

import android.content.ContentValues
import android.os.Bundle
import android.provider.MediaStore
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
import com.example.readytoenjoy.R
import com.example.readytoenjoy.databinding.FragmentCameraBinding
import com.example.readytoenjoy.ui.porfile.MyProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors

@AndroidEntryPoint
class CameraFragment  : Fragment() {
    private lateinit var binding: FragmentCameraBinding
    private val viewModel: MyProfileViewModel by activityViewModels()

    private lateinit var  cameraController: LifecycleCameraController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(
            inflater,
            container,
            false
        )// Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtenemos del layout el area de preview
        val previewView = binding.incidentPreview
        cameraController = LifecycleCameraController(requireContext())
        // Lo asociamos al ciclo de vida de este fragmento
        cameraController.bindToLifecycle(this)
        // USamos la camara trasera
        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        // [Opcional] Solo dejamos Preview y Captura de fotos
        cameraController.setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        // Asociamos el controlador a la preview
        previewView.controller = cameraController

        binding.captureIncidentBtn.setOnClickListener {
            captureImageToDisk()
        }

    }

    private fun captureImageToDisk() {
        // Crea el formato simple
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault())
            .format(System.currentTimeMillis())
        // Creamos un conjunto de contenidos para fijar en MediaStore el
        // nombre del fichero y que va a ser un JPG
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        // Construimos las opciones de salida para tomar la foto
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            requireContext().contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()
        // Ejecutor nuevo
        val cameraExecutor = Executors.newSingleThreadScheduledExecutor()
        cameraController.takePicture(
            outputOptions,
            cameraExecutor,
            object: ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // Cuando la foto se tome, vamos a ponerla en un viewmodel
                    // con scope en la actividad para poder tomarla desde el
                    // fragmento de editar incidente
                    viewLifecycleOwner.lifecycleScope.launch {
                        // Enviamos al viewModel la uri capturada
                        viewModel.onImageCaptured(outputFileResults.savedUri)
                        // Cerramos este fragmento volviendo al preview
                        findNavController().popBackStack()
                    }

                }
                // En caso de error enviamos en un toast el texto del error ocurrido
                override fun onError(exception: ImageCaptureException) {
                    exception.message?.let {
                        Toast.makeText(requireContext(), exception.message, Toast.LENGTH_LONG).show()
                    }

                }

            }

        )
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

}