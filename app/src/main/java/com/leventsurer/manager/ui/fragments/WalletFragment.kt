package com.leventsurer.manager.ui.fragments

import android.os.Bundle
import android.util.Log
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
import com.leventsurer.manager.data.model.FinancialEventModel
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.data.model.UserModel
import com.leventsurer.manager.databinding.FragmentWalletBinding
import com.leventsurer.manager.tools.adapters.DuesPaymentStatusAdapter
import com.leventsurer.manager.tools.adapters.FinancialEventsDetailAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import com.leventsurer.manager.viewModels.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class WalletFragment : Fragment() {
    private var _binding: FragmentWalletBinding? = null
    private val binding: FragmentWalletBinding get() = _binding!!
    private val databaseViewModel by viewModels<DatabaseViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    private val sharedPreferencesViewModel by viewModels<SharedPreferencesViewModel>()
    //Adapter list
    private var duesPaymentStatusAdapterList = ArrayList<UserModel>()
    private var financialEventAdapterList = ArrayList<FinancialEventModel>()
    //Adapters
    private lateinit var duesPaymentStatusAdapter : DuesPaymentStatusAdapter
    private lateinit var financialEventsAdapter:FinancialEventsDetailAdapter


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
        setupFinancialEventsDetailAdapter()
        getUsers()
        getFinancialEvents()
        getApartmentBudget()
        observeApartmentData()
    }

    private fun getApartmentBudget() {
        databaseViewModel.getApartmentInfo()

    }

    private fun observeApartmentData() {
        databaseViewModel.apartmentLiveData.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading ->{}
                is Resource.Failure ->{}
                is Resource.Success ->{
                    binding.twCurrentBudget.text = it.result.budget.toString()
                }
                else->{}
            }
        }
    }

    private fun getUsers() {
        databaseViewModel.getAllApartmentUsers()
        observeUsers()
    }

    private fun getFinancialEvents(){
        databaseViewModel.getFinancialEvents()
        observeFinancialEvents()
    }

    private fun observeFinancialEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            databaseViewModel.financialEventsFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()

                    }
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        financialEventAdapterList = it.result
                        financialEventsAdapter.list = financialEventAdapterList
                    }
                    else -> {

                    }
                }
            }

        }
    }

    private fun observeUsers() {
        viewLifecycleOwner.lifecycleScope.launch {
            databaseViewModel.users.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()

                    }
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        duesPaymentStatusAdapterList = it.result
                        duesPaymentStatusAdapter.list = duesPaymentStatusAdapterList
                    }
                    else -> {

                    }
                }
            }

        }
    }

    //Aidat ödeme durumunun listeleneceği adapterın kurulumunu yapar
    private fun setupDuesPaymentStatusAdapter() {
        binding.rwDuesPaymentStatus.layoutManager = LinearLayoutManager(requireContext())
        duesPaymentStatusAdapter = DuesPaymentStatusAdapter()
        binding.rwDuesPaymentStatus.adapter = duesPaymentStatusAdapter
    }
    private fun setupFinancialEventsDetailAdapter(){
        binding.rwFinancialEvents.layoutManager = LinearLayoutManager(requireContext())
        financialEventsAdapter = FinancialEventsDetailAdapter()
        binding.rwFinancialEvents.adapter = financialEventsAdapter
    }


    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "Cüzdan",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_sensor_door_24,
            endIcon = R.drawable.ic_baseline_settings_24,
            startIconClick = {
                authViewModel.logout()
                sharedPreferencesViewModel.clearSharedPref()
                val action = WalletFragmentDirections.actionWalletFragmentToLoginFragment()
                findNavController().navigate(action)
            },
            endIconClick = {
                val action = WalletFragmentDirections.actionWalletFragmentToSettingsFragmet()
                findNavController().navigate(action)
            },
        )
        (requireActivity() as MainActivity).showBottomNavigation()
    }

}