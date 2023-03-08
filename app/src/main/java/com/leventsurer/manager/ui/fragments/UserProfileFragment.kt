package com.leventsurer.manager.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.data.model.UserModel
import com.leventsurer.manager.databinding.FragmentUserProfileBinding
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.DatabaseViewModel
import com.leventsurer.manager.viewModels.FirebaseStorageViewModel
import com.leventsurer.manager.viewModels.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding: FragmentUserProfileBinding get() = _binding!!
    private val storageViewModel by viewModels<FirebaseStorageViewModel>()
    private val databaseViewModel by viewModels<DatabaseViewModel>()
    private val sharedPreferencesViewModel by viewModels<SharedPreferencesViewModel>()
    private var imageUri: Uri? = null
    private var apartmentCode:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        getUserInfo()
        onClickHandler()
    }
    //Giriş yapan kullanıcının verilerini veritabanından çeker
    private fun getUserInfo() {
        apartmentCode = sharedPreferencesViewModel.readApartmentName()
        databaseViewModel.getUserInfo()
        observeUserInfo()

    }

    private fun observeUserInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            databaseViewModel.userInfoFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        bindUserInfoToUi(it.result)
                    }
                    else -> { }
                }
            }

        }
    }
    //Gelene kullanıcı bilgilerini arayüzde gerekli yerlere yerleştirir
    private fun bindUserInfoToUi(model: UserModel) {
        binding.twUserName.text = model.fullName
        binding.twUserPhoneNumber.text = model.phoneNumber
        binding.twUserCarPlate.text = model.carPlate
        binding.twUserDoorNumber.text = model.doorNumber
        Glide.with(requireContext()).load(model.imageLink).into(binding.iwUserProfilePhoto)
        binding.cbUserPaymentStatus.isChecked = model.duesPaymentStatus
        binding.twApartmentCode.text = apartmentCode
    }

    private fun onClickHandler() {
        binding.apply {
            iwUserProfilePhoto.setOnClickListener {
                resultLauncher.launch("image/*")
            }

            btnUploadImage.setOnClickListener {
                runBlocking {
                    imageUri?.let { it1 -> storageViewModel.uploadImage(it1) }
                }
            }

            cbUserPaymentStatus.setOnClickListener {
                val currentStatus: Boolean = cbUserPaymentStatus.isChecked
                Log.e("kontrol", "fragment içinde $currentStatus")
                databaseViewModel.setUserDuesPaymentStatus(currentStatus)
            }

            btnSendRequest.setOnClickListener {
                val userRequest = etUserRequest.text.toString()
                databaseViewModel.addNewRequest(userRequest)
                etUserRequest.text?.clear()
            }
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri = it
        binding.iwUserProfilePhoto.setImageURI(it)
        binding.btnUploadImage.text = "Resmi Kaydet"
    }

    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "Profiliniz",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_sensor_door_24,
            endIcon = R.drawable.ic_baseline_settings_24,
            startIconClick = {
                findNavController().popBackStack()
            },
            endIconClick = {
                val action =
                    UserProfileFragmentDirections.actionUserProfileFragmentToSettingsFragmet()
                findNavController().navigate(action)
            },
        )

        binding.btnUploadImage.text = "Resim Seç"
    }

}