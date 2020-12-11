package com.cwl.common.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cwl.common.R


abstract class UniversalItemDecoration : RecyclerView.ItemDecoration() {

    private val decorations = SparseArrayCompat<Decoration>()

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childSize = parent.childCount
        for (i in 0 until childSize) {

            val child = parent.getChildAt(i)
            val decoration = decorations.get(parent.getChildAdapterPosition(child)) ?: continue
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams

            //view的上下左右包括 Margin
            val bottom = child.bottom + layoutParams.bottomMargin
            val left = child.left - layoutParams.leftMargin
            val right = child.right + layoutParams.rightMargin
            val top = child.top - layoutParams.topMargin

            //下面的
            if (decoration.bottom != 0)
                decoration.drawItemOffsets(c, left - decoration.left, bottom, right + decoration.right, bottom + decoration.bottom)
            //上面的
            if (decoration.top != 0)
                decoration.drawItemOffsets(c, left - decoration.left, top - decoration.top, right + decoration.right, top)
            //左边的
            if (decoration.left != 0)
                decoration.drawItemOffsets(c, left - decoration.left, top, left, bottom)
            //右边的
            if (decoration.right != 0)
                decoration.drawItemOffsets(c, right, top, right + decoration.right, bottom)

        }

    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        //获取position
        var position = parent.getChildAdapterPosition(view)
        var sapnIndex:Int?=null
        (parent.layoutManager as? StaggeredGridLayoutManager)?.apply {
            sapnIndex=(view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
        }
        //获取调用者返回的Decoration
        val decoration = getItemOffsets(position,sapnIndex)
        decorations.put(position,decoration)
        if (decoration != null) {
            //偏移量设置给item
            outRect.set(decoration!!.left, decoration!!.top, decoration!!.right, decoration!!.bottom)

        }

    }

    /***
     * 需调用者返回分割线对象  上下左右 和颜色值
     * @param position
     * @return
     */
    abstract fun getItemOffsets(position: Int,spanIndex:Int?=null): Decoration?

    /**
     * 分割线
     */
    abstract class Decoration {

        var left: Int = 0
        var right: Int = 0
        var top: Int = 0
        var bottom: Int = 0

        /**
         * 根据偏移量设定的 当前的线在界面中的坐标
         *
         * @param leftZ
         * @param topZ
         * @param rightZ
         * @param bottomZ
         */
        abstract fun drawItemOffsets(c: Canvas, leftZ: Int, topZ: Int, rightZ: Int, bottomZ: Int)

    }

    class ColorDecoration : Decoration() {

        private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
        @ColorInt
        var decorationColor = -1

        init {
            mPaint.color=Color.TRANSPARENT
            mPaint.style = Paint.Style.FILL
        }

        override fun drawItemOffsets(c: Canvas, leftZ: Int, topZ: Int, rightZ: Int, bottomZ: Int) {
            if (decorationColor != -1) {
                mPaint.color = decorationColor
            }
            c.drawRect(leftZ.toFloat(), topZ.toFloat(), rightZ.toFloat(), bottomZ.toFloat(), mPaint)
        }

    }


}