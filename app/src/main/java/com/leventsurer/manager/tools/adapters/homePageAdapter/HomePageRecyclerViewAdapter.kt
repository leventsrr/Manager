package com.leventsurer.manager.tools.adapters.homePageAdapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.PollModel
import com.leventsurer.manager.databinding.ConciergeAnnouncementRowBinding
import com.leventsurer.manager.databinding.ManagerAnnouncementRowBinding
import com.leventsurer.manager.databinding.PollRowBinding
import com.leventsurer.manager.databinding.ResidentRequestRawBinding

class HomeRecyclerViewAdapter : RecyclerView.Adapter<HomeRecyclerViewHolder>() {

    var items = listOf<HomeRecyclerViewItem>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder {
        return when (viewType) {
            R.layout.manager_announcement_row -> HomeRecyclerViewHolder.ManagerAnnouncementHolder(
                ManagerAnnouncementRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.concierge_announcement_row -> HomeRecyclerViewHolder.ConciergeAnnouncementHolder(
                ConciergeAnnouncementRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.resident_request_raw -> HomeRecyclerViewHolder.ResidentRequestHolder(
                ResidentRequestRawBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.poll_row -> HomeRecyclerViewHolder.PollHolder(
                PollRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }
    }

    override fun onBindViewHolder(holder: HomeRecyclerViewHolder, position: Int) {
        when (holder) {
            is HomeRecyclerViewHolder.ManagerAnnouncementHolder -> holder.bind(items[position] as HomeRecyclerViewItem.ManagerAnnouncement)
            is HomeRecyclerViewHolder.ConciergeAnnouncementHolder -> holder.bind(items[position] as HomeRecyclerViewItem.ConciergeAnnouncement)
            is HomeRecyclerViewHolder.ResidentRequestHolder -> holder.bind(items[position] as HomeRecyclerViewItem.ResidentRequest)
            is HomeRecyclerViewHolder.PollHolder ->{
                holder.bind(items[position] as HomeRecyclerViewItem.Polls)
                val btn = holder.itemView.findViewById<Button>(R.id.btnSendAnswer)
                val radioButton1 = holder.itemView.findViewById<RadioButton>(R.id.radio_button_1)
                val radioButton2 = holder.itemView.findViewById<RadioButton>(R.id.radio_button_2)
                val pollTextView = holder.itemView.findViewById<TextView>(R.id.twPollText)
                val pollText = pollTextView.text.toString()
                val pdfButton = holder.itemView.findViewById<ImageView>(R.id.btnCreatePdfFile)
                btn.setOnClickListener {
                    sendPollAnswer.let {
                        if (it != null){
                            if(radioButton1.isChecked){
                                it(pollText,false)
                            }else if(radioButton2.isChecked){
                                it(pollText,true)
                            }

                        }
                    }
                }
                pdfButton.setOnClickListener {

                    createPdfFile.let {
                        if (it != null) {
                            it(items[position] as HomeRecyclerViewItem.Polls)
                        }
                    }
                }

            }

        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HomeRecyclerViewItem.ManagerAnnouncement -> R.layout.manager_announcement_row
            is HomeRecyclerViewItem.ConciergeAnnouncement -> R.layout.concierge_announcement_row
            is HomeRecyclerViewItem.ResidentRequest -> R.layout.resident_request_raw
            is HomeRecyclerViewItem.Polls -> R.layout.poll_row
        }
    }

    private var sendPollAnswer: ((pollText: String,isAgree:Boolean) -> Unit)? = null
    fun sendPollAnswer(f: ((pollText: String,isAgree:Boolean) -> Unit)) {
        sendPollAnswer = f
    }

    private var createPdfFile: ((poll: HomeRecyclerViewItem.Polls) -> Unit)? = null
    fun createPdfFile(f: ((poll: HomeRecyclerViewItem.Polls) -> Unit)) {
        createPdfFile = f
    }

}
 