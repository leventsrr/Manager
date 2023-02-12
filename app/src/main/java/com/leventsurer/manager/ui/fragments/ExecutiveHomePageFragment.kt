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
import com.leventsurer.manager.data.model.*
import com.leventsurer.manager.databinding.FragmentExecutiveHomePageBinding
import com.leventsurer.manager.tools.adapters.ConciergeAnnouncementAdapter
import com.leventsurer.manager.tools.adapters.IncomeExpenseAdapter
import com.leventsurer.manager.tools.adapters.ResidentRequestAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DataStoreViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExecutiveHomePageFragment : Fragment() {
    private var _binding: FragmentExecutiveHomePageBinding? = null
    private val binding: FragmentExecutiveHomePageBinding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()
    private val databaseViewModel by viewModels<DatabaseViewModel>()
    private val dataStoreViewModel by viewModels<DataStoreViewModel>()

    private val conciergeAnnouncementsAdapterList = ArrayList<ConciergeAnnouncementModel>()
    private val residentRequestAdapterList = ArrayList<ResidentsRequestModel>()
    private val financialEventAdapterList = ArrayList<FinancialEventModel>()
    //Adapters
    private lateinit var conciergeAnnouncementAdapter : ConciergeAnnouncementAdapter
    private lateinit var residentRequestAdapter: ResidentRequestAdapter
    private lateinit var financialEventAdapter: IncomeExpenseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExecutiveHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()

        setupConciergeAnnouncementAdapter()
        setupResidentRequestAdapter()
        getConciergeAnnouncement()
        getResidentRequests()
        getFinancialEvents()

        setupIncomeExpenseAdapter()
        readDataStore()
    }

    private fun getConciergeAnnouncement() {
        databaseViewModel.getConciergeAnnouncement()
        observeAnnouncementFlow()
    }

    private fun observeAnnouncementFlow() {
        viewLifecycleOwner.lifecycleScope.launch{

            databaseViewModel.conciergeAnnouncementFlow.collect{
                when(it){
                    is Resource.Failure ->{
                        Toast.makeText(context,it.exception.message,Toast.LENGTH_LONG).show()
                        binding.pbConciergeAnnouncement.visibility = View.GONE

                    }
                    is Resource.Loading ->{
                        binding.pbConciergeAnnouncement.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.pbConciergeAnnouncement.visibility = View.GONE

                        conciergeAnnouncementsAdapterList.addAll(it.result)
                        conciergeAnnouncementAdapter.list = conciergeAnnouncementsAdapterList

                    }
                    else -> {

                    }
                }

            }

        }
    }

    private fun getResidentRequests(){
        databaseViewModel.getResidentRequests()
        observeResidentRequestFlow()
    }

    private fun observeResidentRequestFlow() {
        viewLifecycleOwner.lifecycleScope.launch{

            databaseViewModel.residentRequestFlow.collect{
                when(it){
                    is Resource.Failure ->{
                        Toast.makeText(context,it.exception.message,Toast.LENGTH_LONG).show()
                        binding.pbResidentRequests.visibility = View.GONE

                    }
                    is Resource.Loading ->{
                        binding.pbResidentRequests.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.pbResidentRequests.visibility = View.GONE

                        residentRequestAdapterList.addAll(it.result)
                        residentRequestAdapter.list = residentRequestAdapterList

                    }
                    else -> {

                    }
                }

            }

        }
    }

    private fun getFinancialEvents(){
        databaseViewModel.getFinancialEvents()
        observeFinancialEventFlow()
    }

    private fun observeFinancialEventFlow() {
        viewLifecycleOwner.lifecycleScope.launch{

            databaseViewModel.financialEventsFlow.collect{
                when(it){
                    is Resource.Failure ->{
                        Toast.makeText(context,it.exception.message,Toast.LENGTH_LONG).show()
                        binding.pbFinancialEvents.visibility = View.GONE

                    }
                    is Resource.Loading ->{
                        binding.pbFinancialEvents.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.pbFinancialEvents.visibility = View.GONE

                        financialEventAdapterList.addAll(it.result)
                        financialEventAdapter.list = financialEventAdapterList

                    }
                    else -> {

                    }
                }

            }

        }
    }

    private fun setupResidentRequestAdapter() {
        binding.rwResidentRequest.layoutManager = LinearLayoutManager(requireContext())
        residentRequestAdapter = ResidentRequestAdapter()
        binding.rwResidentRequest.adapter = residentRequestAdapter
    }

    private fun setupConciergeAnnouncementAdapter() {
        binding.rwConciergeAnnouncement.layoutManager = LinearLayoutManager(requireContext())
        conciergeAnnouncementAdapter = ConciergeAnnouncementAdapter()
        binding.rwConciergeAnnouncement.adapter = conciergeAnnouncementAdapter
    }

    private fun setupIncomeExpenseAdapter() {
        binding.rwIncomeExpense.layoutManager = LinearLayoutManager(requireContext())
        financialEventAdapter = IncomeExpenseAdapter()
        binding.rwIncomeExpense.adapter = financialEventAdapter
    }

    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "Anasayfa",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_sensor_door_24,
            endIcon = R.drawable.ic_baseline_account_circle_24,
            startIconClick = {

                viewModel.logout()
                findNavController().popBackStack()

            },
            endIconClick = {
            },
        )


        (requireActivity() as MainActivity).showBottomNavigation()
    }

    private  fun readDataStore(){
        dataStoreViewModel.getUserName()
        val userName = dataStoreViewModel.userNameFlow.value
        dataStoreViewModel.getApartmentCode()
        val apartmentCode = dataStoreViewModel.apartmentCodeFlow.value
        dataStoreViewModel.getIsLogin()
        val isLogin = dataStoreViewModel.isLoginFlow.value


            Log.e("kontrol","userName:$userName, apartmentCode:$apartmentCode, isLogin:$isLogin")
    }

}