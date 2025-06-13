/**
 * @file MyProfileFragment.kt
 * @brief Fragment para la gestión del perfil del usuario
 * @details Permite al usuario editar su perfil, cambiar imagen y cerrar sesión
 * @author ReadyToEnjoy Team
 * @version 1.0
 */

package com.example.readytoenjoy.ui.porfile

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.readytoenjoy.core.model.Adven
import com.example.readytoenjoy.databinding.FragmentMyProfileBinding
import com.example.readytoenjoy.ui.logout.LogoutState
import com.example.readytoenjoy.ui.logout.LogoutViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @class MyProfileFragment
 * @brief Fragment para la gestión del perfil del usuario
 * @details Proporciona funcionalidades para editar perfil, cambiar imagen desde cámara/galería,
 *          compartir perfil y cerrar sesión
 */
@AndroidEntryPoint
class MyProfileFragment : Fragment() {

    /** @brief URI de la imagen seleccionada para actualizar */
    private var _img: Uri? = null

    /** @brief URI de la imagen actual del perfil */
    private var _currentImageUri: Uri? = null

    /** @brief Binding para el layout del fragment */
    private lateinit var binding: FragmentMyProfileBinding

    /** @brief ViewModel para operaciones del perfil */
    private val viewModel: MyProfileViewModel by activityViewModels()

    /** @brief ViewModel para operaciones de logout */
    private val logoutViewModel: LogoutViewModel by viewModels()

    /**
     * @brief Launcher para solicitar permisos de cámara
     * @details Maneja la respuesta del usuario a la solicitud de permisos
     */
    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            navigateToCamera()
        } else {
            showCameraPermissionError()
        }
    }

    /**
     * @brief Launcher para seleccionar media desde galería
     * @details Permite al usuario seleccionar imágenes de la galería
     */
    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            loadPhoto(uri)
        }
    }

    /**
     * @brief Crea la vista del fragment
     * @param inflater LayoutInflater para inflar la vista
     * @param container ViewGroup contenedor padre
     * @param savedInstanceState Estado guardado anterior
     * @return View La vista inflada del fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * @brief Configura la vista después de ser creada
     * @param view La vista del fragment
     * @param savedInstanceState Estado guardado anterior
     */
    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeCameraPhoto()
        observeLogoutState()
        observeProfileState()
    }

    /**
     * @brief Configura los listeners de los botones
     * @details Establece las acciones para logout, guardar, compartir, cámara y galería
     */
    private fun setupClickListeners() {
        binding.closeButton.setOnClickListener {
            logoutViewModel.logout()
        }

        binding.saveButton.setOnClickListener {
            saveProfile()
        }

        binding.shareButton.setOnClickListener {
            shareProfile()
        }

        binding.camera.setOnClickListener {
            handleCameraClick()
        }

        binding.galeria.setOnClickListener {
            openGallery()
        }
    }

    /**
     * @brief Observa cambios en las fotos capturadas desde la cámara
     * @details Reacciona a nuevas fotos tomadas y las carga en la interfaz
     */
    private fun observeCameraPhoto() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.photo.collect { uri ->
                    if (uri != null && uri != Uri.EMPTY) {
                        loadPhoto(uri)
                    }
                }
            }
        }
    }

    /**
     * @brief Observa el estado del proceso de logout
     * @details Reacciona a cambios en el estado de cierre de sesión
     */
    private fun observeLogoutState() {
        viewLifecycleOwner.lifecycleScope.launch {
            logoutViewModel.logoutState.collect { state ->
                when (state) {
                    is LogoutState.Loading -> {
                        binding.closeButton.isEnabled = true
                    }
                    is LogoutState.Success -> {
                        binding.closeButton.visibility = View.GONE
                        navigateToLogin()
                    }
                    is LogoutState.Error -> {
                        binding.closeButton.isEnabled = true
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    /**
     * @brief Observa el estado del perfil
     * @details Reacciona a cambios en las operaciones del perfil
     */
    private fun observeProfileState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is ProfileUiState.Loading -> {
                        binding.saveButton.isEnabled = false
                    }
                    is ProfileUiState.Success -> {
                        binding.saveButton.isEnabled = true
                        updateUI(uiState.adven)
                        Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                    }
                    is ProfileUiState.Error -> {
                        binding.saveButton.isEnabled = true
                        Toast.makeText(context, "Perfil no se actualizado correctamente", Toast.LENGTH_SHORT).show()
                    }
                    is ProfileUiState.Wait -> {
                        binding.saveButton.isEnabled = true
                        updateUI(uiState.adven)
                    }
                }
            }
        }
    }

    /**
     * @brief Guarda los cambios del perfil
     * @details Valida los datos y envía la actualización del perfil
     */
    private fun saveProfile() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val imageToUpload = _img ?: _currentImageUri

        if (validateInputs(name, email)) {
            viewModel.updateProfile(name, imageToUpload, email)
        }
    }

    /**
     * @brief Maneja el click en el botón de cámara
     * @details Verifica permisos y navega a la cámara si están concedidos
     */
    private fun handleCameraClick() {
        if (hasCameraPermissions()) {
            navigateToCamera()
        } else {
            requestCameraPermission()
        }
    }

    /**
     * @brief Abre la galería para seleccionar imágenes
     * @details Lanza el picker de media para seleccionar solo imágenes
     */
    private fun openGallery() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    /**
     * @brief Navega a la pantalla de login
     * @details Redirige al usuario a la actividad de login y cierra la actual
     */
    private fun navigateToLogin() {
        val action = MyProfileFragmentDirections.actionMyProfileFragmentToLoginActivity()
        findNavController().navigate(action)
        activity?.finish()
    }

    /**
     * @brief Comparte el perfil del usuario
     * @details Crea un intent para compartir información del perfil
     */
    private fun shareProfile() {
        val userName = binding.nameEditText.text.toString()
        val shareMessage = "Hola, soy $userName y estoy utilizando la aplicación ReadyToEnjoy"
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, "Compartir con..."))
    }

    /**
     * @brief Valida los datos de entrada del perfil
     * @param name Nombre del usuario
     * @param email Email del usuario
     * @return Boolean true si los datos son válidos, false en caso contrario
     */
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

    /**
     * @brief Actualiza la interfaz con los datos del aventurero
     * @param adven Datos del aventurero a mostrar
     */
    private fun updateUI(adven: Adven) {
        binding.nameEditText.setText(adven.name)
        binding.emailEditText.setText(adven.email)
        binding.profileImage.load(adven.media)
        _currentImageUri = adven.media
    }

    /**
     * @brief Carga una foto en la vista de imagen del perfil
     * @param uri URI de la imagen a cargar
     */
    private fun loadPhoto(uri: Uri?) {
        binding.profileImage.load(uri)
        _img = uri
    }

    /**
     * @brief Verifica si la app tiene permisos de cámara
     * @return Boolean true si tiene permisos, false en caso contrario
     */
    private fun hasCameraPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * @brief Solicita permisos de cámara al usuario
     */
    private fun requestCameraPermission() {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    /**
     * @brief Muestra error cuando no se conceden permisos de cámara
     */
    private fun showCameraPermissionError() {
        Toast.makeText(
            requireContext(),
            "No hay permisos para la cámara",
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * @brief Navega al fragment de cámara
     */
    private fun navigateToCamera() {
        val action = MyProfileFragmentDirections.actionMyProfileFragmentToCameraFragment()
        findNavController().navigate(action)
    }

}