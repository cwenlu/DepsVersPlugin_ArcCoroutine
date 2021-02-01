package com.cwl.common.widget.recyclerview.paging

import androidx.annotation.LayoutRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cwl.common.widget.recyclerview.itemtouch.ItemMoveSwipeCallbackAdapter
import com.cwl.common.widget.recyclerview.multitype2.TypedItemView2
import com.cwl.common.widget.recyclerview.multitype2.TypedItemViewManager2
import com.cwl.common.widget.recyclerview.multitype2.ViewBindingInflate
import java.util.*

/**

 * @Author cwl

 * @Date 2019-08-25 19:44
 */
open abstract class MultiTypePagingAdapter<VH: RecyclerView.ViewHolder,T : Any> (diffCallback: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T,VH>(diffCallback),
    ItemMoveSwipeCallbackAdapter {

    protected val typedItemViewManager= TypedItemViewManager2<VH,T>()


    override fun onBindViewHolder(holder: VH, position: Int) {
        typedItemViewManager.onBindViewHolder(holder,getItem(position),position)
    }

    override fun getItemViewType(position: Int): Int {
        return typedItemViewManager.getItemViewType(getItem(position),position)
    }

    fun register(typedItemView2: TypedItemView2<VH, T>){
        typedItemViewManager.add(typedItemView2)
    }

    fun register(viewType: Int, typedItemView2: TypedItemView2<VH,T>){
        typedItemViewManager.add(viewType,typedItemView2)
    }

    /**
     * 单一样式 isForViewType（eg:比如兼容placeholder，可以自己处理）
     */
     fun register(viewBindingInflate: ViewBindingInflate,enablePlaceHolder:Boolean=false,onBindViewHolder:(VH, T?, Int)->Unit){
        typedItemViewManager.add(object : TypedItemView2<VH,T>(viewBindingInflate) {

            override fun isForViewType(item: T?, position: Int): Boolean = if(enablePlaceHolder) true else item != null

            override fun onBindViewHolder(holder: VH, t: T?, position: Int) =onBindViewHolder(holder,t,position)

        })
    }

    /**
     * 占位布局(单独的注册)
     */
    fun registerPlaceHolder(viewBindingInflate: ViewBindingInflate,onBindViewHolder:(VH,Int)->Unit) {
        typedItemViewManager.add(object : TypedItemView2<VH, T>(viewBindingInflate) {

            override fun isForViewType(item: T?, position: Int): Boolean = item == null

            override fun onBindViewHolder(holder: VH, t: T?, position: Int) =
                onBindViewHolder(holder, position)

        })
    }

    //可以持有PagingData来操作
    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        // TODO: 2021/1/7
//        Collections.swap(items,fromPosition,toPosition)
        notifyItemMoved(fromPosition,toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        // TODO: 2021/1/7
//        (items as? MutableList)?.removeAt(position)
        notifyItemRemoved(position)
    }

}