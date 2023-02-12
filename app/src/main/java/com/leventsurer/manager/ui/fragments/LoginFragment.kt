package com.leventsurer.manager.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.leventsurer.manager.MainActivity
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.databinding.FragmentLoginBinding
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()
    private val dataStoreViewModel by viewModels<DataStoreViewModel>()


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

    private fun observeFlow() {
        viewLifecycleOwner.lifecycleScope.launch{
                viewModel.loginFlow.collect{
                    when(it){
                        is Resource.Failure ->{
                            Toast.makeText(context,it.exception.message,Toast.LENGTH_LONG).show()
                            binding.pbProgressBar.visibility = View.GONE

                        }
                        is Resource.Loading ->{
                            binding.pbProgressBar.visibility = View.VISIBLE
                        }
                        is Resource.Success ->{
                            if(findNavController().currentDestination?.id == R.id.loginFragment){
                                writeDataStore()
                                val action = LoginFragmentDirections.actionLoginFragmentToExecutiveHomePage()
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

    private fun writeDataStore(){
        dataStoreViewModel.storeUserName(viewModel.currentUser!!.displayName!!)
        dataStoreViewModel.storeApartmentCode(binding.twUserApartmentName.text.toString())
        dataStoreViewModel.storeIsLogin(true)

    }
    private fun onClickHandler() {
        binding.apply {
            buttonNavigateSignUp.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
                findNavController().navigate(action)
            }
            buttonLogin.setOnClickListener {
                val email:String = twUserEmail.text.toString()
                val password:String = twUserPassword.text.toString()
                viewModel.login(email,password)
                observeFlow()
            }

            iwSignInWithGoogle.setOnClickListener {

            }
        }
    }


}