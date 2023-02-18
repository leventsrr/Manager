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
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.ConciergeDutiesModel
import com.leventsurer.manager.data.model.FinancialEventModel
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.databinding.FragmentConciergeBinding
import com.leventsurer.manager.tools.adapters.ConciergeAnnouncementAdapter
import com.leventsurer.manager.tools.adapters.ConciergeDutyToDoAdapterAdapter
import com.leventsurer.manager.tools.adapters.ConciergeDutyToDoneAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class ConciergeFragment : Fragment() {

    private var _binding: FragmentConciergeBinding? = null
    private val binding: FragmentConciergeBinding get() = _binding!!
    private val databaseViewModel by viewModels<DatabaseViewModel>()

    private val conciergeDutyDoneAdapterList = ArrayList<ConciergeDutiesModel>()
    private val conciergeDutyDoAdapterList = ArrayList<ConciergeDutiesModel>()
    //Adapters
    private lateinit var conciergeDutyDoneAdapter : ConciergeDutyToDoneAdapter
    private lateinit var conciergeDutyDoAdapter : ConciergeDutyToDoAdapterAdapter
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
        getConciergeDuties()
    }


    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "Kapıcı",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_sensor_door_24,
            endIcon = R.drawable.ic_baseline_settings_24,
            startIconClick = {
                findNavController().popBackStack()
            },
            endIconClick = {
                val action = ConciergeFragmentDirections.actionConciergeFragmentToUserProfileFragment2()
                findNavController().navigate(action)
            },
        )
    }

    private fun getConciergeDuties(){
        databaseViewModel.getConciergeDuties()
        observeFinancialEventFlow()
    }
    private fun observeFinancialEventFlow() {
        viewLifecycleOwner.lifecycleScope.launch{

            databaseViewModel.conciergeDutiesFlow.collect{
                when(it){
                    is Resource.Failure ->{
                        Toast.makeText(context,it.exception.message, Toast.LENGTH_LONG).show()
                        binding.pbDutiesDo.visibility = View.GONE
                        binding.pbDutiesDone.visibility = View.GONE

                    }
                    is Resource.Loading ->{
                        binding.pbDutiesDo.visibility = View.VISIBLE
                        binding.pbDutiesDone.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.pbDutiesDo.visibility = View.GONE
                        binding.pbDutiesDone.visibility = View.VISIBLE
                        for(duty in it.result){
                            if(duty.isDone) conciergeDutyDoneAdapterList.add(duty) else conciergeDutyDoAdapterList.add(duty)
                        }
                        conciergeDutyDoneAdapter.list = conciergeDutyDoneAdapterList
                        conciergeDutyDoAdapter.list = conciergeDutyDoAdapterList


                    }
                    else -> {

                    }
                }

            }

        }
    }

    private fun setupConciergeDutyToDoAdapter() {
        binding.rwDutyToDo.layoutManager = LinearLayoutManager(requireContext())
        conciergeDutyDoAdapter = ConciergeDutyToDoAdapterAdapter()
        binding.rwDutyToDo.adapter = conciergeDutyDoAdapter
    }

    private fun setupConciergeDutyToDoneAdapter() {
        binding.rwDutyToDone.layoutManager = LinearLayoutManager(requireContext())
        conciergeDutyDoneAdapter = ConciergeDutyToDoneAdapter()
        binding.rwDutyToDone.adapter = conciergeDutyDoneAdapter
    }





}