package com.leventsurer.manager.tools.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventsurer.manager.databinding.ResidentPastRequestRawBinding

class ResidentPastRequestAdapter : RecyclerView.Adapter<ResidentPastRequestAdapter.ResidentPastRequestHolder>() {


    class ResidentPastRequestHolder(val binding: ResidentPastRequestRawBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var list = ArrayList<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //Tutucu ilk oluşturulduğunda ne yapılacak
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResidentPastRequestHolder {
        val binding =
            ResidentPastRequestRawBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResidentPastRequestHolder(binding)
    }

    //Bağlanma olduktan sonra ne olacak
    override fun onBindViewHolder(holder: ResidentPastRequestHolder, position: Int) {
        holder.binding.apply {
            twResidentPastRequest.text = "Parka site dışından izinsiz girişler engellenmeli."
        }
    }

    //Kaç tane olacak
    override fun getItemCount(): Int {
        return 13
    }
}