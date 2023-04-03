package com.leventsurer.manager.tools.adapters.homePageAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventsurer.manager.R
import com.leventsurer.manager.databinding.ConciergeAnnouncementRowBinding
import com.leventsurer.manager.databinding.ManagerAnnouncementRowBinding
import com.leventsurer.manager.databinding.ResidentRequestRawBinding

class HomeRecyclerViewAdapter : RecyclerView.Adapter<HomeRecyclerViewHolder>() {

    var items = listOf<HomeRecyclerViewItem>()
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
            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }
    }

    override fun onBindViewHolder(holder: HomeRecyclerViewHolder, position: Int) {
        when (holder) {
            is HomeRecyclerViewHolder.ManagerAnnouncementHolder -> holder.bind(items[position] as HomeRecyclerViewItem.ManagerAnnouncement)
            is HomeRecyclerViewHolder.ConciergeAnnouncementHolder -> holder.bind(items[position] as HomeRecyclerViewItem.ConciergeAnnouncement)
            is HomeRecyclerViewHolder.ResidentRequestHolder -> holder.bind(items[position] as HomeRecyclerViewItem.ResidentRequest)

        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HomeRecyclerViewItem.ManagerAnnouncement -> R.layout.manager_announcement_row
            is HomeRecyclerViewItem.ConciergeAnnouncement -> R.layout.concierge_announcement_row
            is HomeRecyclerViewItem.ResidentRequest -> R.layout.resident_request_raw

        }
    }
}
 