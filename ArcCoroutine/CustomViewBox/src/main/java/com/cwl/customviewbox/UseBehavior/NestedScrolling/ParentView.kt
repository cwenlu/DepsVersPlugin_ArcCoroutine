package com.cwl.customviewbox.UseBehavior.NestedScrolling

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat

/**

 * @Author cwl

 * @Date 2021/2/3 14:29

 */
class ParentView(context: Context,attributeSet: AttributeSet):FrameLayout(context,attributeSet) {

    //子类请求滑动startNestedScroll()
    override fun onStartNestedScroll(child: View?, target: View?, nestedScrollAxes: Int): Boolean {
        return true
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        if(dx>0){
            //向右拖动
            if(target.right+dx>width){//说明已经拖到parent右边了
                val consumedDx=target.right+dx-width//超出parent的部分都parent消耗
                ViewCompat.offsetLeftAndRight(this,consumedDx)
                consumed[0]=consumedDx
            }
        }else{
            if(target.left+dx<0){
                val consumedDx=target.left+dx
                ViewCompat.offsetLeftAndRight(this,consumedDx)
                consumed[0]=consumedDx
            }
        }

        if(dy>0){
            if(target.bottom+dy>height){
                val consumedDy=target.bottom+dy-height
                ViewCompat.offsetTopAndBottom(this,consumedDy)
                consumed[1]=consumedDy
            }
        }else{
            if(target.top+dy<0){
                val consumedDy=target.top+dy
                ViewCompat.offsetTopAndBottom(this,consumedDy)
                consumed[1]=consumedDy
            }
        }
    }
}