package com.leventsurer.manager.tools.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventsurer.manager.data.model.ConciergeDutiesModel
import com.leventsurer.manager.databinding.ConciergeDutyRowBinding

class ConciergeDutyToDoneAdapter : RecyclerView.Adapter<ConciergeDutyToDoneAdapter.ConciergeDutyToDoneHolder>() {


    class ConciergeDutyToDoneHolder(val binding: ConciergeDutyRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var list = ArrayList<ConciergeDutiesModel>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConciergeDutyToDoneHolder {
        val binding =
            ConciergeDutyRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConciergeDutyToDoneHolder(binding)
    }

    override fun onBindViewHolder(holder: ConciergeDutyToDoneHolder, position: Int) {
        holder.binding.apply {
            val currentItem = list[position]
            twDuty.text = currentItem.duty
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}