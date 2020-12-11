package com.cwl.common.widget.recyclerview.multitype

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**

 * @Author cwl

 * @Date 2019-09-04 19:54
 *
 *  header footer 包装
 */
class HeaderFooterAdapter(items: List<Any> = emptyList()) : QuickAdapter(items) {
    companion object {
        const val TYPE_HEADER_VIEW = Int.MIN_VALUE
        const val TYPE_FOOTER_VIEW = Int.MIN_VALUE / 2
    }

    private val headers = arrayListOf<Any>()
    private val footers = arrayListOf<Any>()


    fun <T> addHeaderView(items: List<T>, header: TypedItemView<T>) {
        headers.addAll(items as List<Any>)
        register(headerViewCount() + TYPE_HEADER_VIEW, header)
    }

    fun <T> addHeaderView(
        items: List<T>, @LayoutRes layoutId: Int,
        onBindViewHolder: (holder: CommonViewHolder, t: T, position: Int) -> Unit
    ) {
        addHeaderView(items, object : TypedItemView<T>(layoutId) {
            override fun isForViewType(item: T, position: Int): Boolean = position < items.size

            override fun onBindViewHolder(holder: CommonViewHolder, t: T, position: Int) =
                onBindViewHolder(holder, t, position)

        })
    }

    fun <T> addHeaderView(
        item: T, @LayoutRes layoutId: Int,
        onBindViewHolder: (holder: CommonViewHolder, t: T, position: Int) -> Unit
    ) =
        addHeaderView(arrayListOf(item), layoutId, onBindViewHolder)


    fun <T> addFooterView(items: List<T>, footer: TypedItemView<T>) {
        footers.addAll(items as List<Any>)
        register(footerViewCount() + TYPE_FOOTER_VIEW, footer)
    }

    fun <T> addFooterView(
        items: List<T>, @LayoutRes layoutId: Int,
        onBindViewHolder: (holder: CommonViewHolder, t: T, position: Int) -> Unit
    ) {
        addFooterView(items, object : TypedItemView<T>(layoutId) {
            override fun isForViewType(item: T, position: Int): Boolean = position >= items.size + footerViewCount()

            override fun onBindViewHolder(holder: CommonViewHolder, t: T, position: Int) =
                onBindViewHolder(holder, t, position)

        })
    }

    fun <T> addFooterView(
        item: T, @LayoutRes layoutId: Int,
        onBindViewHolder: (holder: CommonViewHolder, t: T, position: Int) -> Unit
    )=addFooterView(arrayListOf(item), layoutId, onBindViewHolder)


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

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
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

    override fun onViewAttachedToWindow(holder: CommonViewHolder) {
        super.onViewAttachedToWindow(holder)
        (holder.itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)?.apply {
            if (viewAdapterPosition < headerViewCount() || viewAdapterPosition >= items.size + headerViewCount()) {

                isFullSpan = true
            }
        }
    }

}