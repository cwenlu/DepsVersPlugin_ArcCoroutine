package com.cwl.common.widget.recyclerview.multitype

import androidx.annotation.LayoutRes
import androidx.recyclerview.selection.SelectionTracker

/**

 * @Author cwl

 * @Date 2019-08-25 19:16
 *
 * @see {@link StorageStrategy} for supported types.
 * {@link #createStringStorage()},
 *           {@link #createParcelableStorage(Class)},
 *           {@link #createLongStorage()}

 */
abstract class TypedItemView<T>(@LayoutRes val layoutId:Int,val selectionTracker: SelectionTracker<*>?=null) {

    abstract fun isForViewType(item:T,position:Int):Boolean

    abstract fun onBindViewHolder(holder: CommonViewHolder, t:T, position: Int)

}