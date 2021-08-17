package com.cwl.common.widget.recyclerview

import android.os.Parcelable
import android.view.*
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.ItemKeyProvider.SCOPE_CACHED
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.*
import com.cwl.common.util.UniversalItemDecoration
import com.cwl.common.widget.recyclerview.multitype.MultiTypeAdapter
import com.cwl.common.widget.recyclerview.multitype.HeaderFooterAdapter
import com.cwl.common.widget.recyclerview.itemtouch.ItemMoveSwipeCallbackAdapter
import com.cwl.common.widget.recyclerview.itemtouch.SimpleItemTouchHelperCallback
import com.cwl.common.widget.recyclerview.multitype2.HeaderFooterAdapter2
import com.cwl.common.widget.recyclerview.multitype2.MultiTypeAdapter2
import com.cwl.common.widget.recyclerview.selection.ItemDetailsLookup4T
import com.cwl.common.widget.recyclerview.selection.ItemKeyProvider4T


/**

 * @Author cwl

 * @Date 2019-08-30 20:01

 */

// https://blog.csdn.net/u014651216/article/details/53256985
interface OnItemClickListener {
    fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int)
    fun onItemLongClick(viewHolder: RecyclerView.ViewHolder, position: Int)
}

fun RecyclerView.onItemClick(onItemClick: (RecyclerView.ViewHolder, Int) -> Unit) {
    addItemClickListener(object : OnItemClickListener {

        override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
            onItemClick.invoke(viewHolder, position)
        }

        override fun onItemLongClick(viewHolder: RecyclerView.ViewHolder, position: Int) {

        }
    })
}

fun RecyclerView.onItemLongClick(onItemLongClick: (RecyclerView.ViewHolder, Int) -> Unit) {
    addItemClickListener(object : OnItemClickListener {

        override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
        }

        override fun onItemLongClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
            onItemLongClick(viewHolder, position)
        }
    })
}

fun RecyclerView.addItemClickListener(listener: OnItemClickListener) {

    val gestureDetector = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val childView = findChildViewUnder(e.x, e.y)
            if (childView != null) {
                listener.onItemClick(getChildViewHolder(childView), getChildAdapterPosition(childView))
            }
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            super.onLongPress(e)
            val childView = findChildViewUnder(e.x, e.y)
            if (childView != null) {
                listener.onItemLongClick(getChildViewHolder(childView), getChildAdapterPosition(childView))
            }
        }
    })

    this.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            gestureDetector.onTouchEvent(e)
            return false
        }

    })
}

fun RecyclerView.onItemClick2(onItemClick: (RecyclerView.ViewHolder, Int) -> Unit) {
    addItemClickListener2(object : OnItemClickListener {
        override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
            onItemClick.invoke(viewHolder, position)
        }

        override fun onItemLongClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
        }
    })
}

fun RecyclerView.onItemLongClick2(onItemLongClick: (RecyclerView.ViewHolder, Int) -> Unit) {
    addItemClickListener2(object : OnItemClickListener {
        override fun onItemClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
        }

        override fun onItemLongClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
            onItemLongClick.invoke(viewHolder, position)
        }
    })
}

fun RecyclerView.addItemClickListener2(listener: OnItemClickListener) {
    val attachListener=object:RecyclerView.OnChildAttachStateChangeListener{
        override fun onChildViewAttachedToWindow(view: View) {
            var holder = getChildViewHolder(view)
            view.setOnClickListener {
                listener.onItemClick(holder, holder.bindingAdapterPosition)
            }

            view.setOnLongClickListener {
                listener.onItemLongClick(holder, holder.bindingAdapterPosition)
                true
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {
        }

    }
    addOnChildAttachStateChangeListener(attachListener)
}

/**
 * 处理item 或者内部view事件
 */
fun RecyclerView.addItemActionProcessListener(attachStateChange:(viewHolder: RecyclerView.ViewHolder, position: Int)->Unit){
    val attachListener=object:RecyclerView.OnChildAttachStateChangeListener{
        override fun onChildViewAttachedToWindow(view: View) {
//            attachStateChange(getChildViewHolder(view), getChildAdapterPosition(view)) //等同absoluteAdapterPosition
            var holder = getChildViewHolder(view)

            //如果你没有使用MergeAdapter，那么getBindingAdapterPosition()和getAbsoluteAdapterPosition()方法的效果是一模一样的。
            //如果你使用了MergeAdapter，getBindingAdapterPosition()得到的是元素位于当前绑定Adapter的位置，而getAbsoluteAdapterPosition()方法得到的是元素位于合并后Adapter的绝对位置
            attachStateChange(holder,holder.bindingAdapterPosition)
//            attachStateChange(holder,holder.absoluteAdapterPosition)
        }

        override fun onChildViewDetachedFromWindow(view: View) {
        }

    }
    addOnChildAttachStateChangeListener(attachListener)
}

/**
 * 设置layoutmanager
 */
fun RecyclerView.setupLayoutManager(
    spanCount: Int = 0,
    isStaggered: Boolean = false, @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL
): RecyclerView {
    layoutManager = LinearLayoutManager(context, orientation, false)
    if (isStaggered) {
        layoutManager = StaggeredGridLayoutManager(spanCount, orientation)
    } else if (spanCount != 0) {
        layoutManager = GridLayoutManager(context, spanCount, orientation, false)
    }

    return this
}

/**
 * @inclusiveStart 是否绘制开始的分割线
 * @inclusiveEnd 是否绘制结束的分割线
 */
fun RecyclerView.divider(
    @Px gap: Int,@ColorInt color: Int,inclusiveStart: Boolean = false,
    inclusiveEnd: Boolean = false
) {
    addItemDecoration(object : UniversalItemDecoration() {
        override fun getItemOffsets(position: Int,spanIndex:Int?): Decoration =
            ColorDecoration().apply {
                decorationColor = color
                if (inclusiveStart && position == 0) top = gap
                bottom = gap
                if (!inclusiveEnd && position == adapter?.itemCount ?: 0 - 1) bottom = 0
            }
    })
}

/**
 * 间距全等
 */
fun RecyclerView.gridDivider(@Px gap:Int,@ColorInt color: Int?=null)=gridDivider(color,gap,gap,gap,gap,gap)

/**
 * @degeGap 4周的距离
 */
fun RecyclerView.gridDivider(@Px gap:Int,@ColorInt color: Int?=null,@Px degeGap:Int)=gridDivider(color,gap,gap,degeGap,degeGap,degeGap)

/**
 * @bottom 对瀑布流不起作用
 */
fun RecyclerView.gridDivider(@ColorInt color: Int?=null, @Px rowGap: Int, @Px columnGap: Int, @Px leftAndRight: Int, @Px top: Int, @Px bottom: Int?=null) {
    var spanCount=0
    var orientation=RecyclerView.VERTICAL
    var staggered=false
    (layoutManager as? GridLayoutManager)?.apply {
        spanCount=this.spanCount
        orientation=this.orientation
    }?:(layoutManager as? StaggeredGridLayoutManager)?.apply {
        spanCount=this.spanCount
        orientation=this.orientation
        staggered=true
    }?:throw TypeCastException("LayoutManager isn't GridLayoutManager or StaggeredGridLayoutManager==null")


    addItemDecoration(object : UniversalItemDecoration() {
        override fun getItemOffsets(position: Int,spanIndex:Int?): Decoration? {
            var headerCount=0
            //直接简单修改兼容新的viewbinding
            if(adapter is MultiTypeAdapter<*, *>){
                (adapter as? HeaderFooterAdapter<*>)?.apply {
                    headerCount=headerViewCount()
                    if(position<headerViewCount() || position>=items.size+headerViewCount()){
                        return null
                    }
                }
                return createColorDecoration(position-headerCount,spanIndex,(adapter as MultiTypeAdapter<*, *>).items.size)
            }else{
                (adapter as? HeaderFooterAdapter2<*,*>)?.apply {
                    headerCount=headerViewCount()
                    if(position<headerViewCount() || position>=items.size+headerViewCount()){
                        return null
                    }
                }
                return createColorDecoration(position-headerCount,spanIndex,(adapter as MultiTypeAdapter2<*, *>).items.size)
            }

        }


        fun createColorDecoration(position: Int,spanIndex:Int?,totalCount:Int)=UniversalItemDecoration.ColorDecoration().apply {
            if(color!=null) decorationColor = color
            var pos=spanIndex?:position
            if(orientation==RecyclerView.VERTICAL){

                if (pos % spanCount == 0){//first column
                    left=leftAndRight
                    right=columnGap

                }else if((pos+1)%spanCount==0){//last column
                    right=leftAndRight

                }else{
                    right=columnGap
                }

                if(!staggered){
                    //计算行数
                    val lines = if (totalCount % spanCount == 0) totalCount / spanCount else totalCount / spanCount + 1

                    if(position<spanCount){//first row
                        this.top=top
                        this.bottom=rowGap
                    }else if(position/spanCount+1==lines){//last row
                        if(bottom!=null){
                            this.bottom=bottom
                        }
                    }else{
                        this.bottom=rowGap
                    }
                }else{
                    if(position<spanCount) {//first row
                        this.top=top
                    }
                    this.bottom=rowGap
                }

            }else{

                //todo 水平方向
            }


        }


    })

}


/**
 * 设置空布局
 */
fun RecyclerView.setEmptyView(view: View){

    fun empty(){
        val visible=adapter?.itemCount?:0==0
        view.visibility=if(visible) View.VISIBLE else View.GONE
    }

    var globalLayoutListener:ViewTreeObserver.OnGlobalLayoutListener?=null
    globalLayoutListener= ViewTreeObserver.OnGlobalLayoutListener {
        (view.parent as? ViewGroup)?.removeView(view)
        (parent as? ViewGroup)?.addView(view, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
        viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)

    }
    viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)

    adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            super.onChanged()
            empty()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            super.onItemRangeChanged(positionStart, itemCount)
            empty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            empty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            super.onItemRangeRemoved(positionStart, itemCount)
            empty()
        }
    })
}


fun RecyclerView.setupItemTouchHelper(callback:ItemTouchHelper.Callback):ItemTouchHelper{
    val itemTouchHelper=ItemTouchHelper(callback)
    itemTouchHelper.attachToRecyclerView(this)
    return itemTouchHelper
}

/**
 * @see ItemTouchAdapter
 */
fun RecyclerView.setupItemTouchHelper(itemMoveSwipeCallbackAdapter: ItemMoveSwipeCallbackAdapter):ItemTouchHelper =
    setupItemTouchHelper(SimpleItemTouchHelperCallback(itemMoveSwipeCallbackAdapter))

/**
 *   T is Long,String,Parcelable
 *   withSelectionPredicate 定制选择策略，默认的所有可选，数据移除了，选择的数据也不会变化
 */
fun <T> RecyclerView.createSelectionTracker(items:List<T>,storageStrategy: StorageStrategy<T>,@ItemKeyProvider.Scope mScope: Int=SCOPE_CACHED,selectionId:kotlin.String="selection")=
    SelectionTracker.Builder<T>(
    selectionId,
    this,
    ItemKeyProvider4T<T>(items,mScope),
    ItemDetailsLookup4T<T>(this,items), storageStrategy
).withSelectionPredicate(SelectionPredicates.createSelectAnything()).build()



fun RecyclerView.setupSnapHelper(snapHelper: SnapHelper){
    snapHelper.attachToRecyclerView(this)
}

/**
 * 滚动到底部回调
 */
fun RecyclerView.atTheBottom(block: (Boolean) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            block(!canScrollVertically(1))
        }
    })
}