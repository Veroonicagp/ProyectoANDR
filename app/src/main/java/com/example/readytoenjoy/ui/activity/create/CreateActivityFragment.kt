package com.example.readytoenjoy.ui.activity.create

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.readytoenjoy.databinding.FragmentCreateActivityBinding
import kotlinx.coroutines.launch

class CreateActivityFragment : Fragment() {

    private var _img: Uri? = null
    private lateinit var binding: FragmentCreateActivityBinding
    private val vm: CreateActivityViewModel by activityViewModels()

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

    private fun loadPhoto(uri:Uri?) {
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
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collect {
                        uiState ->
                    when(uiState) {
                        is UiState.Created -> {
                            // Se ha creado el incidente, volvemos
                            findNavController().popBackStack()
                        }
                        is UiState.Error -> {

                        }

                        else -> {}
                    }
                }
            }
        }
        binding.crearBttn.setOnClickListener {
            val title = binding.title.text.toString()
            val location = binding.location.text.toString()
            val img = _img
            val advenId = ""
            val price = binding.location.text.toString()
            val description = binding.description.text.toString()
            viewLifecycleOwner.lifecycleScope.launch {
                vm.create(title,img,location,price,description,advenId)
            }
        }

        binding.photoBttn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }


}