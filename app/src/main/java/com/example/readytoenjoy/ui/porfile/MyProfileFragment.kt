package com.example.readytoenjoy.ui.porfile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.readytoenjoy.R
import com.example.readytoenjoy.core.model.Adven
import com.example.readytoenjoy.databinding.FragmentActivityListBinding
import com.example.readytoenjoy.databinding.FragmentMyProfileBinding
import com.example.readytoenjoy.ui.activity.ActivityListAdapter
import com.example.readytoenjoy.ui.activity.ActivityListUiState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyProfileFragment : Fragment() {
    private var _img: Uri? = null
    private lateinit var binding: FragmentMyProfileBinding
    private val viewModel: MyProfileViewModel by viewModels()
    private val cameraPermissionContract = ActivityResultContracts.RequestPermission()
    private val cameraPermissionLauncher = registerForActivityResult(cameraPermissionContract) {
            isGranted ->
        if (isGranted)
            navigateToCamera()
        else
            Toast.makeText(requireContext(),
                "No hay permisos para la camara",
                Toast.LENGTH_LONG,
            ).show()
    }
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Si la uril no es nula, es que el usuario ha selccionado algín archivo
        if (uri != null) {
            // Lo carcagmos en el ImageView
            //binding.incidentImage.load(uri)
            loadPhoto(uri)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val media = _img

            if (validateInputs(name, email)) {
                viewModel.updateProfile(name, media,email)
            }
        }

        binding.shareButton.setOnClickListener {
            shareProfile()
        }

        binding.camera.setOnClickListener {

            // SI TENEMOS PERMISOS NAVEGAMOS A LA CAMARA
            if (hasCameraPermissions(requireContext())) {
                navigateToCamera()
            }
            else {
                // SI NO TENEMOS PERMISOS, LOS PEDIMOS
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        binding.galeria.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is ProfileUiState.Loading -> {
                        binding.saveButton.isEnabled = false
                    }
                    is ProfileUiState.Success -> {
                        binding.saveButton.isEnabled = true
                        updateUI(uiState.adven)
                        Snackbar.make(
                            binding.root,
                            "Perfil actualizado correctamente",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    is ProfileUiState.Error -> {
                        binding.saveButton.isEnabled = true
                        Snackbar.make(
                            binding.root,
                            "El perfil no se actualizado correctamente",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is ProfileUiState.Wait ->{
                        binding.saveButton.isEnabled = true
                        updateUI(uiState.adven)
                    }

                }
            }
        }
    }

    private fun shareProfile() {
        val userName = binding.nameEditText.text.toString()

        val shareMessage = "Hola, soy $userName y estoy utilizando la aplicación ReadyToEnjoy"

        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        sendIntent.type = "text/plain"

        startActivity(Intent.createChooser(sendIntent, "Compartir con..."))
    }

    private fun validateInputs(name: String, email: String): Boolean {
        var isValid = true

        if (name.isBlank()) {
            binding.nameEditText.error = "El nombre es requerido"
            isValid = false
        }

        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = "Email inválido"
            isValid = false
        }

        return isValid
    }

    private fun updateUI(adven: Adven) {
            binding.nameEditText.setText(adven.name)
            binding.emailEditText.setText(adven.email)
            binding.profileImage.load(adven.media)
    }

    private fun loadPhoto(uri: Uri?) {
        binding.profileImage.load(uri)
        _img = uri
    }

    private fun hasCameraPermissions(context: Context) =
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED


    private fun navigateToCamera() {
        val action = MyProfileFragmentDirections.actionMyProfileFragmentToCameraFragment()
        findNavController().navigate(action)
    }
}