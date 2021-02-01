package com.cwl.common.widget.recyclerview.paging

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.cwl.common.widget.recyclerview.multitype2.CommonViewHolder2
import com.cwl.common.widget.recyclerview.multitype2.ViewBindingInflate

/**

 * @Author cwl

 * @Date 2019-09-18 10:31

 */

open class QuickPagingAdapter<VB: ViewBinding,T : Any>(val viewBindingInflate: ViewBindingInflate,diffCallback: DiffUtil.ItemCallback<T>):MultiTypePagingAdapter<CommonViewHolder2<VB>,T>(diffCallback) {

    fun register(enablePlaceHolder:Boolean=false,onBindViewHolder: (CommonViewHolder2<VB>, T?, Int) -> Unit) {
        super.register(viewBindingInflate,enablePlaceHolder, onBindViewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder2<VB> {
        return CommonViewHolder2.createVH(viewBindingInflate,parent)
    }
}