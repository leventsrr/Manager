package com.leventsurer.manager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.leventsurer.manager.R
import com.leventsurer.manager.databinding.FragmentWalletBinding
import com.leventsurer.manager.tools.adapters.DuesPaymentStatusAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper

class WalletFragment : Fragment() {
    private var _binding: FragmentWalletBinding? = null
    private val binding: FragmentWalletBinding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWalletBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupDuesPaymentStatusAdapter()
    }


    private fun setupDuesPaymentStatusAdapter() {
        binding.rwDuesPaymentStatus.layoutManager = LinearLayoutManager(requireContext())
        val duesPaymentStatusAdapter = DuesPaymentStatusAdapter()
        binding.rwDuesPaymentStatus.adapter = duesPaymentStatusAdapter
    }


    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "Cüzdan",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_sensor_door_24,
            endIcon = R.drawable.ic_baseline_account_circle_24,
            startIconClick = {
                findNavController().popBackStack()
            },
            endIconClick = {
                val action = WalletFragmentDirections.actionWalletFragmentToUserProfileFragment2()
                findNavController().navigate(action)
            },
        )
    }

}