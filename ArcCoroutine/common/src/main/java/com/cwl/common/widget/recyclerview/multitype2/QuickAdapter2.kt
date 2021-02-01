package com.cwl.common.widget.recyclerview.multitype2

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**

 * @Author cwl

 * @Date 2019-09-18 10:31

 */

open class QuickAdapter2<VB: ViewBinding,T>(val viewBindingInflate: ViewBindingInflate, items: List<T> = emptyList()):MultiTypeAdapter2<CommonViewHolder2<VB>,T>(items) {

     fun register(onBindViewHolder: (CommonViewHolder2<VB>, T, Int) -> Unit) {
        super.register(viewBindingInflate, onBindViewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder2<VB> {
        return CommonViewHolder2.createVH(viewBindingInflate,parent)
    }
}