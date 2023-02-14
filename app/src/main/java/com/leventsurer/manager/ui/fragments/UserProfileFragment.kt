package com.leventsurer.manager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.leventsurer.manager.R
import com.leventsurer.manager.databinding.FragmentSignupBinding
import com.leventsurer.manager.databinding.FragmentUserProfileBinding
import com.leventsurer.manager.tools.helpers.HeaderHelper

class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding: FragmentUserProfileBinding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }



    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "Profiliniz",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_arrow_back_24,
            endIcon = R.drawable.ic_baseline_account_circle_24,
            startIconClick = {
                findNavController().popBackStack()
            },
            endIconClick = {
            },
        )
    }

}