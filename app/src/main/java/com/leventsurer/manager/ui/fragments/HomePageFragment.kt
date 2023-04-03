package com.leventsurer.manager.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import com.leventsurer.manager.tools.adapters.homePageAdapter.HomeRecyclerViewAdapter
import com.leventsurer.manager.tools.adapters.homePageAdapter.HomeRecyclerViewItem
import com.leventsurer.manager.tools.helpers.ChipCardHelper
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
    private val databaseViewModel by viewModels<DatabaseViewModel>()
    private var chosenCardNumber: Int = 1
    private val adapterList = ArrayList<HomeRecyclerViewItem>()
    private lateinit var homePageAdapter: HomeRecyclerViewAdapter
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
        setupHomePageAdapter()
        getHomePageInfo(chosenCardNumber)
    }

    //Adapter kurulumu yapar
    private fun setupHomePageAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        homePageAdapter = HomeRecyclerViewAdapter()
        binding.recyclerView.adapter = homePageAdapter
    }

    //Seçilen karta göre bilgileri veritababından çağırır
    private fun getHomePageInfo(chosenCardNumber: Int) {
        when (chosenCardNumber) {
            1 -> {
                databaseViewModel.getManagerAnnouncement()
                observeManagerAnnouncementFlow()

            }
            2 -> {
                databaseViewModel.getConciergeAnnouncement()
                observeConciergeAnnouncementFlow()
            }
            3 -> {
                databaseViewModel.getResidentRequests()
                observeResidentsRequestsFlow()
            }
            4 -> {}
        }
    }

    //Yönetici duyurularının dinler
    private fun observeManagerAnnouncementFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            databaseViewModel.managerAnnouncementFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                        binding.pbProgressBar.visibility = GONE

                    }
                    is Resource.Loading -> {
                        binding.recyclerView.visibility = GONE
                        binding.pbProgressBar.visibility = VISIBLE
                    }
                    is Resource.Success -> {
                        binding.pbProgressBar.visibility = GONE
                        binding.recyclerView.visibility = VISIBLE
                        adapterList.clear()
                        for (announcement in it.result) {
                            val listItem =
                                HomeRecyclerViewItem.ManagerAnnouncement(announcement.announcement)
                            adapterList.add(listItem)
                            homePageAdapter.items = adapterList
                        }
                        Log.e("kontrol", adapterList.toString())

                    }
                    else -> {

                    }
                }
            }

        }
    }

    //Kapıcı duyurularını dinler
    private fun observeConciergeAnnouncementFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            databaseViewModel.conciergeAnnouncementFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                        binding.pbProgressBar.visibility = GONE

                    }
                    is Resource.Loading -> {
                        binding.recyclerView.visibility = GONE
                        binding.pbProgressBar.visibility = VISIBLE
                    }
                    is Resource.Success -> {
                        binding.pbProgressBar.visibility = GONE
                        binding.recyclerView.visibility = VISIBLE
                        adapterList.clear()
                        for (announcement in it.result) {
                            val listItem =
                                HomeRecyclerViewItem.ConciergeAnnouncement(announcement.announcement)
                            adapterList.add(listItem)
                            homePageAdapter.items = adapterList
                        }
                        Log.e("kontrol", adapterList.toString())

                    }
                    else -> {

                    }
                }
            }

        }
    }

    //Apartman sakinlerinin isteklerini dinler
    private fun observeResidentsRequestsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            databaseViewModel.residentRequestFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                        binding.pbProgressBar.visibility = GONE

                    }
                    is Resource.Loading -> {
                        binding.recyclerView.visibility = GONE
                        binding.pbProgressBar.visibility = VISIBLE
                    }
                    is Resource.Success -> {
                        binding.pbProgressBar.visibility = GONE
                        binding.recyclerView.visibility = VISIBLE
                        adapterList.clear()
                        for (request in it.result) {
                            val listItem = HomeRecyclerViewItem.ResidentRequest(request.request)
                            adapterList.add(listItem)
                            homePageAdapter.items = adapterList
                        }
                        Log.e("kontrol", adapterList.toString())

                    }
                    else -> {

                    }
                }
            }

        }
    }

    //Fragment açılırken gerekli arayüz bağlantılarını kurar
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

        ChipCardHelper.customChip(
            binding.incCard1,
            text = "Yönetici Duyuruları",
            icon = R.drawable.ic_baseline_manager_announcement_24,
            chipOnClickListener = {
                chosenCardNumber = 1
                getHomePageInfo(chosenCardNumber)
            }
        )

        ChipCardHelper.customChip(
            binding.incCard2,
            text = "Kapıcı Duyuruları",
            icon = R.drawable.ic_baseline_announcement_24,
            chipOnClickListener = {
                chosenCardNumber = 2
                getHomePageInfo(chosenCardNumber)
            }
        )

        ChipCardHelper.customChip(
            binding.incCard3,
            text = "Sakin İstekleri",
            icon = R.drawable.ic_baseline_resident_request_24,
            chipOnClickListener = {
                chosenCardNumber = 3
                getHomePageInfo(chosenCardNumber)
            }
        )

        ChipCardHelper.customChip(
            binding.incCard4,
            text = "Anketler",
            icon = R.drawable.ic_baseline_poll_24,
            chipOnClickListener = {
                chosenCardNumber = 4
            }
        )


        (requireActivity() as MainActivity).showBottomNavigation()
    }


}