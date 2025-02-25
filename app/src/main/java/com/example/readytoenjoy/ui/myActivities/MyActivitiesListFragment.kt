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
import com.example.readytoenjoy.databinding.FragmentMyActivitiesListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyActivitiesListFragment : Fragment() {

    private lateinit var binding: FragmentMyActivitiesListBinding
    private val viewModel: MyActivityListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyActivitiesListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val rv = binding.rvMyActivities
            rv.adapter = MyActivityListAdapter(::toActivityDetail, ::deleteActivity)
            binding.rvMyActivities.layoutManager = LinearLayoutManager(context)

            viewModel.uiState.collect{
                    uiState->
                when (uiState){
                    MyActivityListUiState.Loading ->{}
                    is MyActivityListUiState.Success ->{
                        (rv.adapter as MyActivityListAdapter).submitList(uiState.myActivityList)
                    }
                    is MyActivityListUiState.Error ->{

                    }

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.deleteState.collect { state ->
                when (state) {
                    DeleteActivityState.Loading -> {
                        // No hacer nada
                    }
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


        val createActivityButton = binding.floatingActionButton
        createActivityButton.setOnClickListener{
            val action = MyActivitiesListFragmentDirections.actionMyActivitiesListFragmentToCreateActivityFragment()
            findNavController().navigate(action)
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
        viewModel.deleteActivity(activity)
    }

    private fun toActivityDetail(activity: Activity) {
        findNavController().navigate(
            MyActivitiesListFragmentDirections.actionMyActivitiesListFragmentToEditActivityFragment(activity.id)
        )
    }
}