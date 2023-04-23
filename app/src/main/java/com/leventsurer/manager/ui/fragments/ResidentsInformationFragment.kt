package com.leventsurer.manager.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.leventsurer.manager.MainActivity
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.data.model.UserModel
import com.leventsurer.manager.databinding.FragmentResidentsInformationBinding
import com.leventsurer.manager.tools.adapters.DuesPaymentStatusAdapter
import com.leventsurer.manager.tools.adapters.ResidentsInformationAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import com.leventsurer.manager.viewModels.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResidentsInformationFragment : Fragment() {
    private var _binding: FragmentResidentsInformationBinding? = null
    private val binding: FragmentResidentsInformationBinding get() = _binding!!
    private val databaseViewModel by viewModels<DatabaseViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    private val sharedPrefViewModel by viewModels<SharedPreferencesViewModel>()
    private val filteredList = ArrayList<UserModel>()

    //Adapter list
    private var residentsInformationAdapterList = ArrayList<UserModel>()

    //Adapters
    private lateinit var residentsInformationAdapter: ResidentsInformationAdapter
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
        observeUsers()
        onClickHandler()
        filterList()
    }

    //Liste filtrelemesi yapar
    private fun filterList() {
        binding.swFilter.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //Yazılan kelimenin apartman sakinleri listesindeki kişilerden herhangi birinin araç plakası,adı veya telefon numarasıyla eşleşme kontrolü yapılır
                for (resident in residentsInformationAdapterList) {
                    if (resident.carPlate.contains(
                            newText!!,
                            ignoreCase = true
                        ) || resident.fullName.contains(
                            newText,
                            ignoreCase = true
                        ) || resident.phoneNumber.contains(newText, ignoreCase = true)
                    ) {
                        //eşlelşme varsa filtrelenmiş listeye eklenir
                        if (!filteredList.contains(resident)) {
                            filteredList.add(resident)
                        }
                        //daha önce eklenenlerin bilgisi yeni kelimeyle eşleşmiyorsa filtrelenmiş listeden kişi çıkartılır
                        val iterator = filteredList.iterator()
                        while (iterator.hasNext()) {
                            val res = iterator.next()
                            if (!res.carPlate.contains(
                                    newText,
                                    ignoreCase = true
                                ) && !res.fullName.contains(
                                    newText,
                                    ignoreCase = true
                                ) && !res.phoneNumber.contains(newText, ignoreCase = true)
                            ) {
                                iterator.remove()
                            }
                        }

                    }
                }
                //Elde edilen son liste adapter listesine eşitlenir
                residentsInformationAdapter.list = filteredList
                return false
            }
        })
    }


    private fun onClickHandler() {
        binding.apply {
            btnMoveChatScreen.setOnClickListener {
                val action =
                    ResidentsInformationFragmentDirections.actionResidentsInformationFragmentToChatFragment()
                findNavController().navigate(action)
            }
        }
    }

    //Kullanıcıları veri tabanından çeker
    private fun getUsers() {
        databaseViewModel.getAllApartmentUsers()
    }

    //Kullanıcılar verisini gözlemler
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
                        binding.pbProgressBar.visibility = GONE
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
                authViewModel.logout()
                sharedPrefViewModel.clearSharedPref()
                val action =
                    ResidentsInformationFragmentDirections.actionResidentsInformationFragmentToLoginFragment()
                findNavController().navigate(action)
            },
            endIconClick = {
                val action =
                    ResidentsInformationFragmentDirections.actionResidentsInformationFragmentToSettingsFragmet()
                findNavController().navigate(action)
            },
        )

        (requireActivity() as MainActivity).showBottomNavigation()
    }

    //apartman sakinlerinin listeleneceği adapter ın kurulumunu yapar
    private fun setupConciergeDutyToDoAdapter() {
        binding.rwResidentsInformation.layoutManager =
            LinearLayoutManager(requireContext()) //GridLayoutManager(requireContext(), 3)
        residentsInformationAdapter = ResidentsInformationAdapter()
        binding.rwResidentsInformation.adapter = residentsInformationAdapter
        residentsInformationAdapter.moveDetailPage {
            val action =
                ResidentsInformationFragmentDirections.actionResidentsInformationFragmentToResidentInformationDetailsFragment(
                    it
                )
            findNavController().navigate(action)
        }
    }


}