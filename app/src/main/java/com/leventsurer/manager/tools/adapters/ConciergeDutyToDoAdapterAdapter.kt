package com.leventsurer.manager.tools.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventsurer.manager.data.model.ConciergeDutiesModel
import com.leventsurer.manager.databinding.ConciergeDutyRowBinding

class ConciergeDutyToDoAdapterAdapter(userRole:String) : RecyclerView.Adapter<ConciergeDutyToDoAdapterAdapter.ConciergeDutyToDoHolder>() {

    private val role = userRole
    class ConciergeDutyToDoHolder(val binding: ConciergeDutyRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var list = ArrayList<ConciergeDutiesModel>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConciergeDutyToDoHolder {
        val binding =
            ConciergeDutyRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConciergeDutyToDoHolder(binding)
    }

    override fun onBindViewHolder(holder: ConciergeDutyToDoHolder, position: Int) {
        holder.binding.apply {
            val currentItem = list[position]
            twDuty.text = currentItem.duty
            if(role == "kapici"){
                btnDoneDuty.visibility = VISIBLE
            }
            btnDoneDuty.setOnClickListener {
                markDoneDuty.let {
                    if (it != null) {
                        it(currentItem)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private var markDoneDuty: ((conciergeDuty: ConciergeDutiesModel) -> Unit)? = null
    fun markDoneDuty(f: ((conciergeDuty: ConciergeDutiesModel) -> Unit)) {
        markDoneDuty = f
    }
}