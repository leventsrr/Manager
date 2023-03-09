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
import androidx.recyclerview.widget.GridLayoutManager
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.data.model.UserModel
import com.leventsurer.manager.databinding.FragmentResidentsInformationBinding
import com.leventsurer.manager.tools.adapters.DuesPaymentStatusAdapter
import com.leventsurer.manager.tools.adapters.ResidentsInformationAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class ResidentsInformationFragment : Fragment() {
    private var _binding: FragmentResidentsInformationBinding? = null
    private val binding: FragmentResidentsInformationBinding get() = _binding!!
    private val databaseViewModel by viewModels<DatabaseViewModel>()

    //Adapter list
    private var residentsInformationAdapterList = ArrayList<UserModel>()
    //Adapters
    private lateinit var residentsInformationAdapter : ResidentsInformationAdapter
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
        getUsers()
    }

    private fun getUsers() {
        Log.e("kontrol","fragment içinde")
        databaseViewModel.getAllApartmentUsers()
        observeUsers()
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
                        residentsInformationAdapterList = it.result
                        residentsInformationAdapter.list = residentsInformationAdapterList
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
            title = "Apartman Sakinleri",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_sensor_door_24,
            endIcon = R.drawable.ic_baseline_settings_24,
            startIconClick = {
                findNavController().popBackStack()
            },
            endIconClick = {
                val action = ResidentsInformationFragmentDirections.actionResidentsInformationFragmentToSettingsFragmet()
                findNavController().navigate(action)
            },
        )
    }
    //apartman sakinlerinin listeleneceği adapter ın kurulumunu yapar
    private fun setupConciergeDutyToDoAdapter() {
        binding.rwResidentsInformation.layoutManager = GridLayoutManager(requireContext(), 3)
        residentsInformationAdapter = ResidentsInformationAdapter()
        binding.rwResidentsInformation.adapter = residentsInformationAdapter
        residentsInformationAdapter.moveDetailPage {
            val action = ResidentsInformationFragmentDirections.actionResidentsInformationFragmentToResidentInformationDetailsFragment(it)
            findNavController().navigate(action)
        }
    }


}