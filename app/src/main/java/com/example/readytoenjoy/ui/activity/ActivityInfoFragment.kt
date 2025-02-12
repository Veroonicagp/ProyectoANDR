package com.example.readytoenjoy.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.readytoenjoy.R
import com.example.readytoenjoy.core.data.network.activity.ActivityNetworkRepositoryInterface
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityInfoFragment : Fragment() {

    private lateinit var repository: ActivityNetworkRepositoryInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_info, container, false)
    }


}