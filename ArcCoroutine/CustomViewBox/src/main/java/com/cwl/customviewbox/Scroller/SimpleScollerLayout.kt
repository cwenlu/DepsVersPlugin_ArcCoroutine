package com.cwl.customviewbox.Scroller

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.LinearLayout
import android.widget.OverScroller
import android.widget.ScrollView

/**

 * @Author cwl

 * @Date 2021/2/4 14:20

 */
class SimpleScollerLayout(context: Context, attributeSet: AttributeSet):LinearLayout(
    context,
    attributeSet
) {
    private var mTopView:View?=null
    private var mStickyView:View?=null
    //用sv测试
    private var mScrollableView:/*View*/ScrollView?=null
    private var mVelocityTracker:VelocityTracker?=null
    private var mPreMotionY:Int=0

    private val mScroller:OverScroller
    private val mTouchSlop:Int
    private val mMaximumFlingVelocity:Int
    private val mMinimumFlingVelocity:Int

    init {
        orientation=LinearLayout.VERTICAL

        val vc = ViewConfiguration.get(context)
        mTouchSlop=vc.scaledTouchSlop
        mMaximumFlingVelocity=vc.scaledMaximumFlingVelocity
        mMinimumFlingVelocity=vc.scaledMinimumFlingVelocity

        mScroller= OverScroller(context, DecelerateInterpolator())
    }

    /**
     * @param dy vertical 滚动距离
     * @return >0为可滚动数值,<=0 不可滚动
     */
    private fun canScroll(dy: Int):Int{
        if(dy>0){
            //手势 down,内容up
            return scrollY
        }else{
            return mTopView!!.height-scrollY
        }
    }



    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val y=ev.y.toInt()
        when(ev.action){
            MotionEvent.ACTION_DOWN -> {
                mPreMotionY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = y - mPreMotionY
                mPreMotionY = y
                if (Math.abs(dy) > mTouchSlop) {
                    if (canScroll(dy) > 0) {
                        return true
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(mVelocityTracker==null){
            mVelocityTracker=VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)

        val y=event.y.toInt()
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                mPreMotionY = y
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
                //解决child没有能处理的，冒泡给我们,先说能处理
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = y - mPreMotionY
                mPreMotionY = y
                var range = canScroll(dy)
                if (range > 0) {
                    if (dy > 0) {
                        scrollBy(0, -Math.min(range, dy))
                    } else {
                        scrollBy(0, Math.min(range, -dy))

                    }
                    return true
                }

            }
            MotionEvent.ACTION_UP -> {
                mVelocityTracker!!.computeCurrentVelocity(1000, mMaximumFlingVelocity.toFloat())
                //向下向右为正。内容实际往反方向动
                val yVelocity = mVelocityTracker!!.yVelocity.toInt()
                if (Math.abs(yVelocity) > mMinimumFlingVelocity) {
                    mScroller.fling(0, scrollY, 0, -yVelocity, 0, 0, 0, mTopView!!.height)
                    invalidate()
                }
                mVelocityTracker!!.clear()
            }
            MotionEvent.ACTION_CANCEL -> {
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
                mVelocityTracker!!.clear()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize=MeasureSpec.getSize(widthMeasureSpec)
        val heightMode=MeasureSpec.getMode(heightMeasureSpec)
        val heightSize=MeasureSpec.getSize(heightMeasureSpec)
        measureChild(
            mScrollableView, widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                heightSize - mStickyView!!.measuredHeight,
                heightMode
            )
        )
        setMeasuredDimension(
            widthSize,
            mTopView!!.measuredHeight + mStickyView!!.measuredHeight + mScrollableView!!.measuredHeight
        )
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mTopView=getChildAt(0)
        mStickyView=getChildAt(1)
        mScrollableView= getChildAt(2) as ScrollView?
    }

    override fun computeScroll() {
        super.computeScroll()
        if(mScroller.computeScrollOffset()){
            scrollTo(0, mScroller.currY)
            invalidate()
        }
    }
}