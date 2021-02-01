package com.cwl.common.widget.recyclerview.multitype2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.android.extensions.LayoutContainer

/**

 * @Author cwl

 * @Date 2019-08-26 15:14

 */


//viewbinding 方式
typealias ViewBindingInflate=(layoutInflater: LayoutInflater,parent: ViewGroup,attachToParent:Boolean)->ViewBinding

class CommonViewHolder2<VB:ViewBinding> private constructor(val viewBinding: VB):RecyclerView.ViewHolder(viewBinding.root) {

    companion object{
        fun <VB:ViewBinding> createVH(viewBindingInflate: ViewBindingInflate,parent: ViewGroup,attachToParent:Boolean=false):CommonViewHolder2<VB>{
            return CommonViewHolder2(viewBindingInflate(LayoutInflater.from(parent.context),parent,attachToParent) as VB)
        }
    }
}

