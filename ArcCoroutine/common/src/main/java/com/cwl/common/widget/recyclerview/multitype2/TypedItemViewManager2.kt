package com.cwl.common.widget.recyclerview.multitype2

import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView

/**

 * @Author cwl

 * @Date 2019-08-25 19:58

 */
class TypedItemViewManager2<VH:RecyclerView.ViewHolder,T> {
    private val typedItemViewPool=SparseArrayCompat<TypedItemView2<VH,T>>()

    /**
     * 返回可用的key
     */
    fun getUsableViewTypeKey():Int{
        var viewTypeKey = typedItemViewPool.size()
        while(typedItemViewPool.containsKey(viewTypeKey)){
            viewTypeKey++
        }
        return viewTypeKey
    }

    /**
     * 获取typedItemView的key
     * note: typedItemView 没有找到会抛出数组越界异常
     */
    @Throws(ArrayIndexOutOfBoundsException::class)
    fun getViewTypeKey(typedItemView2: TypedItemView2<VH,T>):Int=typedItemViewPool.keyAt(typedItemViewPool.indexOfValue(typedItemView2))

    fun add(typedItemView2: TypedItemView2<VH,T>){
        val viewTypeKey=getUsableViewTypeKey()
        typedItemViewPool.put(viewTypeKey,typedItemView2)
    }

    fun add(viewTypeKey:Int, typedItemView2: TypedItemView2<VH,T>){
        if(typedItemViewPool[viewTypeKey]!=null) throw IllegalArgumentException("viewTypeKey $viewTypeKey already exist")
        typedItemViewPool.put(viewTypeKey,typedItemView2)
    }

    fun remove(viewTypeKey:Int){
        typedItemViewPool.remove(viewTypeKey)
    }

    fun remove(typedItemView2: TypedItemView2<VH,T>){
        var indexOfValue = typedItemViewPool.indexOfValue(typedItemView2)
        typedItemViewPool.removeAt(indexOfValue)
    }

    fun get(viewTypeKey:Int)=typedItemViewPool.get(viewTypeKey)


    //------------

    @Throws(IllegalArgumentException::class)
    fun getItemViewType(t:T?,position: Int): Int{
        var size = typedItemViewPool.size()
        for (index in 0 until size){
            var typedItemView = typedItemViewPool.valueAt(index)
            if (typedItemView.isForViewType(t,position)) {
                return typedItemViewPool.keyAt(index)
            }
        }
        throw IllegalArgumentException("not found position=$position TypedItemView")
    }

    fun onBindViewHolder(holder: VH, t:T?, position: Int){
        var size = typedItemViewPool.size()
        for (index in 0 until size){
            var typedItemView = typedItemViewPool.valueAt(index)
            if (typedItemView.isForViewType(t,position)) {
                typedItemView.onBindViewHolder(holder,t,position)
                return
            }
        }
    }
}