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
import com.leventsurer.manager.data.model.ManagerAnnouncementModel
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.databinding.FragmentLoginBinding
import com.leventsurer.manager.databinding.FragmentManagerAnnouncementBinding
import com.leventsurer.manager.tools.adapters.ManagerAnnouncementAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ManagerAnnouncementFragment : Fragment() {
    private var _binding: FragmentManagerAnnouncementBinding? = null
    private val binding: FragmentManagerAnnouncementBinding get() = _binding!!
    private val databaseViewModel by viewModels<DatabaseViewModel>()

    private val managerAnnouncementAdapterList = ArrayList<ManagerAnnouncementModel>()
    private lateinit var managerAnnouncementAdapter: ManagerAnnouncementAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManagerAnnouncementBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupManagerAnnouncementsAdapter()
        getManagerAnnouncement()
    }


    private fun setupManagerAnnouncementsAdapter() {
        binding.rwManagerAnnouncements.layoutManager = LinearLayoutManager(requireContext())
        managerAnnouncementAdapter = ManagerAnnouncementAdapter()
        binding.rwManagerAnnouncements.adapter = managerAnnouncementAdapter
    }

    private fun getManagerAnnouncement() {
        databaseViewModel.getManagerAnnouncement()
        observeManagerAnnouncementFlow()
    }

    private fun observeManagerAnnouncementFlow() {
        viewLifecycleOwner.lifecycleScope.launch {

            databaseViewModel.managerAnnouncementFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                        binding.pbProgressBar.visibility = View.GONE

                    }
                    is Resource.Loading -> {
                        binding.pbProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.pbProgressBar.visibility = View.GONE

                        managerAnnouncementAdapterList.addAll(it.result)
                        managerAnnouncementAdapter.list = managerAnnouncementAdapterList

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
            title = "Yönetici Duyuruları",
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