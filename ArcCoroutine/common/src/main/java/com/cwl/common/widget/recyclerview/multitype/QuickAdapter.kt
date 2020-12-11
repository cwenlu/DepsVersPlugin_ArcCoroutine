package com.cwl.common.widget.recyclerview.multitype

import android.view.ViewGroup

/**

 * @Author cwl

 * @Date 2019-09-18 10:31

 */
open class QuickAdapter(items: List<Any> = emptyList()):MultiTypeAdapter<CommonViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        return CommonViewHolder(
            parent,
            typedItemViewManager.get(viewType)?.layoutId!!
        )
    }
}