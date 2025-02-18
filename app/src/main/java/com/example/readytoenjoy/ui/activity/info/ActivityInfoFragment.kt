package com.example.readytoenjoy.ui.activity.info

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.readytoenjoy.R
import com.example.readytoenjoy.core.data.network.activity.ActivityNetworkRepositoryInterface
import com.example.readytoenjoy.databinding.FragmentActivityInfoBinding
import com.example.readytoenjoy.databinding.FragmentEditActivityBinding
import dagger.hilt.android.AndroidEntryPoint

class ActivityInfoFragment : Fragment() {

    private lateinit var binding: FragmentActivityInfoBinding
    private val vmk: ActivityInfoViewModel by activityViewModels()
    private lateinit var repository: ActivityNetworkRepositoryInterface


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

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }



}