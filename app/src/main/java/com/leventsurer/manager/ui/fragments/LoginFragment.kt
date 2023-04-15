package com.leventsurer.manager.ui.fragments

import android.os.Bundle
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
import com.leventsurer.manager.databinding.FragmentLoginBinding
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import com.leventsurer.manager.viewModels.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
    private var apartmentList = arrayListOf<ApartmentModel>()
    private val viewModel by viewModels<AuthViewModel>()
    private val sharedPrefViewModel by viewModels<SharedPreferencesViewModel>()
    private val databaseViewModel by viewModels<DatabaseViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickHandler()

        (requireActivity() as MainActivity).hideBottomNavigation()

    }

    //Giriş yapılırken girilen bilgiler doğrultusunda istek atılmasını ve gelen cevaba göre yönlendirme yapılmasını sağlar
    private fun observeLoginFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                        binding.pbProgressBar.visibility = GONE

                    }
                    is Resource.Loading -> {
                        binding.pbProgressBar.visibility = VISIBLE
                    }
                    is Resource.Success -> {
                        if (findNavController().currentDestination?.id == R.id.loginFragment) {

                            writeDataToSharedPref()


                            val action =
                                LoginFragmentDirections.actionLoginFragmentToExecutiveHomePage()
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

    //Giriş yapılırken gerekli bilgilerin shared preferencese kaydedilmesini sağlar.
    private fun writeDataToSharedPref() {
        sharedPrefViewModel.writeIsLogin(true)
        sharedPrefViewModel.writeApartmentName(binding.twUserApartmentName.text.toString())
        sharedPrefViewModel.writeUserName(viewModel.currentUser?.displayName.toString())
        databaseViewModel.writeUserDocumentIdToSharedPref(
            viewModel.currentUser!!.displayName.toString(),
            binding.twUserApartmentName.text.toString()
        )
    }

    private fun getApartments(){
        databaseViewModel.getAllApartments()
        observeApartmentsFlow()
    }

    private fun isApartmentCodeInDatabase(apartments:List<ApartmentModel>, userApartment: String):Boolean{
        for(apartment in apartments){
            if(apartment.apartmentName == userApartment){
                return true
            }
        }
        return false
    }
    //kullanıcının geçerli bir apartman kodu girip girmediğini kontrol eder
    private fun observeApartmentsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            databaseViewModel.apartmentsFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                        binding.pbProgressBar.visibility = View.GONE

                    }
                    is Resource.Loading -> {
                        binding.pbProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        apartmentList.addAll(it.result)
                        val isApartmentNameValid = isApartmentCodeInDatabase(apartmentList,binding.twUserApartmentName.text.toString())
                        if(isApartmentNameValid){
                            val email: String = binding.twUserEmail.text.toString()
                            val password: String = binding.twUserPassword.text.toString()
                            viewModel.login(email, password)
                            observeLoginFlow()
                        }else{
                            binding.twUserApartmentName.error = "Lütfen Geçerli Bir Apartman Kodu Giriniz"
                            binding.pbProgressBar.visibility = GONE
                        }
                    }
                    else -> {

                    }
                }
            }

        }
    }

    private fun onClickHandler() {
        binding.apply {
            buttonNavigateSignUp.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
                findNavController().navigate(action)
            }
            buttonLogin.setOnClickListener {
                getApartments()
            }
            iwSignInWithGoogle.setOnClickListener {

            }
        }
    }


}