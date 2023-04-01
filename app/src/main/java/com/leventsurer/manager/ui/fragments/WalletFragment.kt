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
import com.leventsurer.manager.viewModels.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class WalletFragment : Fragment() {
    private var _binding: FragmentWalletBinding? = null
    private val binding: FragmentWalletBinding get() = _binding!!
    private val databaseViewModel by viewModels<DatabaseViewModel>()
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
    }

    private fun getUsers() {
        Log.e("kontrol","fragment içinde")
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
                        Log.e("kontrol","fragment içinde observe -> Failure")

                    }
                    is Resource.Loading -> {
                        Log.e("kontrol","fragment içinde observe -> Loading")
                    }
                    is Resource.Success -> {
                        Log.e("kontrol","fragment içinde observe -> Success")
                        Log.e("kontrol","gelen liste ${it.result}")
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
                        Log.e("kontrol","fragment içinde observe -> Failure")

                    }
                    is Resource.Loading -> {
                        Log.e("kontrol","fragment içinde observe -> Loading")
                    }
                    is Resource.Success -> {
                        Log.e("kontrol","fragment içinde observe -> Success")
                        Log.e("kontrol","gelen liste ${it.result}")
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
                findNavController().popBackStack()
            },
            endIconClick = {
                val action = WalletFragmentDirections.actionWalletFragmentToSettingsFragmet()
                findNavController().navigate(action)
            },
        )
        (requireActivity() as MainActivity).showBottomNavigation()
    }

}