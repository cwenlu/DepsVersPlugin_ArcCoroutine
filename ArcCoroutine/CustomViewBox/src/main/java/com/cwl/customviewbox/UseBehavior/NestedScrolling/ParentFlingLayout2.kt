package com.cwl.customviewbox.UseBehavior.NestedScrolling

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.OverScroller
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat

/**

 * @Author cwl

 * @Date 2021/2/5 14:33

 */
class ParentFlingLayout2(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet), NestedScrollingParent2{
    private val mScroller: OverScroller = OverScroller(context)
    private val mParentHelper=NestedScrollingParentHelper(this)

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        //不要忘记了返回true，表示可以处理nested
        return true
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int): Boolean {
        return true
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        mParentHelper.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        mParentHelper.onNestedScrollAccepted(child, target, axes)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        mParentHelper.onStopNestedScroll(target, type)
    }

    override fun onStopNestedScroll(target: View) {
        mParentHelper.onStopNestedScroll(target)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {

    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {

    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if(type==ViewCompat.TYPE_NON_TOUCH){
            //只处理fling

            if(scrollX==0 && dx>0 || target.width-scrollX==width && dx<0 || scrollY==0 && dy>0 || target.height-scrollY==height && dy<0){
                //ignore
            }else{
                var ndx=dx
                var ndy=dy
//                //控制滚动边缘
//                if (ndx < 0) {
//                    val range = target.width - width
//                    if (scrollX + ndx < range) {
//                        ndx = range - scrollX
//                    }
//                } else {
//                    if (scrollX + ndx > 0) {
//                        ndx = -scrollX
//                    }
//                }
//
                if (ndy < 0) {
                    val range = target.height - height
                    if (scrollY + ndy < range) {
                        ndy = range - scrollY
                    }
                } else {
                    if (scrollY + ndy > 0) {
                        ndy = -scrollY
                    }
                }
                consumed[0]+=ndx
                consumed[1]+=ndy
//                scrollBy(ndx,ndy)
                scrollBy(0,ndy)
                invalidate()
            }
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {

    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return false
    }

    //view 的top,right,bottom,left 不主动修改是不会变的,都是相对parent
    //view 的x=left+translateX,y=top+translateY  所以在没有修改是不会变的
    //scroller 都是修改的view的scroll值,不影响上面相关属性。上面是修改view本身位置,scroller本质上调整viewport位置
    //scrollTo,velocityX都想象成移动viewport,内容就可以看成反向运动
    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    override fun getNestedScrollAxes(): Int {
        return mParentHelper.nestedScrollAxes
    }

}