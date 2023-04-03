package com.leventsurer.manager.tools.helpers

import com.leventsurer.manager.databinding.MyChipCardBinding

object ChipCardHelper {
    fun customChip(
        binding:MyChipCardBinding,
        text:String,
        icon:Int,
        chipOnClickListener:(()->Unit)? = null
    ){
        binding.apply {
            twCardText.text = text
            iwCardImage.setImageResource(icon)
            cwMyChip.setOnClickListener {
                if (chipOnClickListener != null) {
                    chipOnClickListener()
                }
            }
        }
    }
}