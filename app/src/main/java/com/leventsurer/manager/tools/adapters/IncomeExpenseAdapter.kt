package com.leventsurer.manager.tools.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventsurer.manager.databinding.IncomeExpenseRowBinding


class IncomeExpenseAdapter : RecyclerView.Adapter<IncomeExpenseAdapter.IncomeExpenseHolder>() {


    class IncomeExpenseHolder(val binding: IncomeExpenseRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var list = ArrayList<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //Tutucu ilk oluşturulduğunda ne yapılacak
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomeExpenseHolder {
        val binding =
            IncomeExpenseRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IncomeExpenseHolder(binding)
    }

    //Bağlanma olduktan sonra ne olacak
    override fun onBindViewHolder(holder: IncomeExpenseHolder, position: Int) {
        holder.binding.apply {
            textViewIncomeExpenseName.text = "Şunun bunun aidatı"
        }
    }

    //Kaç tane olacak
    override fun getItemCount(): Int {
        return 4
    }
}