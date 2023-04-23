package com.leventsurer.manager.tools.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.leventsurer.manager.R
import com.leventsurer.manager.data.model.UserModel
import com.leventsurer.manager.databinding.ResidentInformationRowBinding
import com.leventsurer.manager.ui.fragments.ResidentsInformationFragmentDirections


class ResidentsInformationAdapter : RecyclerView.Adapter<ResidentsInformationAdapter.ResidentInformationHolder>() {
    private lateinit var context: Context

    class ResidentInformationHolder(val binding: ResidentInformationRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var list = ArrayList<UserModel>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResidentInformationHolder {
        context = parent.context
        val binding =
            ResidentInformationRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResidentInformationHolder(binding)
    }

    override fun onBindViewHolder(holder: ResidentInformationHolder, position: Int) {
        holder.binding.apply {
            val currentItem = list[position]
            twResidentDoorNumber.text = currentItem.doorNumber
            twResidentFullName.text = currentItem.fullName
            if (currentItem.imageLink.isNotEmpty()){
                Glide.with(context).load(currentItem.imageLink).into(iwResidentPhoto)
            }else{
                iwResidentPhoto.setImageResource(R.drawable.default_profile_photo)
            }
        }

        holder.itemView.setOnClickListener{
            moveDetailPage.let {
                if (it != null) {
                    it(list[position])
                }
            }
        }

    }

    private var moveDetailPage: ((userModel: UserModel) -> Unit)? = null
    fun moveDetailPage(f: ((userModel: UserModel) -> Unit)) {
        moveDetailPage = f
    }

    override fun getItemCount(): Int {
        return list.size
    }

}