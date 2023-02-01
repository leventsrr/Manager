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
import com.leventsurer.manager.data.model.ConciergeAnnouncementModel
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.databinding.FragmentExecutiveHomePageBinding
import com.leventsurer.manager.tools.adapters.ConciergeAnnouncementAdapter
import com.leventsurer.manager.tools.adapters.IncomeExpenseAdapter
import com.leventsurer.manager.tools.adapters.ResidentRequestAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExecutiveHomePageFragment : Fragment() {
    private var _binding: FragmentExecutiveHomePageBinding? = null
    private val binding: FragmentExecutiveHomePageBinding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()
    private val databaseViewModel by viewModels<DatabaseViewModel>()
    private val conciergeAnnouncementsAdapterList = ArrayList<ConciergeAnnouncementModel>()
    private lateinit var conciergeAnnouncementAdapter : ConciergeAnnouncementAdapter
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
        setupIncomeExpenseAdapter()
        setupConciergeAnnouncementAdapter()
        setupResidentRequestAdapter()
        getConciergeAnnouncement()
        observeFlow()

    }

    private fun getConciergeAnnouncement() {
        databaseViewModel.getConciergeAnnouncement()
    }

    private fun observeFlow() {
        viewLifecycleOwner.lifecycleScope.launch{
            databaseViewModel.conciergeAnnouncementFlow.collect{
                when(it){
                    is Resource.Failure ->{
                        Toast.makeText(context,it.exception.message, Toast.LENGTH_LONG).show()
                        //binding.pbProgressBar.visibility = View.GONE

                    }
                    is Resource.Loading ->{
                        //binding.pbProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        conciergeAnnouncementsAdapterList.addAll(it.result)
                        conciergeAnnouncementAdapter.list = conciergeAnnouncementsAdapterList

                    }
                    else -> {

                    }
                }
            }

        }
    }
    private fun setupResidentRequestAdapter() {
        binding.rwResidentRequest.layoutManager = LinearLayoutManager(requireContext())
        val residentRequestAdapter = ResidentRequestAdapter()
        binding.rwResidentRequest.adapter = residentRequestAdapter
    }

    private fun setupConciergeAnnouncementAdapter() {
        binding.rwConciergeAnnouncement.layoutManager = LinearLayoutManager(requireContext())
        conciergeAnnouncementAdapter = ConciergeAnnouncementAdapter()
        binding.rwConciergeAnnouncement.adapter = conciergeAnnouncementAdapter
    }

    private fun setupIncomeExpenseAdapter() {
        binding.rwIncomeExpense.layoutManager = LinearLayoutManager(requireContext())
        val incomeExpenseAdapter = IncomeExpenseAdapter()
        binding.rwIncomeExpense.adapter = incomeExpenseAdapter
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
                Log.e("kontrol", viewModel.currentUser.toString())

            },
            endIconClick = {
            },
        )


        (requireActivity() as MainActivity).showBottomNavigation()
    }

}