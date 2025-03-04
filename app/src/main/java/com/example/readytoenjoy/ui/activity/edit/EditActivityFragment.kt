package com.example.readytoenjoy.ui.activity.edit

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.core.model.Adven
import com.example.readytoenjoy.databinding.FragmentEditActivityBinding
import com.example.readytoenjoy.ui.myActivities.MyActivityListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class EditActivityFragment : Fragment() {


    private var _img: Uri? = null
    private var _currentImageUri: Uri? = null
    private var _photoUri: Uri? = null
    private lateinit var binding: FragmentEditActivityBinding
    private val vm: EditActivityViewModel by activityViewModels()
    private val args: EditActivityFragmentArgs by navArgs()

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Si la uril no es nula, es que el usuario ha selccionado algín archivo
        if (uri != null) {
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
    ): View? {
        binding = FragmentEditActivityBinding.inflate(
            inflater,
            container,
            false
        )
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            with(binding) {
               crearBttn.setOnClickListener {
                   val id = args.activityId
                   val title = title.text.toString()
                    val price  = price.text.toString()
                    val img = _photoUri
                    val description  = description.text.toString()
                    val location = location.text.toString()
                   // Usamos la imagen seleccionada si existe, o mantenemos la actual
                   val imageToUpload = _img ?: _currentImageUri
                   vm.updateActivity(id,title,imageToUpload,location,price,description)
                }
            }


        vm.loadActivity(args.activityId)

        lifecycleScope.launch {
            vm.uiState.collect { uiState ->
                when (uiState) {
                    is EditActivityUiState.Loading -> {
                        binding.crearBttn.isEnabled = false
                    }
                    is EditActivityUiState.Success -> {
                        binding.crearBttn.isEnabled = true
                        updateUI(uiState.activity)
                        Snackbar.make(
                            binding.root,
                            "Actividad actualizada correctamente",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack()

                    }
                    is EditActivityUiState.Error -> {
                        binding.crearBttn.isEnabled = true
                    }

                    is EditActivityUiState.Wait ->{
                        binding.crearBttn.isEnabled = true
                        updateUI(uiState.activity)
                    }

                    is EditActivityUiState.ActivityLoaded -> {
                        binding.crearBttn.isEnabled = true
                        updateUI(uiState.activity)
                    }
                }
            }
        }
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.photoBttn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

    }
    private fun updateUI(activity: Activity) {
        binding.apply {
            title.setText(activity.title)
            location.setText(activity.location)
            price.setText(activity.price)
            imagenAct.load(activity.img)
            description.setText(activity.description)
            _currentImageUri = activity.img
        }
    }

}
