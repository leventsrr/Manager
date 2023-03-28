package com.leventsurer.manager.ui.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.leventsurer.manager.MainActivity
import com.leventsurer.manager.R
import com.leventsurer.manager.databinding.FragmentChatBinding
import com.leventsurer.manager.databinding.FragmentSplashBinding
import com.leventsurer.manager.viewModels.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding ? = null
    private val binding: FragmentSplashBinding get() = _binding!!
    private val sharedPrefViewModel by viewModels<SharedPreferencesViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).hideBottomNavigation()

        Handler().postDelayed({
            checkIsLoginInfo()
        }, 1000)

    }



    private fun checkIsLoginInfo() {
        val isLogin = sharedPrefViewModel.readIsLogin()
        if(isLogin == true){
            val action =
                SplashFragmentDirections.actionSplashFragmentToExecutiveHomePage()
            findNavController().navigate(action)
        }else{
            val action =
                SplashFragmentDirections.actionSplashFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }


}