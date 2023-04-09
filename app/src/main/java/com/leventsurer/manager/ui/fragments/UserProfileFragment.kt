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
    private var isExpense:Boolean? = null
    private  var userModel=UserModel()
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
                        Toast.makeText(context, it.exception.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        userModel.fullName = it.result.fullName
                        userModel.phoneNumber = it.result.phoneNumber
                        userModel.carPlate = it.result.carPlate
                        userModel.doorNumber = it.result.doorNumber

                        if(it.result.role == "yonetici"){
                            binding.apply {
                                mcwSetMonthyPaymentCard.visibility = VISIBLE
                                mcwShareNewAnnouncementCard.visibility =VISIBLE
                                mcwShareNewExpenseOrIncomeCard.visibility = VISIBLE
                                managerPollCard.visibility = VISIBLE
                                bindUserInfoToUi(it.result)
                            }
                        }else if(it.result.role == "sakin"){
                            binding.apply {
                                bindUserInfoToUi(it.result)
                                mcwResidentMaterialCard1.visibility = VISIBLE
                                mcwResidentMaterialCard2.visibility = VISIBLE
                            }
                        }

                    }
                    else -> { }
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
                val time = FieldValue.serverTimestamp()
                databaseViewModel.addNewRequest(userRequest,time)
                etUserRequest.text?.clear()
            }

            btnSendExpense.setOnClickListener {
                if(radioButton1.isChecked || radioButton2.isChecked || etAmountName.text.toString().isNotEmpty()|| etAmount.text.toString().isNotEmpty()){

                        if(radioButton1.isChecked){
                            isExpense = false
                        }else if(radioButton2.isChecked){
                            isExpense = true
                        }
                        val amount = etAmount.text.toString().toDouble()
                        val time = FieldValue.serverTimestamp()
                        val eventName = etAmountName.text.toString()
                        databaseViewModel.addBudgetMovement(amount,isExpense!!,time,eventName)

                }else if(!(radioButton1.isChecked || radioButton2.isChecked)){
                    Toast.makeText(requireContext(),"Lütfen Bir Tip Seçiniz",Toast.LENGTH_LONG).show()

                }else if(etAmountName.text.toString().isEmpty()){
                    etAmountName.error = "Lütfen İşlemi Açıklaması Giriniz"
                }else if( etAmount.text.toString().isEmpty()){
                    etAmount.error = "Lütfen İşlemi Tutarı Giriniz"
                }

                radioButton1.isChecked = false
                radioButton2.isChecked = false
                etAmountName.text?.clear()
                etAmount.text?.clear()


            }

            btnNewMonthlyPayment.setOnClickListener {
                val amount:Double = etMonthlyPaymentAmount.text.toString().toDouble()
                databaseViewModel.setApartmentMonthlyPayment(amount)
                etMonthlyPaymentAmount.text?.clear()
                Toast.makeText(requireContext(),"Aidat Güncellendi",Toast.LENGTH_LONG).show()
            }

            btnSendAnnouncement.setOnClickListener {
                if(etManagerAnnouncement.text.isNullOrEmpty()){
                    Toast.makeText(requireContext(),"Boş Duyuru Paylaşılamaz",Toast.LENGTH_LONG).show()
                }else{
                    val announcement = etManagerAnnouncement.text.toString()
                    val time =  FieldValue.serverTimestamp()
                    databaseViewModel.addNewManagerAnnouncement(announcement,time)
                    etManagerAnnouncement.text?.clear()
                    Toast.makeText(requireContext(),"Duyuru Paylaşıldı",Toast.LENGTH_LONG).show()
                }

            }

            btnEditInfo.setOnClickListener {

                ProfileCustomDialog(userModel).show(parentFragmentManager,"Custom Fragment")

            }


            btnPollShare.setOnClickListener {
                val time:FieldValue = FieldValue.serverTimestamp()
                val pollText:String = etPoll.text.toString()
                databaseViewModel.addNewPoll(pollText, time)
                etPoll.text?.clear()
                Toast.makeText(requireContext(),"Anket Paylaşıldı",Toast.LENGTH_LONG).show()
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
        (requireActivity() as MainActivity).showBottomNavigation()
        binding.btnUploadImage.text = "Resim Seç"
    }

}