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
import com.leventsurer.manager.data.repository.DatabaseRepository
import com.leventsurer.manager.data.repository.DatabaseRepositoryImpl
import com.leventsurer.manager.databinding.FragmentLoginBinding
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import com.leventsurer.manager.viewModels.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

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
        checkIsLoginInfo()
        (requireActivity() as MainActivity).hideBottomNavigation()

    }

    private fun checkIsLoginInfo() {
        val isLogin = sharedPrefViewModel.readIsLogin()
        if(isLogin == true){
            val action =
                LoginFragmentDirections.actionLoginFragmentToExecutiveHomePage()
            findNavController().navigate(action)
            binding.pbProgressBar.visibility = View.GONE
        }
    }

    //Giriş yapılırken girilen bilgiler doğrultusunda istek atılmasını ve gelen cevaba göre yönlendirme yapılmasını sağlar
    private fun observeFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                        binding.pbProgressBar.visibility = View.GONE

                    }
                    is Resource.Loading -> {
                        binding.pbProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        if (findNavController().currentDestination?.id == R.id.loginFragment) {
                            writeDataToSharedPref()

                            val action =
                                LoginFragmentDirections.actionLoginFragmentToExecutiveHomePage()
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

    //Giriş yapılırken gerekli bilgilerin shared preferencese kaydedilmesini sağlar.
    private fun writeDataToSharedPref() {
        sharedPrefViewModel.writeIsLogin(true)
        sharedPrefViewModel.writeApartmentName(binding.twUserApartmentName.text.toString())
        sharedPrefViewModel.writeUserName(viewModel.currentUser?.displayName.toString())
        /*databaseViewModel.getUserDocumentId(
            viewModel.currentUser!!.displayName.toString(),
            binding.twUserApartmentName.text.toString()
        )*/
    }



    private fun onClickHandler() {
        binding.apply {
            buttonNavigateSignUp.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
                findNavController().navigate(action)
            }
            buttonLogin.setOnClickListener {
                val email: String = twUserEmail.text.toString()
                val password: String = twUserPassword.text.toString()
                viewModel.login(email, password)
                observeFlow()
            }

            iwSignInWithGoogle.setOnClickListener {

            }
        }
    }


}