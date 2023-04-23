package com.leventsurer.manager.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FieldValue
import com.leventsurer.manager.MainActivity
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.ChatMessageModel
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.databinding.FragmentChatBinding
import com.leventsurer.manager.tools.adapters.ChatMessagesAdapter
import com.leventsurer.manager.tools.helpers.HeaderHelper
import com.leventsurer.manager.viewModels.AuthViewModel
import com.leventsurer.manager.viewModels.DatabaseViewModel
import com.leventsurer.manager.viewModels.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding: FragmentChatBinding get() = _binding!!

    private lateinit var apartmentName: String
    private lateinit var userName: String

    private val chatMessagesAdapterList = ArrayList<ChatMessageModel>()
    private lateinit var chatMessageAdapter: ChatMessagesAdapter
    private val sharedPrefViewModel by viewModels<SharedPreferencesViewModel>()
    private val databaseViewModel by viewModels<DatabaseViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readSharedPref()
        setupUi()
        setupChatMessagesAdapter()
        getMessages()
        observeChatMessages()
        onClickHandler()

    }

    private fun setupChatMessagesAdapter() {
        binding.rwChatMessages.layoutManager = LinearLayoutManager(requireContext())
        chatMessageAdapter = ChatMessagesAdapter(sharedPrefViewModel)
        binding.rwChatMessages.adapter = chatMessageAdapter
        binding.rwChatMessages.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
            reverseLayout = false
        }

    }

    private fun getMessages() {
        databaseViewModel.getChatMessages()
    }

    private fun observeChatMessages() {
        databaseViewModel.getChatMessages().observe(viewLifecycleOwner) {

            when (it) {
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    chatMessagesAdapterList.clear()
                    chatMessagesAdapterList.addAll(it.result)
                    chatMessageAdapter.list = chatMessagesAdapterList

                }
                else -> {
                    Log.e("control", "location observe function in else ChatFragment")
                }
            }
        }


    }

    private fun onClickHandler() {
        binding.apply {
            btnSendMessage.setOnClickListener {
                val message = twUserMessage.text.toString()
                val userName = sharedPrefViewModel.readUserName()
                val time = FieldValue.serverTimestamp()
                twUserMessage.text?.clear()
                databaseViewModel.sendChatMessage(message, userName!!, time)
            }
        }
    }

    private fun readSharedPref() {
        apartmentName = sharedPrefViewModel.readApartmentName().toString()
        userName = sharedPrefViewModel.readUserName().toString()
    }

    private fun setupUi() {
        HeaderHelper.customHeader(
            binding.customHeader,
            title = "$apartmentName Sohbet",
            startIconVisibility = true,
            endIconVisibility = true,
            startIcon = R.drawable.ic_baseline_arrow_back_24,
            endIcon = R.drawable.ic_baseline_settings_24,
            startIconClick = {
                findNavController().popBackStack()
            },
            endIconClick = {
                val action = ChatFragmentDirections.actionChatFragmentToSettingsFragmet()
                findNavController().navigate(action)
            },
        )


        (requireActivity() as MainActivity).hideBottomNavigation()
    }


}