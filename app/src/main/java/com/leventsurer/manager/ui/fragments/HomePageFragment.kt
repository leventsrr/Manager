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
import com.leventsurer.manager.data.model.*
import com.leventsurer.manager.databinding.FragmentHomePageBinding
import com.leventsurer.manager.tools.adapters.ConciergeAnnouncementAdapter
import com.leventsurer.manager.tools.adapters.ManagerAnnouncementAdapter
import com.leventsurer.manager.tools.adapters.ResidentRequestAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import com.leventsurer.manager.viewModels.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomePageFragment : Fragment() {
    private var _binding: FragmentHomePageBinding? = null
    private val binding: FragmentHomePageBinding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()
    private val sharedPrefViewModel by viewModels<SharedPreferencesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()

        readSharedPref()
        onClickHandler()
    }

    private fun onClickHandler() {
        binding.apply {
            mcwManagerAnnouncementsCard.setOnClickListener {
                val action = HomePageFragmentDirections.actionExecutiveHomePageToManagerAnnouncementFragment()
                findNavController().navigate(action)
            }
            mcwConciergeAnnouncementsCard.setOnClickListener {
                val action = HomePageFragmentDirections.actionExecutiveHomePageToConciergeAnnouncementsFragment()
                findNavController().navigate(action)
            }
            mcwResidentsRequestsCard.setOnClickListener {
                val action = HomePageFragmentDirections.actionExecutiveHomePageToResidentsRequestsFragment()
                findNavController().navigate(action)
            }
        }

    }

    private fun readSharedPref() {
        val isLogin = sharedPrefViewModel.readIsLogin()
        val apartmentName = sharedPrefViewModel.readApartmentName()
        val userName = sharedPrefViewModel.readUserName()
        val userDocumentId = sharedPrefViewModel.readUserDocumentId()
    }

    private fun setupUi() {
        HeaderHelper.customHeader(
           binding.customHeader,
            title = "Anasayfa",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_sensor_door_24,
            endIcon = R.drawable.ic_baseline_settings_24,
            startIconClick = {

                viewModel.logout()
                findNavController().popBackStack()

            },
            endIconClick = {
                val action = HomePageFragmentDirections.actionExecutiveHomePageToSettingsFragmet()
                findNavController().navigate(action)
            },
        )


        (requireActivity() as MainActivity).showBottomNavigation()
    }


}