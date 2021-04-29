package com.cwl.arccoroutine.test.databinding

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

/**

 * @Author cwl

 * @Date 2021/4/28 10:20

 */

@BindingAdapter("bindClick")
fun bindingClick(view: View,actionListener: ActionListener){
    view.setOnClickListener {
        actionListener.onClick(it)
    }
}
