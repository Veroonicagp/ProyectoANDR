package com.example.readytoenjoy.ui.activity.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.core.model.Adven
import com.example.readytoenjoy.databinding.FragmentEditActivityBinding
import com.example.readytoenjoy.ui.myActivities.MyActivityListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class EditActivityFragment : Fragment() {

    private lateinit var binding: FragmentEditActivityBinding
    private val vm: EditActivityViewModel by activityViewModels()
    private val args: EditActivityFragmentArgs by navArgs()

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
                    val description  = description.text.toString()
                    val location = location.text.toString()
                   vm.updateActivity(id,title,price,location,description)
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
                            "Perfil actualizado correctamente",
                            Snackbar.LENGTH_SHORT
                        ).show()

                    }
                    is EditActivityUiState.Error -> {
                        binding.crearBttn.isEnabled = true
                    }

                    is EditActivityUiState.Wait ->{
                        binding.crearBttn.isEnabled = true
                        updateUI(uiState.activity)
                    }

                }
            }
        }
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }
    private fun updateUI(activity: Activity) {
        binding.apply {
            title.setText(activity.title)
            location.setText(activity.location)
            price.setText(activity.price)
            description.setText(activity.description)
        }
    }

}
