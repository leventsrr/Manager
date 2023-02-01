package com.leventsurer.manager.tools.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventsurer.manager.data.model.ConciergeAnnouncementModel
import com.leventsurer.manager.databinding.ConciergeAnnouncementRowBinding

class ConciergeAnnouncementAdapter : RecyclerView.Adapter<ConciergeAnnouncementAdapter.ConciergeAnnouncementHolder>() {


    class ConciergeAnnouncementHolder(val binding: ConciergeAnnouncementRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var list = ArrayList<ConciergeAnnouncementModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //Tutucu ilk oluşturulduğunda ne yapılacak
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConciergeAnnouncementHolder {
        val binding =
            ConciergeAnnouncementRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConciergeAnnouncementHolder(binding)
    }

    //Bağlanma olduktan sonra ne olacak
    override fun onBindViewHolder(holder: ConciergeAnnouncementHolder, position: Int) {
        holder.binding.apply {
            val currentItem:ConciergeAnnouncementModel = list[position]
            twAnnouncement.text = currentItem.title
        }
    }

    //Kaç tane olacak
    override fun getItemCount(): Int {
        return list.size
    }
}