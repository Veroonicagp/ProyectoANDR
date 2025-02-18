package com.example.readytoenjoy.ui.activity.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.readytoenjoy.databinding.FragmentEditActivityBinding

class EditActivityFragment : Fragment() {

    private lateinit var binding: FragmentEditActivityBinding
    private val vm: EditActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditActivityBinding.inflate(
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