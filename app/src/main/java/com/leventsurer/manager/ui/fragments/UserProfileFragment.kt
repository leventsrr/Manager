package com.leventsurer.manager.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.leventsurer.manager.R
import com.leventsurer.manager.databinding.FragmentUserProfileBinding
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.FirebaseStorageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding: FragmentUserProfileBinding get() = _binding!!
    private val storageViewModel by viewModels<FirebaseStorageViewModel>()
    private var imageUri: Uri? = null

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
        onClickHandler()
    }

    private fun onClickHandler() {
        binding.apply {
            iwUserProfilePhoto.setOnClickListener {
                Log.e("kontrol", "fotoya tıklandı")
                resultLauncher.launch("image/*")
            }

            btnUploadImage.setOnClickListener {
                Log.e("kontrol", "butona tıklandı")
                runBlocking {
                    imageUri?.let { it1 -> storageViewModel.uploadImage(it1) }
                }
            }
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        imageUri = it
        binding.iwUserProfilePhoto.setImageURI(it)
    }

    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "Profiliniz",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_arrow_back_24,
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
    }

}