package com.cwl.common.widget.recyclerview.selection

import android.os.Parcelable
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView

/**

 * @Author cwl

 * @Date 2019-09-18 11:19

 */
class ItemKeyProvider4T<T>(var items:List<T>, @Scope mScope: Int=SCOPE_CACHED): ItemKeyProvider<T>(mScope) {
    override fun getKey(position: Int): T? = items[position]

    override fun getPosition(key: T): Int = items.indexOf(key)

}

class ItemDetailsLookup4T<T>(val recyclerView: RecyclerView,var items:List<T>): ItemDetailsLookup<T>(){
    override fun getItemDetails(e: MotionEvent): ItemDetails<T>? {
        var findChildViewUnder: View? = recyclerView.findChildViewUnder(e.x, e.y) ?: return null
        var position = recyclerView.getChildAdapterPosition(findChildViewUnder!!)
        return object : ItemDetails<T>() {
            override fun getSelectionKey(): T? =items[position]

            override fun getPosition(): Int =position

        }
    }

}