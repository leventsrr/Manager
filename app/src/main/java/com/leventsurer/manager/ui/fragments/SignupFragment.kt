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
import com.leventsurer.manager.MainActivity
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.databinding.FragmentSignupBinding
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding: FragmentSignupBinding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    private val databaseViewModel by viewModels<DatabaseViewModel>()
    private lateinit var newUserRole:String
    private var isRoleSelected:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickHandler()
        (requireActivity() as MainActivity).hideBottomNavigation()
    }

    private fun observeSignUpFlow() {
        viewLifecycleOwner.lifecycleScope.launch{
            authViewModel.signupFlow.collect{
                when(it){
                    is Resource.Failure ->{
                        Toast.makeText(context,it.exception.message,Toast.LENGTH_LONG).show()
                        binding.pbProgressBar.visibility = View.GONE

                    }
                    is Resource.Loading ->{
                        binding.pbProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        if(findNavController().currentDestination?.id == R.id.signupFragment){

                            val action = SignupFragmentDirections.actionSignupFragmentToExecutiveHomePage()
                            findNavController().navigate(action)
                            binding.pbProgressBar.visibility = View.GONE

                        }

                    }
                    else -> {

                    }
                }
            }

        }
    }
    private fun signUp(name:String,email:String,password:String,apartmentCode:String,role:String,doorNumber:String,carPlate:String){
        authViewModel.signup(name,email,password)
        if(newUserRole == "yonetici"){
            databaseViewModel.addNewApartment(name,apartmentCode,carPlate,doorNumber,role,apartmentCode)
        }else{
            databaseViewModel.addNewUser(name,apartmentCode,carPlate,doorNumber,role)
        }

        observeSignUpFlow()
    }
    private fun onClickHandler() {
        binding.apply {
            buttonNavigateLogin.setOnClickListener {
                findNavController().popBackStack()
            }

            buttonSignup.setOnClickListener {
                val userName = etNewUserName.text.toString()
                val userEmail = etNewUserMail.text.toString()
                val userPassword = etNewUserPassword.text.toString()
                val userCarPlate = etCarPlate.text.toString()
                val userDoorNumber = etDoorNumber.text.toString()
                val apartmentCode = etApartmentCode.text.toString()

                if(isRoleSelected){
                    signUp(userName,userEmail,userPassword,apartmentCode,newUserRole,userDoorNumber, userCarPlate)
                }else{
                    Toast.makeText(context,"Rol Seçimi Yapmak Zorunludur.",Toast.LENGTH_LONG).show()
                }

            }
            toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
                if(isChecked){
                    when(checkedId){
                        R.id.button1 -> {
                            newUserRole = "yonetici"
                            isRoleSelected= true

                        }
                        R.id.button2 -> {
                            newUserRole = "sakin"
                            isRoleSelected = true
                        }
                        R.id.button3 -> {
                            newUserRole = "kapici"
                            isRoleSelected = true
                        }
                    }
                }else{
                    isRoleSelected = false
                }
            }
        }
    }




}