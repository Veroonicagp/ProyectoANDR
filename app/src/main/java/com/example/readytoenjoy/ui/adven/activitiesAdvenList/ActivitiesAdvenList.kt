package com.example.readytoenjoy.ui.adven.activitiesAdvenList

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.databinding.FragmentActivitiesAdvenListBinding
import com.example.readytoenjoy.ui.myActivities.MyActivitiesListFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ActivitiesAdvenList : Fragment() {
    private lateinit var binding: FragmentActivitiesAdvenListBinding
    private val vm: AdvenActListViewModel by viewModels()
    private val args: ActivitiesAdvenListArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivitiesAdvenListBinding.inflate(
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

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launch {
            val rv = binding.rvActivities
            rv.adapter = ActivitiesAdvenListAdapter(::toActivityDetail)
            binding.rvActivities.layoutManager = LinearLayoutManager(context)

            vm.uiState.collect{
                    uiState->
                when (uiState){
                    is AdvenActListViewModel.ActivitiesListUiState.Error -> {

                    }
                    AdvenActListViewModel.ActivitiesListUiState.Loading -> {}
                    is AdvenActListViewModel.ActivitiesListUiState.Success -> {
                        (rv.adapter as ActivitiesAdvenListAdapter).submitList(uiState.ActivitiesList)
                    }
                }
            }
        }

    }
    override fun onResume() {
        super.onResume()
        refreshData()
    }
    private fun refreshData() {
        vm.load(args.advenId)
    }

    private fun toActivityDetail(activity: Activity) {
        findNavController().navigate(
            ActivitiesAdvenListDirections.actionActivitiesAdvenListToActivityInfoFragment2(activity.id)
        )
    }
}