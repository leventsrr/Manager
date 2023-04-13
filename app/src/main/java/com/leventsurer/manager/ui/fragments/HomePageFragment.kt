package com.leventsurer.manager.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.leventsurer.manager.MainActivity
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.*
import com.leventsurer.manager.databinding.FragmentHomePageBinding
import com.leventsurer.manager.tools.adapters.homePageAdapter.HomeRecyclerViewAdapter
import com.leventsurer.manager.tools.adapters.homePageAdapter.HomeRecyclerViewItem
import com.leventsurer.manager.tools.helpers.ChipCardHelper
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class HomePageFragment : Fragment() {
    private var _binding: FragmentHomePageBinding? = null
    private val binding: FragmentHomePageBinding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()
    private val databaseViewModel by viewModels<DatabaseViewModel>()
    private var chosenCardNumber: Int = 0
    private val adapterList = ArrayList<HomeRecyclerViewItem>()
    private var residentsList = ArrayList<UserModel>()
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
        changeChipsColor()
        observeResidents()
        observeApartmentInfo()
        onClickListener()
    }

    private fun onClickListener() {
        binding.apply {
            btnHome.setOnClickListener {
                recyclerView.visibility = GONE
                homePageGreetPart.visibility = VISIBLE
                chosenCardNumber = 0
                changeChipsColor()
            }
        }
    }

    //Adapter kurulumu yapar
    private fun setupHomePageAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        homePageAdapter = HomeRecyclerViewAdapter()
        binding.recyclerView.adapter = homePageAdapter
        homePageAdapter.sendPollAnswer{ text,isAgree->
            runBlocking {
                val report = databaseViewModel.changePollStatistics(isAgree,text)
                Toast.makeText(requireContext(),report,Toast.LENGTH_LONG).show()
            }
        }
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
            4 -> {
                runBlocking {
                    databaseViewModel.getPolls()
                    observePollData()
                }
            }
        }
    }
    //Apartmana ait anketleri dinler
    private suspend fun observePollData() {
        databaseViewModel.getPolls().observe(viewLifecycleOwner) {
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
                    binding.homePageGreetPart.visibility = GONE
                    binding.pbProgressBar.visibility = GONE
                    binding.recyclerView.visibility = VISIBLE

                    adapterList.clear()
                    for (poll in it.result) {
                        Log.e("kontrol","${poll.pollText} / ${poll.agreeCount} / ${poll.disagreeCount}")
                        val listItem =
                            HomeRecyclerViewItem.Polls(
                                pollText = poll.pollText,
                                agreeCount = poll.agreeCount,
                                disagreeCount = poll.disagreeCount
                            )

                        Log.e("kontrol","ana sayfa ${listItem.agreeCount} / ${listItem.disagreeCount} / ${listItem.pollText}")
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
                        binding.homePageGreetPart.visibility = GONE
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
                        binding.homePageGreetPart.visibility = GONE
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
                        binding.homePageGreetPart.visibility = GONE
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

    //apartmanda oturan kişileri çeker
    private fun observeResidents(){
        databaseViewModel.getAllApartmentUsers()
        viewLifecycleOwner.lifecycleScope.launch {
            databaseViewModel.users.collect {
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
                        residentsList = it.result
                        for(resident:UserModel in it.result){
                            if (resident.role == "yonetici"){
                                binding.twManagerName.text = resident.fullName
                                binding.twManagerPhone.text = resident.phoneNumber
                            }else if(resident.role == "kapici"){
                                binding.twConciergeName.text = resident.fullName
                                binding.twConciergePhone.text = resident.phoneNumber
                            }
                        }
                        binding.twResidentCount.text = residentsList.size.toString()

                        Log.e("kontrol", adapterList.toString())

                    }
                    else -> {

                    }
                }
            }

        }
    }

    private fun observeApartmentInfo(){
        databaseViewModel.getApartmentInfo()
            databaseViewModel.apartmentLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, "it.exception.message", Toast.LENGTH_LONG).show()
                        binding.pbProgressBar.visibility = GONE

                    }
                    is Resource.Loading -> {
                        binding.recyclerView.visibility = GONE
                        binding.pbProgressBar.visibility = VISIBLE
                    }
                    is Resource.Success -> {
                        binding.pbProgressBar.visibility = GONE
                        binding.apply {
                            twApartmentAddress.text = it.result.address
                            twApartmentMonthlyPayment.text = it.result.monthlyPayment.toString()
                            twApartmentName.text = it.result.apartmentName

                        }

                        Log.e("kontrol", adapterList.toString())

                    }
                    else -> {

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
                changeChipsColor()
            }
        )

        ChipCardHelper.customChip(
            binding.incCard2,
            text = "Kapıcı Duyuruları",
            icon = R.drawable.ic_baseline_announcement_24,
            chipOnClickListener = {
                chosenCardNumber = 2
                getHomePageInfo(chosenCardNumber)
                changeChipsColor()
            }
        )

        ChipCardHelper.customChip(
            binding.incCard3,
            text = "Sakin İstekleri",
            icon = R.drawable.ic_baseline_resident_request_24,
            chipOnClickListener = {
                chosenCardNumber = 3
                getHomePageInfo(chosenCardNumber)
                changeChipsColor()
            }
        )

        ChipCardHelper.customChip(
            binding.incCard4,
            text = "Anketler",
            icon = R.drawable.ic_baseline_poll_24,
            chipOnClickListener = {
                chosenCardNumber = 4
                getHomePageInfo(chosenCardNumber)
                changeChipsColor()
            }
        )


        (requireActivity() as MainActivity).showBottomNavigation()
    }

    @SuppressLint("ResourceAsColor")
    private fun changeChipsColor() {
        val chipsList =
            listOf(binding.incCard1, binding.incCard2, binding.incCard3, binding.incCard4)
        for (i in 1..chipsList.size) {

            if (chosenCardNumber == i) {
                chipsList[i - 1].cwMyChip.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.thirdColor
                    )
                )

                chipsList[i - 1].twCardText.setTextColor(Color.parseColor("#FFFFFF"))
                chipsList[i - 1].iwCardImage.visibility = GONE

            } else {
                chipsList[i - 1].cwMyChip.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                chipsList[i - 1].twCardText.setTextColor(R.color.black)
                chipsList[i - 1].iwCardImage.visibility = VISIBLE
            }
        }
    }

}