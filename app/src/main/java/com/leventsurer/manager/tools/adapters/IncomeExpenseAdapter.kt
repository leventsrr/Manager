package com.leventsurer.manager.tools.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.FinancialEventModel
import com.leventsurer.manager.databinding.IncomeExpenseRowBinding


class IncomeExpenseAdapter : RecyclerView.Adapter<IncomeExpenseAdapter.IncomeExpenseHolder>() {


    class IncomeExpenseHolder(val binding: IncomeExpenseRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var list = ArrayList<FinancialEventModel>()
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
            val currentItem = list[position]
            textViewIncomeExpenseName.text = currentItem.eventName
            Log.e("kontrol",currentItem.isExpense.toString())
            if (currentItem.isExpense) {
                iwArrowImage.setImageResource(R.drawable.ic_baseline_expense_arrow)
            }else{
                iwArrowImage.setImageResource(R.drawable.ic_outline_income_arrow)
            }

        }
    }

    //Kaç tane olacak
    override fun getItemCount(): Int {
        return list.size
    }
}