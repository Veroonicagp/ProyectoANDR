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
import com.example.readytoenjoy.core.data.activity.Activity
import com.example.readytoenjoy.databinding.FragmentActivityListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ActivitiesListFragment : Fragment() {

    private lateinit var binding: FragmentActivityListBinding
    private val viewModel: ActivityListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentActivityListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //¡?
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val rv = binding.rvActivities
            rv.adapter = ActivityListAdapter(::toActivityDetail)
            binding.rvActivities.layoutManager = LinearLayoutManager(context)

            viewModel.uiState.collect{
                uiState->
                when (uiState){
                    ActivityListUiState.Loading->{}
                    is ActivityListUiState.Success->{
                        (rv.adapter as ActivityListAdapter).submitList(uiState.activityList)
                    }
                    is ActivityListUiState.Error->{

                    }
                }
            }


        }




        //(rv.adapter as  ActivityListAdapter).submitList(repository.getActivities())

    }
    //override fun onResume() {
       // super.onResume()
        //val recyclerView = binding.rvActivities
        //(recyclerView.adapter as ActivityListAdapter).submitList(repository.getActivities())


   // }

    //¡?

    private fun toActivityDetail(activity: Activity) {
        val action = ActivitiesListFragmentDirections.actionActivitiesListFragmentToActivityInfoFragment()
        findNavController().navigate(action)
    }


}