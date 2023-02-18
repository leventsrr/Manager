package com.leventsurer.manager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.leventsurer.manager.R
import com.leventsurer.manager.databinding.FragmentResidentInformationDetailsBinding
import com.leventsurer.manager.tools.adapters.ResidentPastRequestAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper

class ResidentInformationDetailsFragment : Fragment() {
    private var _binding: FragmentResidentInformationDetailsBinding? = null
    private val binding: FragmentResidentInformationDetailsBinding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResidentInformationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupResidentPastRequestAdapter()
    }


    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "Apartman Sakini Detayı",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_arrow_back_24,
            endIcon = R.drawable.ic_baseline_settings_24,
            startIconClick = {
                findNavController().popBackStack()
            },
            endIconClick = {
                val action = ResidentInformationDetailsFragmentDirections.actionResidentInformationDetailsFragmentToUserProfileFragment()
                findNavController().navigate(action)
            },
        )
    }

    private fun setupResidentPastRequestAdapter() {
        binding.rwResidentPastRequest.layoutManager = LinearLayoutManager(requireContext())
        val residentsPastRequestAdapter = ResidentPastRequestAdapter()
        binding.rwResidentPastRequest.adapter = residentsPastRequestAdapter
    }


}