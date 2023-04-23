package com.leventsurer.manager.tools.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.FinancialEventModel
import com.leventsurer.manager.databinding.FinancialEventDetailRowBinding


class FinancialEventsDetailAdapter : RecyclerView.Adapter<FinancialEventsDetailAdapter.FinancialEventDetailHolder>() {


    class FinancialEventDetailHolder(val binding: FinancialEventDetailRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var list = ArrayList<FinancialEventModel>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinancialEventDetailHolder {
        val binding =
            FinancialEventDetailRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FinancialEventDetailHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: FinancialEventDetailHolder, position: Int) {
        holder.binding.apply {
            val currentItem = list[position]
            textViewIncomeExpenseName.text = currentItem.eventName
            twFinancialEventAmount.text = currentItem.amount.toString()

            if (currentItem.isExpense){
                iwArrowImage.setImageResource(R.drawable.ic_baseline_expense_arrow)
            }
            else{
                iwArrowImage.setImageResource(R.drawable.ic_outline_income_arrow)
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}