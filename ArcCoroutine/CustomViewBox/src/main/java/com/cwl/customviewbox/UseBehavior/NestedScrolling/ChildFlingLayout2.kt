package com.cwl.customviewbox.UseBehavior.NestedScrolling

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.OverScroller
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChild2
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat

/**

 * @Author cwl

 * @Date 2021/2/4 11:20
 注意要兼容5.0之前的版本必须全部实现,若5.0后,可以只处理新的
 */
class ChildFlingLayout2(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet),NestedScrollingChild2 {
    private var child: View? = null
    private val mScroller: OverScroller = OverScroller(context)
    private var mPreMotionX: Int = 0
    private var mPreMotionY: Int = 0
    private val mVelocityTracker: VelocityTracker = VelocityTracker.obtain()
    private val mChildHelper:NestedScrollingChildHelper= NestedScrollingChildHelper(this)
    private val consumed=IntArray(2)

    init {
        isNestedScrollingEnabled=true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        mVelocityTracker.addMovement(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPreMotionX = x
                mPreMotionY = y
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
                startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL or ViewCompat.SCROLL_AXIS_VERTICAL)
            }
            MotionEvent.ACTION_MOVE -> {
                var dx = mPreMotionX - x
                var dy = mPreMotionY - y
                //控制滚动边缘
                if (dx < 0) {
                    val range = child!!.width - width
                    if (scrollX + dx < range) {
                        dx = range - scrollX
                    }
                } else {
                    if (scrollX + dx > 0) {
                        dx = -scrollX
                    }
                }

                if (dy < 0) {
                    val range = child!!.height - height
                    if (scrollY + dy < range) {
                        dy = range - scrollY
                    }
                } else {
                    if (scrollY + dy > 0) {
                        dy = -scrollY
                    }
                }
                scrollBy(dx, dy)
                mPreMotionX = x
                mPreMotionY = y

            }
            MotionEvent.ACTION_UP -> {
                mVelocityTracker.computeCurrentVelocity(1000)
                val xVelocity = mVelocityTracker.xVelocity
                val yVelocity = mVelocityTracker.yVelocity

                //开始fling nested滚动
                startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL or ViewCompat.SCROLL_AXIS_VERTICAL,ViewCompat.TYPE_NON_TOUCH)

                //后面fling计算用
                mPreMotionX=scrollX
                mPreMotionY=scrollY
                //mScroller.fling(scrollX,scrollY,-xVelocity,-yVelocity,-(width-child!!.width),0,-(height-child!!.height),0)
                //滑动方向（相当于移动视口位置）和内容滚动方向总是相反的
                mScroller.fling(
                    scrollX,
                    scrollY,
                    -xVelocity.toInt(),
                    -yVelocity.toInt(),
                    child!!.width - width,
                    0,
                    child!!.height - height,
                    0
                )
                invalidate()

                mVelocityTracker.clear()
            }
            MotionEvent.ACTION_CANCEL -> {
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
            }
        }
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        //开启nested fling 后这个只一次为true,还没找到原因
        if (mScroller.computeScrollOffset()) {
            val x=mScroller.currX
            val y=mScroller.currY

            var dx=x-mPreMotionX
            var dy=y-mPreMotionY
            //分发fling
            if (dispatchNestedPreScroll(dx,dy,consumed,null,ViewCompat.TYPE_NON_TOUCH)){
                dx-=consumed[0]
                dy-=consumed[1]
                if(dx!=0 && dy!=0){
                    scrollBy(dx,dy)
                    invalidate()
                }
            }else{
                scrollTo(x,y)
                invalidate()
            }
            mPreMotionX=x
            mPreMotionY=y
        }else{
            stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        child = getChildAt(0)
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper.isNestedScrollingEnabled=enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mChildHelper.isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
       return mChildHelper.startNestedScroll(axes,type)
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mChildHelper.startNestedScroll(axes)
    }

    override fun stopNestedScroll(type: Int) {
         mChildHelper.stopNestedScroll(type)
    }

    override fun stopNestedScroll() {
        mChildHelper.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return mChildHelper.hasNestedScrollingParent(type)
    }

    override fun hasNestedScrollingParent(): Boolean {
        return mChildHelper.hasNestedScrollingParent()
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?
    ): Boolean {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow)

    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?
    ): Boolean {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)

    }

    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }
}