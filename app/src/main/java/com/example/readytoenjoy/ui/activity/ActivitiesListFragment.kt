package com.example.readytoenjoy.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.core.utils.ConnectivityHelper
import com.example.readytoenjoy.databinding.FragmentActivityListBinding
import com.example.readytoenjoy.ui.utils.OfflineUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ActivitiesListFragment : Fragment() {

    private lateinit var binding: FragmentActivityListBinding
    private val viewModel: ActivityListViewModel by viewModels()

    @Inject
    lateinit var connectivityHelper: ConnectivityHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkConnectivityOnStart()
        setupRecyclerView()
        loadActivities()
    }

    private fun checkConnectivityOnStart() {
        if (!connectivityHelper.isNetworkAvailable()) {
            OfflineUtils.showOfflineMessage(binding.root)
        }
    }

    private fun setupRecyclerView() {
        val rv = binding.rvActivities
        rv.adapter = ActivityListAdapter(::toActivityDetail)
        rv.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    ActivityListUiState.Loading -> {}
                    is ActivityListUiState.Success -> {
                        (rv.adapter as ActivityListAdapter).submitList(uiState.activityList)
                    }
                    is ActivityListUiState.Error -> {
                        if (!connectivityHelper.isNetworkAvailable()) {
                            OfflineUtils.showOfflineMessage(binding.root)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (connectivityHelper.isNetworkAvailable()) {
            viewModel.loadActivities()
        }
    }

    private fun loadActivities() {
        if (connectivityHelper.isNetworkAvailable()) {
            viewModel.loadActivities()
        }
    }

    private fun toActivityDetail(activity: Activity) {
        val action = ActivitiesListFragmentDirections.actionActivitiesListFragmentToActivityInfoFragment(activity.id)
        findNavController().navigate(action)
    }
}