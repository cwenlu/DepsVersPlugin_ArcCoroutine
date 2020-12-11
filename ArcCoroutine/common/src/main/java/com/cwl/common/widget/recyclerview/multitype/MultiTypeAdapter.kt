package com.cwl.common.widget.recyclerview.multitype

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.cwl.common.widget.recyclerview.itemtouch.ItemMoveSwipeCallbackAdapter
import java.util.*

/**

 * @Author cwl

 * @Date 2019-08-25 19:44

 */
open abstract class MultiTypeAdapter<VH:CommonViewHolder> (var items: List<Any> = emptyList()) :RecyclerView.Adapter<VH>(),
    ItemMoveSwipeCallbackAdapter {

    protected val typedItemViewManager= TypedItemViewManager<Any>()

    override fun getItemCount(): Int =items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        typedItemViewManager.onBindViewHolder(holder,items.get(position),position)
    }

    override fun getItemViewType(position: Int): Int {
        return typedItemViewManager.getItemViewType(items[position],position)
    }

    fun <T> register(typedItemView: TypedItemView<T>){
        typedItemViewManager.add(typedItemView as TypedItemView<Any>)
    }

    fun <T> register(viewType: Int,typedItemView: TypedItemView<T>){
        typedItemViewManager.add(viewType,typedItemView as TypedItemView<Any>)
    }


    /**
     * 单一样式
     */
    fun <T> register(@LayoutRes layoutId:Int,onBindViewHolder:(CommonViewHolder, T, Int)->Unit){
        typedItemViewManager.add(object : TypedItemView<T>(layoutId) {

            override fun isForViewType(item: T, position: Int): Boolean =true

            override fun onBindViewHolder(holder: CommonViewHolder, t: T, position: Int) =onBindViewHolder(holder,t,position)

        } as TypedItemView<Any>)
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