package com.leventsurer.manager.tools.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.leventsurer.manager.data.model.UserModel
import com.leventsurer.manager.databinding.ResidentInformationRowBinding
import com.leventsurer.manager.ui.fragments.ResidentsInformationFragmentDirections


class ResidentsInformationAdapter : RecyclerView.Adapter<ResidentsInformationAdapter.ResidentInformationHolder>() {
    private lateinit var context: Context

    class ResidentInformationHolder(val binding: ResidentInformationRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    var list = ArrayList<UserModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    //Tutucu ilk oluşturulduğunda ne yapılacak
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResidentInformationHolder {
        context = parent.context
        val binding =
            ResidentInformationRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResidentInformationHolder(binding)
    }

    //Bağlanma olduktan sonra ne olacak
    override fun onBindViewHolder(holder: ResidentInformationHolder, position: Int) {
        holder.binding.apply {
            val currentItem = list[position]
            twResidentDoorNumber.text = currentItem.doorNumber
            twResidentFullName.text = currentItem.fullName
            Glide.with(context).load(currentItem.imageLink).into(iwResidentPhoto)

        }

        holder.itemView.setOnClickListener{
            moveDetailPage.let {
                if (it != null) {
                    it(list[position])
                }
            }
           /* val action = ResidentsInformationFragmentDirections.actionResidentsInformationFragmentToResidentInformationDetailsFragment()
            it?.findNavController()?.navigate(action)*/
        }

    }

    //move to product's detail page
    private var moveDetailPage: ((userModel: UserModel) -> Unit)? = null
    fun moveDetailPage(f: ((userModel: UserModel) -> Unit)) {
        Log.e("TAG", "setOnClickListenerCustom: ")
        moveDetailPage = f
    }

    //Kaç tane olacak
    override fun getItemCount(): Int {
        return list.size
    }

}