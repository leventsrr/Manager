package com.leventsurer.manager.tools.helpers

import android.view.View
import com.leventsurer.manager.databinding.CustomHeaderBinding

object HeaderHelper {
    //  source: https://github.com/mehmeteminyildiz/custom_header/blob/master/app/src/main/java/com/mey/includelayout/HeaderHelper.kt
    fun customHeader(
        binding: CustomHeaderBinding,
        title: String,
        startIconVisibility: Boolean = true,
        endIconVisibility: Boolean = false,
        startIcon: Int,
        endIcon: Int,
        startIconClick: (() -> Unit)? = null,
        endIconClick: (() -> Unit)? = null,
    ) {
        binding.apply {
            textViewTitle.text = title
            imageStart.setImageResource(startIcon)
            imageEnd.setImageResource(endIcon)
            imageStart.visibility = if (startIconVisibility) View.VISIBLE else View.GONE
            imageEnd.visibility = if (endIconVisibility) View.VISIBLE else View.GONE

            imageStart.setOnClickListener {
                startIconClick?.let {
                    it()
                }
            }
            imageEnd.setOnClickListener {
                endIconClick?.let {
                    it()
                }
            }
        }
    }
}