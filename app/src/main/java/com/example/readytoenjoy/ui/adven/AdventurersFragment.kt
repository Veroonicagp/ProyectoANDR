package com.example.readytoenjoy.ui.adven

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readytoenjoy.core.model.Adven
import com.example.readytoenjoy.core.utils.ConnectivityHelper
import com.example.readytoenjoy.databinding.FragmentAdventurersBinding
import com.example.readytoenjoy.ui.utils.OfflineUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AdventurersFragment : Fragment() {
    private lateinit var binding: FragmentAdventurersBinding
    private val viewModel: AdvenListViewModel by viewModels()
    private lateinit var advenListAdapter: AdvenListAdapter

    @Inject
    lateinit var connectivityHelper: ConnectivityHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdventurersBinding.inflate(
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
        observeDeleteResults()
    }

    private fun checkConnectivityOnStart() {
        if (!connectivityHelper.isNetworkAvailable()) {
            OfflineUtils.showOfflineMessage(binding.root)
        }
    }

    private fun setupRecyclerView() {
        lifecycleScope.launch {
            val rv = binding.rvAventureros
            advenListAdapter = AdvenListAdapter(
                onAdvenClick = ::onAdvenClick,
                onDeleteClick = ::onDeleteClick
            )
            rv.adapter = advenListAdapter
            rv.layoutManager = LinearLayoutManager(context)

            viewModel.isAdmin.collect { isAdmin ->
                advenListAdapter.updateAdminStatus(isAdmin)
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    AdvenListUiState.Loading -> {
                    }
                    is AdvenListUiState.Success -> {
                        advenListAdapter.submitList(uiState.advenList)
                    }
                    is AdvenListUiState.Error -> {
                        if (!connectivityHelper.isNetworkAvailable()) {
                            OfflineUtils.showOfflineMessage(binding.root)
                        } else {
                            showError(uiState.message)
                        }
                    }
                }
            }
        }
    }

    private fun observeDeleteResults() {
        lifecycleScope.launch {
            viewModel.deleteResult.collect { result ->
                result?.let {
                    when (it) {
                        is DeleteResult.Success -> {
                            showSuccess(it.message)
                        }
                        is DeleteResult.Error -> {
                            showError(it.message)
                        }
                    }
                    viewModel.clearDeleteResult()
                }
            }
        }
    }

    private fun onAdvenClick(adven: Adven) {
        val action = AdventurersFragmentDirections.actionAdventurersFragmentToActivitiesAdvenList(adven.id)
        findNavController().navigate(action)
    }

    private fun onDeleteClick(adven: Adven) {
        showDeleteConfirmationDialog(adven)
    }

    private fun showDeleteConfirmationDialog(adven: Adven) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que quieres eliminar a ${adven.name}?\n\nEsta acción eliminará el aventurero y todas sus actividades.")
            .setPositiveButton("Eliminar") { _, _ ->
                deleteAdvenAndUser(adven)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteAdvenAndUser(adven: Adven) {
        lifecycleScope.launch {
            viewModel.deleteAdvenAndUser(adven.id)
        }
    }

    private fun showSuccess(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(resources.getColor(android.R.color.holo_green_dark, null))
            .show()
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(resources.getColor(android.R.color.holo_red_dark, null))
            .show()
    }
}