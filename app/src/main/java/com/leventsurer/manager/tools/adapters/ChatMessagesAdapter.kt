package com.leventsurer.manager.tools.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.ChatMessageModel
import com.leventsurer.manager.databinding.UserChatMessageRowBinding
import com.leventsurer.manager.viewModels.AuthViewModel

class ChatMessagesAdapter(private val authViewModel: AuthViewModel) : RecyclerView.Adapter<ChatMessagesAdapter.ChatMessageHolder>(

) {
    private val userName = authViewModel.currentUser!!.displayName

    class ChatMessageHolder(val binding: UserChatMessageRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var list = ArrayList<ChatMessageModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageHolder {
        val binding =
            UserChatMessageRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatMessageHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ChatMessageHolder, position: Int) {
        holder.binding.apply {

            val currentItem:ChatMessageModel = list[position]

            if(currentItem.userName == userName){
                cwMessageCard.setCardBackgroundColor(R.color.thirdColor)
                twSender.visibility = GONE
                Log.e("kontrol","if içinde")
            }else{
                Log.e("kontrol","else içinde")
                twSender.text = currentItem.userName
            }
            twMessage.text = currentItem.message
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}