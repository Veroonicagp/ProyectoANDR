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
import coil.load
import com.example.readytoenjoy.databinding.FragmentActivityInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ActivityInfoFragment : Fragment() {

    private lateinit var binding: FragmentActivityInfoBinding
    private val vm: ActivityInfoViewModel by activityViewModels()
    private val args: ActivityInfoFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            vm.uiState.collect { uiState ->
                when (uiState) {
                    is InfoActivityUiState.Loading -> {
                    }

                    is InfoActivityUiState.Success -> {
                        val activity = uiState.activity
                        binding.apply {
                            binding.appBarLayout
                            topAppBar.title = (activity.title)
                            location.setText(activity.location)
                            crdImg.load(activity.img)
                            price.setText(activity.price)
                            description.setText(activity.description)
                        }
                    }

                    is InfoActivityUiState.Error -> {
                    }
                }
            }



        }

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

}