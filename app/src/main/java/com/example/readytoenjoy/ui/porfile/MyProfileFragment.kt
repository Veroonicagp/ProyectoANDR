package com.example.readytoenjoy.ui.porfile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var binding: FragmentMyProfileBinding
    private val viewModel: MyProfileViewModel by viewModels()

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

            if (validateInputs(name, email)) {
                viewModel.updateProfile(name, email)
            }
        }

        binding.shareButton.setOnClickListener {
            shareProfile()
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
    }
}