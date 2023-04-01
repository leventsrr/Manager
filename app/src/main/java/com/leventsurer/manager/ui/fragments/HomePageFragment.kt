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
    private val databaseViewModel by viewModels<DatabaseViewModel>()
    private val sharedPrefViewModel by viewModels<SharedPreferencesViewModel>()

    private val conciergeAnnouncementsAdapterList = ArrayList<ConciergeAnnouncementModel>()
    private val residentRequestAdapterList = ArrayList<ResidentsRequestModel>()
    private val managerAnnouncementAdapterList = ArrayList<ManagerAnnouncementModel>()

    //Adapters
    private lateinit var conciergeAnnouncementAdapter: ConciergeAnnouncementAdapter
    private lateinit var residentRequestAdapter: ResidentRequestAdapter
    private lateinit var managerAnnouncementAdapter: ManagerAnnouncementAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()

       // setupConciergeAnnouncementAdapter()
        //setupResidentRequestAdapter()
        getConciergeAnnouncement()
        getResidentRequests()
        getManagerAnnouncement()

       // setupIncomeExpenseAdapter()
        readSharedPref()
    }

    private fun readSharedPref() {
        val isLogin = sharedPrefViewModel.readIsLogin()
        val apartmentName = sharedPrefViewModel.readApartmentName()
        val userName = sharedPrefViewModel.readUserName()
        val userDocumentId = sharedPrefViewModel.readUserDocumentId()
    }

    //Veri tabanından kapıcı duyurularının listesinin çekilmesi sağlanır
    private fun getConciergeAnnouncement() {
        databaseViewModel.getConciergeAnnouncement()
        observeAnnouncementFlow()
    }
    //Kapıcı duyuruları için atılan isteğe karşılık gelen cevabı inceler ve gelen verileri ilgili adapter listesine aktarır
    private fun observeAnnouncementFlow() {
        viewLifecycleOwner.lifecycleScope.launch {

            databaseViewModel.conciergeAnnouncementFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                        //binding.pbConciergeAnnouncement.visibility = View.GONE

                    }
                    is Resource.Loading -> {
                       // binding.pbConciergeAnnouncement.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        //binding.pbConciergeAnnouncement.visibility = View.GONE

                        conciergeAnnouncementsAdapterList.addAll(it.result)
                        conciergeAnnouncementAdapter.list = conciergeAnnouncementsAdapterList

                    }
                    else -> {

                    }
                }

            }

        }
    }

    //Veri tabanından apartman sakinlerinin isteklerini çeker
    private fun getResidentRequests() {
        databaseViewModel.getResidentRequests()
        observeResidentRequestFlow()
    }

    //sakin istekleri için atılan isteğe karşılık gelen cevabı inceler ve gelen verileri ilgili adapter listesine aktarır
    private fun observeResidentRequestFlow() {
        viewLifecycleOwner.lifecycleScope.launch {

            databaseViewModel.residentRequestFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                        //binding.pbResidentRequests.visibility = View.GONE

                    }
                    is Resource.Loading -> {
                        //binding.pbResidentRequests.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        //binding.pbResidentRequests.visibility = View.GONE

                        residentRequestAdapterList.addAll(it.result)
                        residentRequestAdapter.list = residentRequestAdapterList

                    }
                    else -> {

                    }
                }

            }

        }
    }

    //Gerçekleşen ekonomik harcama ve birikim olayları için istek atar
    private fun getManagerAnnouncement() {
        databaseViewModel.getManagerAnnouncement()
        observeManagerAnnouncementFlow()
    }
    //Ekonomik olaylar için atılan isteğe karşılık gelen cevabı inceler ve gelen verileri ilgili adapter listesine aktarır
    private fun observeManagerAnnouncementFlow() {
        viewLifecycleOwner.lifecycleScope.launch {

            databaseViewModel.managerAnnouncementFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                        //binding.pbFinancialEvents.visibility = View.GONE

                    }
                    is Resource.Loading -> {
                       // binding.pbFinancialEvents.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        //binding.pbFinancialEvents.visibility = View.GONE

                        managerAnnouncementAdapterList.addAll(it.result)
                        managerAnnouncementAdapter.list = managerAnnouncementAdapterList

                    }
                    else -> {

                    }
                }

            }

        }
    }

    //Sakin isteklerinin listeleneceği adapter ın kurulumunu yapar
    /*private fun setupResidentRequestAdapter() {
        binding.rwResidentRequest.layoutManager = LinearLayoutManager(requireContext())
        residentRequestAdapter = ResidentRequestAdapter()
        binding.rwResidentRequest.adapter = residentRequestAdapter
    }
    //Kapıcı duyurularının listeleneceği adapter ın kurulumunu yapar
    private fun setupConciergeAnnouncementAdapter() {
        binding.rwConciergeAnnouncement.layoutManager = LinearLayoutManager(requireContext())
        conciergeAnnouncementAdapter = ConciergeAnnouncementAdapter()
        binding.rwConciergeAnnouncement.adapter = conciergeAnnouncementAdapter
    }
    //Gelir gider verilerinin listeleneceği adapter ın kurulumunu yapar
    private fun setupIncomeExpenseAdapter() {
        binding.rwIncomeExpense.layoutManager = LinearLayoutManager(requireContext())
        managerAnnouncementAdapter = ManagerAnnouncementAdapter()
        binding.rwIncomeExpense.adapter = managerAnnouncementAdapter
    }*/

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