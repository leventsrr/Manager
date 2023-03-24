package com.leventsurer.manager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.leventsurer.manager.MainActivity
import com.leventsurer.manager.R
import com.leventsurer.manager.databinding.FragmentSettingsFragmetBinding
import com.leventsurer.manager.tools.helpers.HeaderHelper

class SettingsFragmet : Fragment() {
    private var _binding: FragmentSettingsFragmetBinding? = null
    private val binding: FragmentSettingsFragmetBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsFragmetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }


    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "Ayarlar",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_arrow_back_24,
            endIcon = R.drawable.ic_baseline_settings_24,
            startIconClick = {
                findNavController().popBackStack()
            },
            endIconClick = {
                /*val action = SettingsFragmetDirections.actionSettingsFragmetToUserProfileFragment2()
                findNavController().navigate(action)*/
            },
        )

        (requireActivity() as MainActivity).hideBottomNavigation()
    }

}