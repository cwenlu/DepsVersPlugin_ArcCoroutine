package com.cwl.common.widget.recyclerview.itemtouch

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**

 * @Author cwl

 * @Date 2019-09-08 14:57

 */
open class SimpleItemTouchHelperCallback(var itemMoveSwipeCallbackAdapter: ItemMoveSwipeCallbackAdapter):ItemTouchHelper.Callback() {
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if(recyclerView.layoutManager is GridLayoutManager){
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags=0
            return makeMovementFlags(dragFlags,swipeFlags)
        }else{
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags=ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(dragFlags,swipeFlags)
        }

    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if(viewHolder.itemViewType!=target.itemViewType) return false
        return itemMoveSwipeCallbackAdapter.onItemMove(viewHolder.adapterPosition,target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        itemMoveSwipeCallbackAdapter.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if(actionState!=ItemTouchHelper.ACTION_STATE_IDLE && viewHolder is ItemTouchStartEndCallbackAdapter){
            (viewHolder as ItemTouchStartEndCallbackAdapter).onItemSelected()
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        (viewHolder as? ItemTouchStartEndCallbackAdapter)?.onItemClear()
    }
}

/**
 * 监听回调 {@link ItemTouchHelper.Callback}处理移动和删除
 */
interface ItemMoveSwipeCallbackAdapter {

    fun onItemMove(fromPosition:Int,toPosition:Int):Boolean

    fun onItemDismiss(position:Int)
}

/**
 * drag or swipe 的开始和结束
 * ViewHolder 可实现此接口，（CommonViewHolder 实现，MultiTypeAdapter重写onCreateViewHolder）
 */
interface ItemTouchStartEndCallbackAdapter{

    fun onItemSelected()

    fun onItemClear()
}