package com.cwl.common.widget.recyclerview.multitype2

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding

/**

 * @Author cwl

 * @Date 2019-09-04 19:54
 *
 *  header footer 包装
 *  一般也不需要再定义ViewHolder，这里直接用定义好的了，不再像MultiTypeAdapter那样定义
 *  paging 占位符没处理
 */
class HeaderFooterAdapter2<VB: ViewBinding,T>(viewBindingInflate: ViewBindingInflate, items: List<T> = emptyList()) : QuickAdapter2<VB,T>(viewBindingInflate,items) {
    companion object {
        const val TYPE_HEADER_VIEW = Int.MIN_VALUE
        const val TYPE_FOOTER_VIEW = Int.MIN_VALUE / 2
    }

    private val headers = arrayListOf<T>()
    private val footers = arrayListOf<T>()


    fun addHeaderView(items: List<T>, header: TypedItemView2<CommonViewHolder2<VB>,T>) {
        headers.addAll(items)
        register(headerViewCount() + TYPE_HEADER_VIEW, header)
    }

    fun addHeaderView(
        items: List<T>, viewBindingInflate: ViewBindingInflate,
        onBindViewHolder: (holder: CommonViewHolder2<VB>, t: T, position: Int) -> Unit
    ) {
        addHeaderView(items, object : TypedItemView2<CommonViewHolder2<VB>,T>(viewBindingInflate) {
            override fun isForViewType(item: T?, position: Int): Boolean = position < items.size

            override fun onBindViewHolder(holder: CommonViewHolder2<VB>, t: T?, position: Int) =
                onBindViewHolder(holder, t!!, position)

        })
    }

    fun  addHeaderView(
        item: T, viewBindingInflate: ViewBindingInflate,
        onBindViewHolder: (holder: CommonViewHolder2<VB>, t: T, position: Int) -> Unit
    ) =
        addHeaderView(arrayListOf(item), viewBindingInflate, onBindViewHolder)


    fun  addFooterView(items: List<T>, footer: TypedItemView2<CommonViewHolder2<VB>,T>) {
        footers.addAll(items)
        register(footerViewCount() + TYPE_FOOTER_VIEW, footer)
    }

    fun  addFooterView(
        items: List<T>, viewBindingInflate: ViewBindingInflate,
        onBindViewHolder: (holder: CommonViewHolder2<VB>, t: T, position: Int) -> Unit
    ) {
        addFooterView(items, object : TypedItemView2<CommonViewHolder2<VB>,T>(viewBindingInflate) {
            override fun isForViewType(item: T?, position: Int): Boolean = position >= items.size + footerViewCount()

            override fun onBindViewHolder(holder: CommonViewHolder2<VB>, t: T?, position: Int) =
                onBindViewHolder(holder, t!!, position)

        })
    }

    fun  addFooterView(
        item: T, viewBindingInflate: ViewBindingInflate,
        onBindViewHolder: (holder: CommonViewHolder2<VB>, t: T, position: Int) -> Unit
    )=addFooterView(arrayListOf(item), viewBindingInflate, onBindViewHolder)


    fun headerViewCount() = headers.size

    fun footerViewCount() = footers.size

    override fun getItemCount(): Int {
        return items.size + headerViewCount() + footerViewCount()
    }

    override fun getItemViewType(position: Int): Int {
        if (position < headerViewCount()) {
            return typedItemViewManager.getItemViewType(headers[position], position)
        } else if (position >= items.size + headerViewCount()) {
            return typedItemViewManager.getItemViewType(footers[position - items.size - headerViewCount()], position)
        }
        return super.getItemViewType(position - headerViewCount())
    }

    override fun onBindViewHolder(holder: CommonViewHolder2<VB>, position: Int) {
        if (position < headerViewCount()) {
            return typedItemViewManager.onBindViewHolder(holder, headers[position], position)

        } else if (position >= items.size + headerViewCount()) {
            return typedItemViewManager.onBindViewHolder(
                holder,
                footers[position - items.size - headerViewCount()],
                position
            )
        }
        return super.onBindViewHolder(holder, position - headerViewCount())
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        (recyclerView.layoutManager as? GridLayoutManager)?.apply {
            var oldSpan = spanSizeLookup
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position < headerViewCount() || position >= items.size + headerViewCount()) {
                        return spanCount
                    }
                    return oldSpan.getSpanSize(position)
                }

            }
        }
    }

    override fun onViewAttachedToWindow(holder: CommonViewHolder2<VB>) {
        super.onViewAttachedToWindow(holder)
        (holder.itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)?.apply {
            if (viewAdapterPosition < headerViewCount() || viewAdapterPosition >= items.size + headerViewCount()) {

                isFullSpan = true
            }
        }
    }

}