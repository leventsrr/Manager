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
            title = "Kap??c??",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_sensor_door_24,
            endIcon = R.drawable.ic_baseline_settings_24,
            startIconClick = {
                findNavController().popBackStack()
            },
            endIconClick = {
                val action = ConciergeFragmentDirections.actionConciergeFragmentToSettingsFragmet()
                findNavController().navigate(action)
            },
        )
    }
    //Veri taban??ndan kap??c?? g??revlerinin getirilmesi sa??lan??r
    private fun getConciergeDuties(){
        databaseViewModel.getConciergeDuties()
        observeFinancialEventFlow()
    }
    //Kap??c?? g??revleri i??in at??lan iste??e kar????l??k gelen cevab??n incelenmesini ve gelen verilerin
    // adapter listesine aktar??lmas?? sa??lan??r
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
                        binding.pbDutiesDone.visibility = View.GONE
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

    //Kap??c??n??n yap??lacaklar listesi i??in olan adapter??n kurulumu sa??lan??r
    private fun setupConciergeDutyToDoAdapter() {
        binding.rwDutyToDo.layoutManager = LinearLayoutManager(requireContext())
        conciergeDutyDoAdapter = ConciergeDutyToDoAdapterAdapter()
        binding.rwDutyToDo.adapter = conciergeDutyDoAdapter
    }

    //Kap??c??n??n yap??lan listesi i??in olan adapter??n kurulumu sa??lan??r
    private fun setupConciergeDutyToDoneAdapter() {
        binding.rwDutyToDone.layoutManager = LinearLayoutManager(requireContext())
        conciergeDutyDoneAdapter = ConciergeDutyToDoneAdapter()
        binding.rwDutyToDone.adapter = conciergeDutyDoneAdapter
    }





}