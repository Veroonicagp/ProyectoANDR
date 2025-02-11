package com.example.readytoenjoy.ui.activity.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.readytoenjoy.databinding.FragmentCreateActivityBinding

class CreateActivityFragment : Fragment() {

    private lateinit var binding: FragmentCreateActivityBinding



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateActivityBinding.inflate(
            inflater,
            container,
            false
        )
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.crearBttn.setOnClickListener {
            val title = binding.title.text.toString()
            val location = binding.location.text.toString()
            val price = binding.location.text.toString()
            val description = binding.description.text.toString()
            findNavController().popBackStack()

        }

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }


}