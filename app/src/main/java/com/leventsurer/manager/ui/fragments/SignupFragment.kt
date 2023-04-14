package com.leventsurer.manager.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.leventsurer.manager.MainActivity
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.ApartmentModel
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.databinding.FragmentSignupBinding
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import com.leventsurer.manager.viewModels.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding: FragmentSignupBinding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    private val databaseViewModel by viewModels<DatabaseViewModel>()
    private val sharedPrefViewModel by viewModels<SharedPreferencesViewModel>()
    private lateinit var newUserRole: String
    private var apartmentsList = arrayListOf<ApartmentModel>()
    private var isRoleSelected: Boolean = false
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
        observeApartmentsFlow()
        (requireActivity() as MainActivity).hideBottomNavigation()
    }

    //Yeni kayıt olunurken girilen bilgiler doğrultusunda istek atılmasını ve gelen cevaba göre yönlendirme yapılmasını sağlar
    private fun observeSignUpFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.signupFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                        binding.pbProgressBar.visibility = GONE

                    }
                    is Resource.Loading -> {
                        binding.pbProgressBar.visibility = VISIBLE
                    }
                    is Resource.Success -> {
                        if (findNavController().currentDestination?.id == R.id.signupFragment) {
                            writeDataToSharedPref()
                            val action =
                                SignupFragmentDirections.actionSignupFragmentToExecutiveHomePage()
                            findNavController().navigate(action)
                            binding.pbProgressBar.visibility = GONE

                        }

                    }
                    else -> {

                    }
                }
            }

        }
    }

    //Girilen bilgilerin shared preferencese yazılması sağlanır
    private fun writeDataToSharedPref() {
        sharedPrefViewModel.writeIsLogin(true)
        sharedPrefViewModel.writeApartmentName(binding.etApartmentCode.text.toString())
        sharedPrefViewModel.writeUserName(authViewModel.currentUser?.displayName.toString())
        databaseViewModel.writeUserDocumentIdToSharedPref(
            authViewModel.currentUser!!.displayName.toString(),
            binding.etApartmentCode.text.toString()
        )
    }

    //Seçilen role göre gerekli kayıt olma fonksiyonları çalıştırılır.
    private fun signUp(
        name: String,
        email: String,
        password: String,
        apartmentCode: String,
        role: String,
        doorNumber: String,
        carPlate: String
    ) {
        if (newUserRole == "yonetici") {
            databaseViewModel.addNewApartment(
                name,
                apartmentCode,
                carPlate,
                doorNumber,
                role,
                apartmentCode
            )
        } else  {
            databaseViewModel.addNewUser(name, apartmentCode, carPlate, doorNumber, role)
        }
        authViewModel.signup(name, email, password)

        observeSignUpFlow()
    }


    private fun getApartments() {
        databaseViewModel.getAllApartments()

    }
    //Yöneticinin girdiği apartman koduyla daha önce kayıt yapılıp yapılmadığını sorgular
    private fun observeApartmentsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            databaseViewModel.apartmentsFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                        binding.pbProgressBar.visibility = GONE

                    }
                    is Resource.Loading -> {
                        binding.pbProgressBar.visibility = VISIBLE
                    }
                    is Resource.Success -> {
                        apartmentsList.clear()
                        apartmentsList.addAll(it.result)
                        val isTaken = isApartmentNameTaken(
                            apartmentsList,
                            binding.etApartmentCode.text.toString()
                        )
                        val userName = binding.etNewUserName.text.toString()
                        val userEmail = binding.etNewUserMail.text.toString()
                        val userPassword = binding.etNewUserPassword.text.toString()
                        val userCarPlate = binding.etCarPlate.text.toString()
                        val userDoorNumber = binding.etDoorNumber.text.toString()
                        val apartmentCode = binding.etApartmentCode.text.toString()
                        if(newUserRole == "yonetici"){

                            if (isTaken) {
                                binding.etApartmentCode.error = "Bu İsim Daha Önce Alındı.Apartmanınıza Başka Bir İsim Belirleyiniz"
                                binding.pbProgressBar.visibility = GONE
                            } else {
                                    signUp(
                                        userName,
                                        userEmail,
                                        userPassword,
                                        apartmentCode,
                                        newUserRole,
                                        userDoorNumber,
                                        userCarPlate
                                    )
                            }
                        }else{
                            if(isTaken){
                                    signUp(
                                        userName,
                                        userEmail,
                                        userPassword,
                                        apartmentCode,
                                        newUserRole,
                                        userDoorNumber,
                                        userCarPlate
                                    )
                            }else{
                                binding.etApartmentCode.error = "Lütfen Geçerli Bir İsim Giriniz"
                                binding.pbProgressBar.visibility = GONE
                            }
                        }

                    }
                    else -> {

                    }
                }
            }

        }
    }

    private fun isApartmentNameTaken(
        apartmentList: List<ApartmentModel>,
        newUserApartmentName: String
    ): Boolean {
        for (apartment in apartmentList) {
            if (newUserApartmentName == apartment.apartmentName) {
                return true
            }
        }
        return false
    }

    private fun onClickHandler() {
        binding.apply {
            buttonNavigateLogin.setOnClickListener {
                findNavController().popBackStack()
            }

            buttonSignup.setOnClickListener {
                if (isRoleSelected) {
                    getApartments()

                } else {
                    Toast.makeText(context, "Rol Seçimi Yapmak Zorunludur.", Toast.LENGTH_LONG)
                        .show()
                }

            }
            toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.button1 -> {
                            newUserRole = "yonetici"
                            isRoleSelected = true

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
                } else {
                    isRoleSelected = false
                }
            }
        }
    }


}