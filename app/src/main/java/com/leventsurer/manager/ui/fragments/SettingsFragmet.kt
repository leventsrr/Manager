package com.leventsurer.manager.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.leventsurer.manager.MainActivity
import com.leventsurer.manager.R
import com.leventsurer.manager.databinding.FragmentSettingsFragmetBinding
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import com.leventsurer.manager.viewModels.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class SettingsFragmet : Fragment() {
    private var _binding: FragmentSettingsFragmetBinding? = null
    private val binding: FragmentSettingsFragmetBinding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    private val sharedPreferencesViewModel by viewModels<SharedPreferencesViewModel>()
    private val databaseViewModel by viewModels<DatabaseViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsFragmetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        onClickHandler()
    }

    private fun onClickHandler() {
        binding.apply {
            btnDeleteAccount.setOnClickListener {
                deleteData()
                Toast.makeText(requireContext(),"Hesap Bilgileriniz Tamamen Silindi",Toast.LENGTH_LONG).show()
                val action = SettingsFragmetDirections.actionSettingsFragmetToLoginFragment()
                findNavController().navigate(action)
            }

            btnSetPassword.setOnClickListener {
                val newPassword = etNewUserPassword.text.toString()
                val newPasswordAgain = etNewUserPasswordAgain.text.toString()
                if(newPassword == newPasswordAgain){
                    authViewModel.updateUserPassword(newPassword)
                    Toast.makeText(requireContext(),"Şifreniz Güncellendi",Toast.LENGTH_LONG).show()
                    etNewUserPassword.text?.clear()
                    etNewUserPasswordAgain.text?.clear()
                }else{
                    etNewUserPasswordAgain.error = "Girilen Şifreler Aynı Değil"
                }

            }
            
        }
    }

    private fun deleteData() {
        databaseViewModel.deleteUserData()
        authViewModel.deleteUser()
        sharedPreferencesViewModel.clearSharedPref()
        authViewModel.logout()
    }


    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "Ayarlar",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_arrow_back_24,
            endIcon = R.drawable.ic_baseline_settings_24,
            startIconClick = {
                findNavController().popBackStack()
            },
            endIconClick = {
            },
        )

        (requireActivity() as MainActivity).hideBottomNavigation()
    }

}