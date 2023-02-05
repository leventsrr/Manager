package com.leventsurer.manager.tools.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventsurer.manager.data.model.ConciergeDutiesModel
import com.leventsurer.manager.databinding.ConciergeDutyRowBinding

class ConciergeDutyToDoAdapterAdapter : RecyclerView.Adapter<ConciergeDutyToDoAdapterAdapter.ConciergeDutyToDoHolder>() {


    class ConciergeDutyToDoHolder(val binding: ConciergeDutyRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var list = ArrayList<ConciergeDutiesModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //Tutucu ilk oluşturulduğunda ne yapılacak
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConciergeDutyToDoHolder {
        val binding =
            ConciergeDutyRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConciergeDutyToDoHolder(binding)
    }

    //Bağlanma olduktan sonra ne olacak
    override fun onBindViewHolder(holder: ConciergeDutyToDoHolder, position: Int) {
        holder.binding.apply {
            val currentItem = list[position]
            twDuty.text = currentItem.duty
        }
    }

    //Kaç tane olacak
    override fun getItemCount(): Int {
        return list.size
    }
}