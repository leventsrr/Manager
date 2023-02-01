package com.leventsurer.manager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.leventsurer.manager.R
import com.leventsurer.manager.databinding.FragmentConciergeBinding
import com.leventsurer.manager.tools.adapters.ConciergeDutyToDoAdapterAdapter
import com.leventsurer.manager.tools.adapters.ConciergeDutyToDoneAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper

class ConciergeFragment : Fragment() {

    private var _binding: FragmentConciergeBinding? = null
    private val binding: FragmentConciergeBinding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConciergeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupConciergeDutyToDoAdapter()
        setupConciergeDutyToDoneAdapter()
    }


    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "Kapıcı",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_sensor_door_24,
            endIcon = R.drawable.ic_baseline_account_circle_24,
            startIconClick = {
                findNavController().popBackStack()
            },
            endIconClick = {
                //test2()
            },
        )
    }


    private fun setupConciergeDutyToDoAdapter() {
        binding.rwDutyToDo.layoutManager = LinearLayoutManager(requireContext())
        val conciergeDutyToDoAdapter = ConciergeDutyToDoAdapterAdapter()
        binding.rwDutyToDo.adapter = conciergeDutyToDoAdapter
    }

    private fun setupConciergeDutyToDoneAdapter() {
        binding.rwDutyToDone.layoutManager = LinearLayoutManager(requireContext())
        val conciergeDutyToDoAdapterAdapter = ConciergeDutyToDoneAdapter()
        binding.rwDutyToDone.adapter = conciergeDutyToDoAdapterAdapter
    }





}