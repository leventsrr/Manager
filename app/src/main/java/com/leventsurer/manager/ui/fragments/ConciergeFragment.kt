package com.leventsurer.manager.ui.fragments

import android.annotation.SuppressLint
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
import com.leventsurer.manager.data.model.ConciergeDutiesModel
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.databinding.FragmentConciergeBinding
import com.leventsurer.manager.tools.adapters.ConciergeDutyToDoAdapterAdapter
import com.leventsurer.manager.tools.adapters.ConciergeDutyToDoneAdapter
import com.leventsurer.manager.tools.adapters.homePageAdapter.HomeRecyclerViewItem
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import com.leventsurer.manager.viewModels.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class ConciergeFragment : Fragment() {

    private var _binding: FragmentConciergeBinding? = null
    private val binding: FragmentConciergeBinding get() = _binding!!
    private val databaseViewModel by viewModels<DatabaseViewModel>()
    private val sharedPrefViewModel by viewModels<SharedPreferencesViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    private lateinit var userRole:String
    //lists
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
    ): View {
        _binding = FragmentConciergeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        getUserInfo()

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
                authViewModel.logout()
                sharedPrefViewModel.clearSharedPref()
                val action = ConciergeFragmentDirections.actionConciergeFragmentToLoginFragment()
                findNavController().navigate(action)
            },
            endIconClick = {
                val action = ConciergeFragmentDirections.actionConciergeFragmentToSettingsFragmet()
                findNavController().navigate(action)
            },
        )
        (requireActivity() as MainActivity).showBottomNavigation()
    }
    //Veri tabanından kapıcı görevlerinin getirilmesi sağlanır
    private fun getConciergeDuties(){
        databaseViewModel.getConciergeDuties()
        observeConciergeDutyFlow()
    }
    //Kapıcı görevleri için atılan isteğe karşılık gelen cevabın incelenmesini ve gelen verilerin
    // adapter listesine aktarılması sağlanır
    private fun observeConciergeDutyFlow() {
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
                    else -> {}
                }

            }

        }
    }

    //Kapıcının yapılacaklar listesi için olan adapterın kurulumu sağlanır
    private fun setupConciergeDutyToDoAdapter() {
        binding.rwDutyToDo.layoutManager = LinearLayoutManager(requireContext())
        conciergeDutyDoAdapter = ConciergeDutyToDoAdapterAdapter(userRole)
        binding.rwDutyToDo.adapter = conciergeDutyDoAdapter
        conciergeDutyDoAdapter.markDoneDuty {
            databaseViewModel.changeConciergeDutyStatus(it.duty)
            changeDutyListTypePosition(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun changeDutyListTypePosition(duty:ConciergeDutiesModel) {
        conciergeDutyDoAdapter.list.remove(duty)
        conciergeDutyDoAdapter.notifyDataSetChanged()
        conciergeDutyDoneAdapter.list.add(duty)
        conciergeDutyDoneAdapter.notifyDataSetChanged()
    }

    //Kapıcının yapılan listesi için olan adapterın kurulumu sağlanır
    private fun setupConciergeDutyToDoneAdapter() {
        binding.rwDutyToDone.layoutManager = LinearLayoutManager(requireContext())
        conciergeDutyDoneAdapter = ConciergeDutyToDoneAdapter()
        binding.rwDutyToDone.adapter = conciergeDutyDoneAdapter
    }

    private fun getUserInfo(){
        databaseViewModel.getUserInfo()
        observeUserInfo()
    }

    private fun observeUserInfo() {
            databaseViewModel.userInfoFlow.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        userRole = it.result.role
                        setupConciergeDutyToDoAdapter()
                        setupConciergeDutyToDoneAdapter()
                        getConciergeDuties()
                    }
                    else -> {}
                }
            }

    }


}