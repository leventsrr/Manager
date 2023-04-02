package com.leventsurer.manager.ui.fragments

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
import com.leventsurer.manager.MainActivity
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.ManagerAnnouncementModel
import com.leventsurer.manager.data.model.ResidentsRequestModel
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.databinding.FragmentLoginBinding
import com.leventsurer.manager.databinding.FragmentManagerAnnouncementBinding
import com.leventsurer.manager.databinding.FragmentResidentsRequestsBinding
import com.leventsurer.manager.tools.adapters.ManagerAnnouncementAdapter
import com.leventsurer.manager.tools.adapters.ResidentRequestAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResidentsRequestsFragment : Fragment() {
    private var _binding: FragmentResidentsRequestsBinding? = null
    private val binding: FragmentResidentsRequestsBinding get() = _binding!!
    private val databaseViewModel by viewModels<DatabaseViewModel>()

    private val residentsRequestsAdapterList = ArrayList<ResidentsRequestModel>()
    private lateinit var residentsRequestsAdapter: ResidentRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResidentsRequestsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupManagerAnnouncementsAdapter()
        getResidentsRequests()
    }


    private fun setupManagerAnnouncementsAdapter() {
        binding.rwResidentsRequests.layoutManager = LinearLayoutManager(requireContext())
        residentsRequestsAdapter = ResidentRequestAdapter()
        binding.rwResidentsRequests.adapter = residentsRequestsAdapter
    }

    private fun getResidentsRequests() {
        databaseViewModel.getResidentRequests()
        observeResidentsRequestsFlow()
    }

    private fun observeResidentsRequestsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {

            databaseViewModel.residentRequestFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                        binding.progressBar.visibility = View.GONE

                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE

                        residentsRequestsAdapterList.addAll(it.result)
                        residentsRequestsAdapter.list = residentsRequestsAdapterList

                    }
                    else -> {

                    }
                }

            }

        }
    }

    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "Apartman Sakini İstekleri",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_arrow_back_24,
            endIcon = R.drawable.ic_baseline_settings_24,
            startIconClick = {
                findNavController().popBackStack()

            },
            endIconClick = {
            },
        )


        (requireActivity() as MainActivity).hideBottomNavigation()
    }


}