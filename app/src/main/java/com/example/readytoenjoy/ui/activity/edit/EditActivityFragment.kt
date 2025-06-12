package com.example.readytoenjoy.ui.activity.edit

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.databinding.FragmentEditActivityBinding
import kotlinx.coroutines.launch

class EditActivityFragment : Fragment() {

    private var _img: Uri? = null
    private var _currentImageUri: Uri? = null
    private lateinit var binding: FragmentEditActivityBinding
    private val vm: EditActivityViewModel by activityViewModels()
    private val args: EditActivityFragmentArgs by navArgs()

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
        binding = FragmentEditActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        loadActivityData()
        observeUiState()
    }

    private fun setupClickListeners() {
        binding.crearBttn.setOnClickListener {
            updateActivity()
        }

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.photoBttn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun loadActivityData() {
        vm.loadActivity(args.activityId)
    }

    private fun updateActivity() {
        val id = args.activityId
        val title = binding.title.text.toString()
        val price = binding.price.text.toString()
        val description = binding.description.text.toString()
        val location = binding.location.text.toString()
        val imageToUpload = _img ?: _currentImageUri

        vm.updateActivity(id, title, imageToUpload, price, location, description)
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            vm.uiState.collect { uiState ->
                when (uiState) {
                    is EditActivityUiState.Loading -> {
                        binding.crearBttn.isEnabled = false
                    }
                    is EditActivityUiState.Success -> {
                        binding.crearBttn.isEnabled = true
                        updateUI(uiState.activity)
                        Toast.makeText(context, "Actividad editada correctamente", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    is EditActivityUiState.Error -> {
                        Toast.makeText(context, "Actividad no editada", Toast.LENGTH_SHORT).show()
                        binding.crearBttn.isEnabled = true
                    }
                    is EditActivityUiState.Wait -> {
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
    }

    private fun updateUI(activity: Activity) {
        binding.title.setText(activity.title)
        binding.location.setText(activity.location)
        binding.price.setText(activity.price)
        binding.imagenAct.load(activity.img)
        binding.description.setText(activity.description)
        _currentImageUri = activity.img
    }
}
