package com.example.readytoenjoy.ui.activity.info

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.readytoenjoy.R
import com.example.readytoenjoy.core.data.network.activity.ActivityNetworkRepositoryInterface
import com.example.readytoenjoy.databinding.FragmentActivityInfoBinding
import com.example.readytoenjoy.databinding.FragmentEditActivityBinding
import com.example.readytoenjoy.ui.activity.edit.EditActivityFragmentArgs
import com.example.readytoenjoy.ui.activity.edit.EditActivityUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

class ActivityInfoFragment : Fragment() {

    private lateinit var binding: FragmentActivityInfoBinding
    private val vm: ActivityInfoViewModel by activityViewModels()
    private val args: EditActivityFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActivityInfoBinding.inflate(
            inflater,
            container,
            false
        )
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        vm.loadActivity(args.activityId)
        lifecycleScope.launch {
            vm.uiState.collect { state ->
                when (state) {
                    is EditActivityUiState.Loading -> {
                        // Mostrar progreso
                    }

                    is EditActivityUiState.Success -> {
                        val activity = state.activity
                        binding.apply {
                            binding.appBarLayout
                            topAppBar.title = (activity.title)
                            location.setText(activity.location)
                            price.setText(activity.price)
                            description.setText(activity.description)
                        }
                    }

                    is EditActivityUiState.Error -> {
                        // Mostrar error
                    }
                }
            }



        }

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

}