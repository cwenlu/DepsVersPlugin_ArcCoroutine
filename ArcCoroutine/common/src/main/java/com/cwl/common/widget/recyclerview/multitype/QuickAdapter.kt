package com.cwl.common.widget.recyclerview.multitype

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**

 * @Author cwl

 * @Date 2019-09-18 10:31

 */
//kotlinx.android.extensions 废弃
open class QuickAdapter<T>(items: List<T> = emptyList()):MultiTypeAdapter<CommonViewHolder,T>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        return CommonViewHolder(
            parent,
            typedItemViewManager.get(viewType)?.layoutId!!
        )
    }
}

