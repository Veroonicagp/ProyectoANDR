package com.example.readytoenjoy.ui.myActivities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readytoenjoy.core.model.Activity
import com.example.readytoenjoy.core.utils.ConnectivityHelper
import com.example.readytoenjoy.databinding.FragmentMyActivitiesListBinding
import com.example.readytoenjoy.ui.utils.OfflineUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyActivitiesListFragment : Fragment() {

    private lateinit var binding: FragmentMyActivitiesListBinding
    private val viewModel: MyActivityListViewModel by viewModels()

    @Inject
    lateinit var connectivityHelper: ConnectivityHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyActivitiesListBinding.inflate(
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
        observeViewModel()
        setupFab()
    }

    private fun checkConnectivityOnStart() {
        if (!connectivityHelper.isNetworkAvailable()) {
            OfflineUtils.showOfflineMessage(binding.root)
        }
    }

    private fun setupRecyclerView() {
        lifecycleScope.launch {
            val rv = binding.rvMyActivities
            rv.adapter = MyActivityListAdapter(::toActivityDetail, ::deleteActivity)
            binding.rvMyActivities.layoutManager = LinearLayoutManager(context)

            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    MyActivityListUiState.Loading -> {}
                    is MyActivityListUiState.Success -> {
                        (rv.adapter as MyActivityListAdapter).submitList(uiState.myActivityList)
                    }
                    is MyActivityListUiState.Error -> {
                        if (!connectivityHelper.isNetworkAvailable()) {
                            OfflineUtils.showOfflineMessage(binding.root)
                        }
                    }
                }
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deleteState.collect { state ->
                when (state) {
                    DeleteActivityState.Loading -> {}
                    DeleteActivityState.DeleteSuccess -> {
                        Toast.makeText(context, "Actividad eliminada correctamente", Toast.LENGTH_SHORT).show()
                        viewModel.resetDeleteState()
                    }
                    is DeleteActivityState.DeleteError -> {
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                        viewModel.resetDeleteState()
                    }
                }
            }
        }
    }

    private fun setupFab() {
        binding.floatingActionButton.setOnClickListener {
            if (connectivityHelper.isNetworkAvailable()) {
                val action = MyActivitiesListFragmentDirections.actionMyActivitiesListFragmentToCreateActivityFragment()
                findNavController().navigate(action)
            } else {
                OfflineUtils.showNeedConnectionMessage(binding.root, "crear actividades")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        viewModel.load()
    }

    private fun deleteActivity(activity: Activity) {
        if (connectivityHelper.isNetworkAvailable()) {
            viewModel.deleteActivity(activity)
        } else {
            OfflineUtils.showNeedConnectionMessage(binding.root, "eliminar actividades")
        }
    }

    private fun toActivityDetail(activity: Activity) {
        findNavController().navigate(
            MyActivitiesListFragmentDirections.actionMyActivitiesListFragmentToEditActivityFragment(activity.id)
        )
    }
}