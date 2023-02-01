package com.leventsurer.manager.tools.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leventsurer.manager.databinding.ResidentRequestRawBinding

class ResidentRequestAdapter : RecyclerView.Adapter<ResidentRequestAdapter.ResidentRequestHolder>() {


    class ResidentRequestHolder(val binding: ResidentRequestRawBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var list = ArrayList<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //Tutucu ilk oluşturulduğunda ne yapılacak
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResidentRequestHolder {
        val binding =
            ResidentRequestRawBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResidentRequestHolder(binding)
    }

    //Bağlanma olduktan sonra ne olacak
    override fun onBindViewHolder(holder: ResidentRequestHolder, position: Int) {
        holder.binding.apply {
            twResidentRequest.text = "Parkı artık bakıma alın!!"
        }
    }

    //Kaç tane olacak
    override fun getItemCount(): Int {
        return 4
    }
}