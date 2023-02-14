package com.leventsurer.manager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.leventsurer.manager.R
import com.leventsurer.manager.databinding.FragmentResidentsInformationBinding
import com.leventsurer.manager.tools.adapters.ResidentsInformationAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper

class ResidentsInformationFragment : Fragment() {
    private var _binding: FragmentResidentsInformationBinding? = null
    private val binding: FragmentResidentsInformationBinding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResidentsInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupConciergeDutyToDoAdapter()
    }

    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "Apartman Sakinleri",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_sensor_door_24,
            endIcon = R.drawable.ic_baseline_account_circle_24,
            startIconClick = {
                findNavController().popBackStack()
            },
            endIconClick = {
                val action = ResidentsInformationFragmentDirections.actionResidentsInformationFragmentToUserProfileFragment2()
                findNavController().navigate(action)
            },
        )
    }

    private fun setupConciergeDutyToDoAdapter() {
        binding.rwResidentsInformation.layoutManager = GridLayoutManager(requireContext(), 3)
        val residentsInformationAdapter = ResidentsInformationAdapter()
        binding.rwResidentsInformation.adapter = residentsInformationAdapter
    }


}