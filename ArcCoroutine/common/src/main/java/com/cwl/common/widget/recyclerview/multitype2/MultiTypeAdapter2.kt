package com.cwl.common.widget.recyclerview.multitype2

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.cwl.common.widget.recyclerview.itemtouch.ItemMoveSwipeCallbackAdapter
import java.util.*

/**

 * @Author cwl

 * @Date 2019-08-25 19:44

 */
open abstract class MultiTypeAdapter2<VH: RecyclerView.ViewHolder,T> (var items: List<T> = emptyList()) :RecyclerView.Adapter<VH>(),
    ItemMoveSwipeCallbackAdapter {

    protected val typedItemViewManager= TypedItemViewManager2<VH,T>()

    override fun getItemCount(): Int =items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        typedItemViewManager.onBindViewHolder(holder,items.get(position),position)
    }

    override fun getItemViewType(position: Int): Int {
        return typedItemViewManager.getItemViewType(items[position],position)
    }

    fun register(typedItemView2: TypedItemView2<VH,T>){
        typedItemViewManager.add(typedItemView2)
    }

    fun register(viewType: Int, typedItemView2: TypedItemView2<VH,T>){
        typedItemViewManager.add(viewType,typedItemView2)
    }


    /**
     * 单一样式
     */
     fun register(viewBindingInflate: ViewBindingInflate,onBindViewHolder:(VH, T, Int)->Unit){
        typedItemViewManager.add(object : TypedItemView2<VH,T>(viewBindingInflate) {

            override fun isForViewType(item: T?, position: Int): Boolean =true

            override fun onBindViewHolder(holder: VH, t: T?, position: Int) =onBindViewHolder(holder,t!!,position)

        })
    }


    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(items,fromPosition,toPosition)
        notifyItemMoved(fromPosition,toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        (items as? MutableList)?.removeAt(position)
        notifyItemRemoved(position)
    }

}