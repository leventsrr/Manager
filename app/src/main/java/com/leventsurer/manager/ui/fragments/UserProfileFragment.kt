package com.leventsurer.manager.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FieldValue
import com.leventsurer.manager.MainActivity
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.data.model.UserModel
import com.leventsurer.manager.databinding.FragmentUserProfileBinding
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.ui.dialog.ProfileCustomDialog
import com.leventsurer.manager.viewModels.AuthViewModel
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
    private val authViewModel by viewModels<AuthViewModel>()
    private var imageUri: Uri? = null
    private var apartmentCode: String? = null
    private var isExpense: Boolean? = null
    private var userModel = UserModel()
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
        observeUserInfo()
    }


    //Giriş yapan kullanıcının verilerini veritabanından çeker
    private fun getUserInfo() {
        apartmentCode = sharedPreferencesViewModel.readApartmentName()
        databaseViewModel.getUserInfo()
    }

    private fun observeUserInfo() {
        databaseViewModel.userInfoFlow.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Failure -> {
                    binding.pbProgressBar.visibility = GONE
                    Toast.makeText(context, "it.exception.message", Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    binding.pbProgressBar.visibility = VISIBLE
                }
                is Resource.Success -> {
                    binding.pbProgressBar.visibility = GONE
                    userModel.fullName = it.result.fullName
                    userModel.phoneNumber = it.result.phoneNumber
                    userModel.carPlate = it.result.carPlate
                    userModel.doorNumber = it.result.doorNumber
                    bindUserInfoToUi(it.result)
                    when (it.result.role) {
                        "yonetici" -> {
                            binding.apply {
                                mcwAssignConciergeDutyCard.visibility = VISIBLE
                                managerCards.visibility = VISIBLE
                            }
                        }
                        "sakin" -> {
                            binding.apply {
                                mcwAssignConciergeDutyCard.visibility = VISIBLE
                                residentCards.visibility = VISIBLE
                            }
                        }
                        "kapici" -> {
                            binding.apply {
                                conciergeCards.visibility = VISIBLE

                            }
                        }
                    }

                }
                else -> {}
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
                databaseViewModel.setUserDuesPaymentStatus(currentStatus, userModel.fullName)
            }

            btnSendRequest.setOnClickListener {
                if (etUserRequest.text.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Boş İstek Paylaşılamaz", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val userRequest = etUserRequest.text.toString()
                    val time = FieldValue.serverTimestamp()
                    databaseViewModel.addNewRequest(userRequest, time)
                    etUserRequest.text?.clear()
                    Toast.makeText(requireContext(), "Yeni İstek Paylaşıldı", Toast.LENGTH_LONG)
                        .show()
                }
            }

            btnSendExpense.setOnClickListener {
                if (radioButton1.isChecked || radioButton2.isChecked && etAmountName.text.toString()
                        .isNotEmpty() && etAmount.text.toString().isNotEmpty()
                ) {

                    if (radioButton1.isChecked) {
                        isExpense = false
                    } else if (radioButton2.isChecked) {
                        isExpense = true
                    }
                    val amount = etAmount.text.toString().toDouble()
                    val time = FieldValue.serverTimestamp()
                    val eventName = etAmountName.text.toString()
                    databaseViewModel.addBudgetMovement(amount, isExpense!!, time, eventName)
                    radioButton1.isChecked = false
                    radioButton2.isChecked = false
                    etAmountName.text?.clear()
                    etAmount.text?.clear()
                    Toast.makeText(
                        requireContext(),
                        "Yeni Gelir/Gider Paylaşıldı",
                        Toast.LENGTH_LONG
                    ).show()

                } else if (!(radioButton1.isChecked || radioButton2.isChecked)) {
                    Toast.makeText(requireContext(), "Lütfen Bir Tip Seçiniz", Toast.LENGTH_LONG)
                        .show()


                } else if (etAmountName.text.toString().isEmpty()) {
                    etAmountName.error = "Lütfen İşlemi Açıklaması Giriniz"

                } else if (etAmount.text.toString().isEmpty()) {
                    etAmount.error = "Lütfen İşlemi Tutarı Giriniz"

                }

            }

            btnNewMonthlyPayment.setOnClickListener {
                if (etMonthlyPaymentAmount.text.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Lütfen Bir Miktar Giriniz", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val amount: Double = etMonthlyPaymentAmount.text.toString().toDouble()
                    databaseViewModel.setApartmentMonthlyPayment(amount)
                    etMonthlyPaymentAmount.text?.clear()
                    Toast.makeText(requireContext(), "Aidat Güncellendi", Toast.LENGTH_LONG).show()
                }

            }

            btnSendAnnouncement.setOnClickListener {
                if (etManagerAnnouncement.text.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Boş Duyuru Paylaşılamaz", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val announcement = etManagerAnnouncement.text.toString()
                    val time = FieldValue.serverTimestamp()
                    databaseViewModel.addNewManagerAnnouncement(announcement, time)
                    etManagerAnnouncement.text?.clear()
                    Toast.makeText(requireContext(), "Duyuru Paylaşıldı", Toast.LENGTH_LONG).show()
                }

            }

            btnEditInfo.setOnClickListener {

                ProfileCustomDialog(userModel).show(parentFragmentManager, "Custom Fragment")

            }


            btnPollShare.setOnClickListener {

                if (etPoll.text.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Boş Anket Paylaşılamaz", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val time: FieldValue = FieldValue.serverTimestamp()
                    val pollText: String = etPoll.text.toString()
                    databaseViewModel.addNewPoll(pollText, time)
                    etPoll.text?.clear()
                }


            }

            btnSendConciergeAnnouncement.setOnClickListener {
                if (etConciergeAnnouncement.text.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Boş Duyuru Paylaşılamaz", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val time: FieldValue = FieldValue.serverTimestamp()
                    val announcement: String = etConciergeAnnouncement.text.toString()
                    databaseViewModel.addNewConciergeAnnouncement(announcement, time)
                    etConciergeAnnouncement.text?.clear()
                    Toast.makeText(requireContext(), "Duyuru Paylaşıldı", Toast.LENGTH_LONG).show()
                }

            }

            btnNewConciergeDuty.setOnClickListener {
                if (etNewConciergeDuty.text.isNullOrEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Boş Görev Ataması Yapılamaz",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    val time = FieldValue.serverTimestamp()
                    val duty = etNewConciergeDuty.text.toString()
                    databaseViewModel.addNewConciergeDuty(duty, time)
                    etNewConciergeDuty.text?.clear()
                    Toast.makeText(
                        requireContext(),
                        "Yeni Kapıcı Görevi Eklendi",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            btnReset.setOnClickListener {
                val isRequestReset: Boolean = cbResidentRequest.isChecked
                val isManagerAnnouncementReset: Boolean = cbMnagerAnnouncement.isChecked
                val isConciergeAnnouncementReset: Boolean = cbConciergeAnnouncement.isChecked
                val isPollReset: Boolean = cbPoll.isChecked
                val isFinancialEventReset: Boolean = cbFinancialEvent.isChecked
                val isConciergeDutyReset: Boolean = cbConciergeDuty.isChecked
                Log.e("kontrol","request:${isRequestReset},managerAn:${isManagerAnnouncementReset}")
                databaseViewModel.resetData(
                    isRequestReset,
                    isManagerAnnouncementReset,
                    isConciergeAnnouncementReset,
                    isPollReset,
                    isFinancialEventReset,
                    isConciergeDutyReset
                )
                Toast.makeText(requireContext(), "Seçili Veriler Silindi", Toast.LENGTH_LONG).show()

                cbResidentRequest.isChecked = false
                cbMnagerAnnouncement.isChecked = false
                cbConciergeAnnouncement.isChecked = false
                cbPoll.isChecked = false
                cbFinancialEvent.isChecked = false
                cbConciergeDuty.isChecked = false
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
                authViewModel.logout()
                sharedPreferencesViewModel.clearSharedPref()
                val action =
                    UserProfileFragmentDirections.actionUserProfileFragmentToLoginFragment()
                findNavController().navigate(action)
            },
            endIconClick = {
                val action =
                    UserProfileFragmentDirections.actionUserProfileFragmentToSettingsFragmet()
                findNavController().navigate(action)
            },
        )
        (requireActivity() as MainActivity).showBottomNavigation()
        binding.btnUploadImage.text = "Resim Seç"
    }

}