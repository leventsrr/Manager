package com.leventsurer.manager.tools.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.leventsurer.manager.databinding.ResidentInformationRowBinding
import com.leventsurer.manager.ui.fragments.ResidentsInformationFragmentDirections


class ResidentsInformationAdapter : RecyclerView.Adapter<ResidentsInformationAdapter.ResidentInformationHolder>() {


    class ResidentInformationHolder(val binding: ResidentInformationRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var list = ArrayList<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //Tutucu ilk oluşturulduğunda ne yapılacak
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResidentInformationHolder {
        val binding =
            ResidentInformationRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResidentInformationHolder(binding)
    }

    //Bağlanma olduktan sonra ne olacak
    override fun onBindViewHolder(holder: ResidentInformationHolder, position: Int) {
        holder.binding.apply {
            twResidentDoorNumber.text = "13"
            twResidentFullName.text = "Levent Sürer"

        }

        holder.itemView.setOnClickListener{
            val action = ResidentsInformationFragmentDirections.actionResidentsInformationFragmentToResidentInformationDetailsFragment()
            it?.findNavController()?.navigate(action)
        }

    }

    //Kaç tane olacak
    override fun getItemCount(): Int {
        return 30
    }

}