package com.cwl.customviewbox.UseBehavior.NestedScrolling

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.OverScroller

/**

 * @Author cwl

 * @Date 2021/2/5 14:33

 */
class ParentFlingLayout(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {
    private val mScroller: OverScroller = OverScroller(context)

    override fun onStartNestedScroll(child: View?, target: View?, nestedScrollAxes: Int): Boolean {
        //不要忘记了返回true，表示可以处理nested
        return true
    }

    //view 的top,right,bottom,left 不主动修改是不会变的,都是相对parent
    //view 的x=left+translateX,y=top+translateY  所以在没有修改是不会变的
    //scroller 都是修改的view的scroll值,不影响上面相关属性。上面是修改view本身位置,scroller本质上调整viewport位置
    //scrollTo,velocityX都想象成移动viewport,内容就可以看成反向运动
    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        if(scrollY==0 && velocityY<0 || scrollX==0 && velocityX<0 || target.width-scrollX==width && velocityX>0 || target.height-scrollY==height && velocityY>0){
            return false
        }
        mScroller.fling(
            scrollX,
            scrollY,
            -velocityX.toInt(),
            -velocityY.toInt(),
            target.width - width,
            0,
            target.height - height,
            0
        )
        invalidate()
        return true
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
        }
    }
}