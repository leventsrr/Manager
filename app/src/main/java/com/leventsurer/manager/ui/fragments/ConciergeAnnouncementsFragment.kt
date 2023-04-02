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
import com.leventsurer.manager.data.model.ConciergeAnnouncementModel
import com.leventsurer.manager.data.model.ManagerAnnouncementModel
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.databinding.FragmentConciergeAnnouncementsBinding
import com.leventsurer.manager.databinding.FragmentLoginBinding
import com.leventsurer.manager.databinding.FragmentManagerAnnouncementBinding
import com.leventsurer.manager.tools.adapters.ConciergeAnnouncementAdapter
import com.leventsurer.manager.tools.adapters.ManagerAnnouncementAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConciergeAnnouncementsFragment : Fragment() {
    private var _binding: FragmentConciergeAnnouncementsBinding? = null
    private val binding: FragmentConciergeAnnouncementsBinding get() = _binding!!
    private val databaseViewModel by viewModels<DatabaseViewModel>()

    private val conciergeAnnouncementAdapterList = ArrayList<ConciergeAnnouncementModel>()
    private lateinit var conciergeAnnouncementAdapter: ConciergeAnnouncementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConciergeAnnouncementsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupManagerAnnouncementsAdapter()
        getConciergeAnnouncement()
    }


    private fun setupManagerAnnouncementsAdapter() {
        binding.rwConciergeAnnouncements.layoutManager = LinearLayoutManager(requireContext())
        conciergeAnnouncementAdapter = ConciergeAnnouncementAdapter()
        binding.rwConciergeAnnouncements.adapter = conciergeAnnouncementAdapter
    }

    private fun getConciergeAnnouncement() {
        databaseViewModel.getConciergeAnnouncement()
        observeConciergeAnnouncementFlow()
    }

    private fun observeConciergeAnnouncementFlow() {
        viewLifecycleOwner.lifecycleScope.launch {

            databaseViewModel.conciergeAnnouncementFlow.collect {
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

                        conciergeAnnouncementAdapterList.addAll(it.result)
                        conciergeAnnouncementAdapter.list = conciergeAnnouncementAdapterList

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
            title = "Kapıcı Duyuruları",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_arrow_back_24,
            endIcon = R.drawable.ic_baseline_settings_24,
            startIconClick = {
                findNavController().popBackStack()

            },
            endIconClick = {
            },
        )


        (requireActivity() as MainActivity).hideBottomNavigation()
    }


}