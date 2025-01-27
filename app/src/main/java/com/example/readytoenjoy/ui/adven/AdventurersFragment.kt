package com.example.readytoenjoy.ui.adven

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readytoenjoy.databinding.FragmentAdventurersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AdventurersFragment : Fragment() {
    private lateinit var binding: FragmentAdventurersBinding
    private val viewModel: AdvenListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAdventurersBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val rv = binding.rvAventureros
            rv.adapter = AdvenListAdapter()
            binding.rvAventureros.layoutManager = LinearLayoutManager(context)

            viewModel.uiState.collect{
                uiState->
                when (uiState){
                    AdvenListUiState.Loading->{}
                    is AdvenListUiState.Success->{
                        (rv.adapter as AdvenListAdapter).submitList(uiState.advenList)
                    }
                    is AdvenListUiState.Error->{

                    }
                }
            }
        }
    }


}