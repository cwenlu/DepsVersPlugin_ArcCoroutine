package com.cwl.customviewbox.UseBehavior

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView

/**

 * @Author cwl

 * @Date 2021/2/7 10:48

 */
class HeaderBehavior(context: Context, attributeSet: AttributeSet):
    CoordinatorLayout.Behavior<View>(context,attributeSet) {


    //测试ScrollBehavior跟随依赖
    var py = 0f
    //本质是CoordinatorLayout的onTouchEvent分发
    override fun onTouchEvent(parent: CoordinatorLayout, child: View, ev: MotionEvent): Boolean {
        val y = ev.y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                py = y
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = y - py
                py=y
                child.translationY+=dy
            }
        }
        return true
    }

    //协调滚动
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return (axes and ViewCompat.SCROLL_AXIS_VERTICAL)!=0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
//        child.translationY+=(-dy)
        if(Math.abs(child.translationY-dy)>child.measuredHeight){
            child.translationY=-child.measuredHeight.toFloat()
        }else if (child.translationY-dy>0){
            child.translationY=0f
        }else{
            child.translationY-=dy
            consumed[1]+=dy
        }

    }
}

class ScrollBehavior(context: Context, attributeSet: AttributeSet):
    CoordinatorLayout.Behavior<View>(context,attributeSet){

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return (dependency.layoutParams as? CoordinatorLayout.LayoutParams)?.behavior is HeaderBehavior
    }

    //measure了两次CoordinatorLayout内部存储依赖关系的在每次onMeasure#prepareChildren中会进行重置
    //最后一次相当于把onDependentViewChanged的设置给清了，所以这里不能依赖top,left去设置，只能靠x,y,translateX等
    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {

        child.translationY=dependency.translationY

        //这种方式第一次时由于最后执行onLayoutChild,里面获取的child值还是原始的值,导致位置又定回去了,所以onLayoutChild中处理
//        ViewCompat.offsetTopAndBottom(child,dependency.measuredHeight)
        //使用这个放式可以不重写onLayoutChild
//        child.y=dependency.measuredHeight.toFloat()
        return true
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        val dependency=parent.getDependencies(child).get(0)
        child.layout(0,dependency.measuredHeight,child.measuredWidth,child.measuredHeight+dependency.measuredHeight)
        return true
    }
}


class TitleBehavior(context: Context,attributeSet: AttributeSet):CoordinatorLayout.Behavior<View>(context,attributeSet){
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is NestedScrollView
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        //根据向上滑动距离算出比例
        val fracton=-dependency.translationY/dependency.top
        //算出对应child应移动距离和透明度
        child.translationY=fracton*child.height
        child.alpha=fracton
        return true
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        //设置初始位置
        child.layout(0,-child.measuredHeight,child.measuredWidth,0)
        return true
    }
}
