package com.example.readytoenjoy.ui.activity.create

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.readytoenjoy.databinding.FragmentCreateActivityBinding
import kotlinx.coroutines.launch

class CreateActivityFragment : Fragment() {

    private lateinit var binding: FragmentCreateActivityBinding
    private val vm: CreateActivityViewModel by activityViewModels()



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
            val price = binding.location.text.toString()
            val description = binding.description.text.toString()
            viewLifecycleOwner.lifecycleScope.launch {
                vm.create(title,location,price,description)
            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }


}