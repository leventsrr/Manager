package com.leventsurer.manager.tools.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventsurer.manager.databinding.DuesPaymentStatusRowBinding


class DuesPaymentStatusAdapter : RecyclerView.Adapter<DuesPaymentStatusAdapter.DuesPaymentStatusHolder>() {


    class DuesPaymentStatusHolder(val binding: DuesPaymentStatusRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var list = ArrayList<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //Tutucu ilk oluşturulduğunda ne yapılacak
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DuesPaymentStatusHolder {
        val binding =
            DuesPaymentStatusRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DuesPaymentStatusHolder(binding)
    }

    //Bağlanma olduktan sonra ne olacak
    override fun onBindViewHolder(holder: DuesPaymentStatusHolder, position: Int) {
        holder.binding.apply {
            twApartmentDweller.text = "Levent Sürer"
        }
    }

    //Kaç tane olacak
    override fun getItemCount(): Int {
        return 32
    }
}