package com.cwl.customviewbox.UseBehavior.NestedScrolling

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat

/**

 * @Author cwl

 * @Date 2021/2/3 15:11
    将事件继续分发给parent
 */
class MiddleView(context: Context, attributeSet: AttributeSet): FrameLayout(context,attributeSet) {
    init{
        isNestedScrollingEnabled=true
    }

    override fun onStartNestedScroll(child: View?, target: View?, nestedScrollAxes: Int): Boolean {
        return startNestedScroll(nestedScrollAxes)
    }

    override fun onStopNestedScroll(child: View?) {
        stopNestedScroll()
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        var ndx=dx
        var ndy=dy
        //先向parent分发
        if(dispatchNestedPreScroll(dx,dy,consumed,null)){
            ndx=dx-consumed[0]
            ndy=dy-consumed[1]
        }

        if(ndx>0){
            //向右拖动
            if(target.right+ndx>width){//说明已经拖到parent右边了
                val consumedDx=target.right+ndx-width//超出parent的部分都parent消耗
                ViewCompat.offsetLeftAndRight(this,consumedDx)
                //parent可能消费了，so +=
                consumed[0]+=consumedDx
            }
        }else{
            if(target.left+ndx<0){
                val consumedDx=target.left+ndx
                ViewCompat.offsetLeftAndRight(this,consumedDx)
                consumed[0]+=consumedDx
            }
        }

        if(ndy>0){
            if(target.bottom+ndy>height){
                val consumedDy=target.bottom+ndy-height
                ViewCompat.offsetTopAndBottom(this,consumedDy)
                consumed[1]+=consumedDy
            }
        }else{
            if(target.top+ndy<0){
                val consumedDy=target.top+ndy
                ViewCompat.offsetTopAndBottom(this,consumedDy)
                consumed[1]+=consumedDy
            }
        }
    }

}