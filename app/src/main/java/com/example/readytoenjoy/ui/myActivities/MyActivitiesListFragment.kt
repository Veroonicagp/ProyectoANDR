package com.example.readytoenjoy.ui.myActivities

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
import com.example.readytoenjoy.databinding.FragmentMyActivitiesListBinding
import com.example.readytoenjoy.ui.activity.ActivityListAdapter
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
        //¡?
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val rv = binding.rvMyActivities
            rv.adapter = MyActivityListAdapter(::toActivityDetail)
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



        //binding.rvMyActivities.layoutManager = LinearLayoutManager(context)

        //val adapter = ActivityListAdapter()
        //val rv = binding.rvMyActivities
        //rv.adapter = adapter
        //(rv.adapter as  ActivityListAdapter).submitList(repository.getActivities())

        val createActivityButton = binding.floatingActionButton
        createActivityButton.setOnClickListener{
            val action = MyActivitiesListFragmentDirections.actionMyActivitiesListFragmentToCreateActivityFragment()
            findNavController().navigate(action)
        }


    }

    //¡?  implementar cuando lo tenga hecho
    private fun toActivityDetail(activity: Activity) {
       val action = MyActivitiesListFragmentDirections.actionMyActivitiesListFragmentToEditActivityFragment()
        findNavController().navigate(action)
    }


}