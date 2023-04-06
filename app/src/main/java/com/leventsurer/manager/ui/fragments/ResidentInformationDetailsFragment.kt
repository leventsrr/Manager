package com.leventsurer.manager.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.leventsurer.manager.MainActivity
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.data.model.UserModel
import com.leventsurer.manager.databinding.FragmentResidentInformationDetailsBinding
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResidentInformationDetailsFragment : Fragment() {
    private var _binding: FragmentResidentInformationDetailsBinding? = null
    private val binding: FragmentResidentInformationDetailsBinding get() = _binding!!
    lateinit var userModel: UserModel
    private val databaseViewModel by viewModels<DatabaseViewModel>()

    private var REQUEST_PHONE_CALL = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResidentInformationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        handleArguments()
        getUserInfo()
        onClickHandler()
    }

    private fun onClickHandler() {
        binding.apply {
            btnCallResident.setOnClickListener {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.CALL_PHONE),
                        REQUEST_PHONE_CALL
                    )
                } else {
                    makeCall()
                }


            }
        }
    }

    //detayı görüntülenen kişinin aranmasını sağlar
    private fun makeCall() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:${userModel.phoneNumber}")
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(requireContext(), "İzin Gerekli", Toast.LENGTH_LONG).show()
            return
        }
        startActivity(intent)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PHONE_CALL) {
            makeCall()
        }
    }

    private fun getUserInfo() {
        databaseViewModel.getAUserByNameAndDoorNumber(userModel.fullName, userModel.doorNumber)
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
                    bindUserInfoToUi(it.result)
                }
                else -> {}
            }
        }

    }

    private fun bindUserInfoToUi(model: UserModel) {
        binding.apply {
            twUserName.text = model.fullName
            twDoorNumber.text = model.doorNumber
            twUserCarPlate.text = model.carPlate
            twUserPhoneNumber.text = model.phoneNumber
            Glide.with(requireContext()).load(model.imageLink).into(binding.iwUserProfilePhoto)

        }
    }

    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "Apartman Sakini Detayı",
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

    //get the data sent from the other page
    private fun handleArguments() {
        arguments?.let {
            userModel = ResidentInformationDetailsFragmentArgs.fromBundle(it).userModel
        }
    }
}