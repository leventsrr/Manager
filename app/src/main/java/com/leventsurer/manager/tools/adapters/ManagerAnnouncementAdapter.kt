package com.leventsurer.manager.tools.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventsurer.manager.data.model.ConciergeAnnouncementModel
import com.leventsurer.manager.data.model.ManagerAnnouncementModel
import com.leventsurer.manager.databinding.ConciergeAnnouncementRowBinding
import com.leventsurer.manager.databinding.ManagerAnnouncementRowBinding


class ManagerAnnouncementAdapter : RecyclerView.Adapter<ManagerAnnouncementAdapter.ManagerAnnouncementHolder>() {


    class ManagerAnnouncementHolder(val binding: ManagerAnnouncementRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var list = ArrayList<ManagerAnnouncementModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //Tutucu ilk oluşturulduğunda ne yapılacak
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagerAnnouncementHolder {
        val binding =
            ManagerAnnouncementRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ManagerAnnouncementHolder(binding)
    }

    //Bağlanma olduktan sonra ne olacak
    override fun onBindViewHolder(holder: ManagerAnnouncementHolder, position: Int) {
        holder.binding.apply {
            val currentItem: ManagerAnnouncementModel = list[position]
            twAnnouncement.text = currentItem.announcement
        }
    }

    //Kaç tane olacak
    override fun getItemCount(): Int {
        return list.size
    }
}