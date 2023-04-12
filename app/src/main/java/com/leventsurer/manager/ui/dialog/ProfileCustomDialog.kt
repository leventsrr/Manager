package com.leventsurer.manager.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.data.model.UserModel
import com.leventsurer.manager.databinding.CustomAlertDialogBinding
import com.leventsurer.manager.viewModels.DatabaseViewModel
import com.leventsurer.manager.viewModels.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class ProfileCustomDialog(userModel:UserModel) :DialogFragment() {
    private var _binding: CustomAlertDialogBinding? = null
    private val binding: CustomAlertDialogBinding get() = _binding!!
    private val databaseViewModel by viewModels<DatabaseViewModel>()

    private val userModel : UserModel = userModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomAlertDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()

        onClickHandler()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            twUserName.setText(userModel.fullName)
            twUserCarPlate.setText(userModel.carPlate)
            twUserDoorNumber.setText(userModel.doorNumber)
            twUserPhoneNumber.setText(userModel.phoneNumber)
        }
    }


    private fun onClickHandler() {
        binding.apply {
            btnNewInfoSave.setOnClickListener {
                val userName =twUserName.text.toString()
                val phoneNumber = twUserPhoneNumber.text.toString()
                val carPlate = twUserCarPlate.text.toString()
                val doorNumber = twUserDoorNumber.text.toString()

                runBlocking {
                    databaseViewModel.updateUserInfo(userName, phoneNumber, carPlate, doorNumber)
                }
                dialog?.cancel()
                Toast.makeText(requireContext(),"Bilgiler Sayfa Güncellendiğinde Güncellenecektir.",Toast.LENGTH_LONG).show()
            }

            btnCancel.setOnClickListener {
                dialog?.cancel()
            }
        }

    }


}