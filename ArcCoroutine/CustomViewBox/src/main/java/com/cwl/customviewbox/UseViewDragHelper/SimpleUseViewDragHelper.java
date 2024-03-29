package com.cwl.customviewbox.UseViewDragHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.customview.widget.ViewDragHelper;


/**
 * @Author cwl
 * @Date 2021/1/28 15:56
 * 简单示例
 */
public class SimpleUseViewDragHelper extends ViewGroup {

    private View mPanelView;

    private View mMainView;

    private ViewDragHelper mViewDragHelper;

    public SimpleUseViewDragHelper(Context context) {
        this(context,null);
    }

    public SimpleUseViewDragHelper(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SimpleUseViewDragHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewDragHelper=ViewDragHelper.create(this,1f/*敏感系数1是正常，越大约敏感*/,new DragHelperCallback());
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode!=MeasureSpec.EXACTLY && widthMode!=MeasureSpec.AT_MOST){
            throw new IllegalArgumentException("Width must have an exact value or MATCH_PARENT");
        }
        if(heightMode!=MeasureSpec.EXACTLY && heightMode!=MeasureSpec.AT_MOST){
            throw new IllegalStateException("Height must have an exact value or MATCH_PARENT");
        }

        final int childCount=getChildCount();
        int layoutWidth=widthSize-getPaddingLeft()-getPaddingRight();
        int layoutHeight=heightSize-getPaddingTop()-getPaddingBottom();

        for (int i = 0; i < childCount; i++) {
            final View child=getChildAt(i);
            final MarginLayoutParams lp= (MarginLayoutParams) child.getLayoutParams();

            int width=layoutWidth;
            int height=layoutHeight;
            if(child==mMainView){
                width-=lp.leftMargin+lp.rightMargin;
            }else if(child==mPanelView){
                height-=lp.topMargin;
            }

            //没有特殊规则,可以直接使用getChildMeasureSpec,进行parent-child尺寸信息交换
            int childWidthSpec;
            if(lp.width==LayoutParams.WRAP_CONTENT){
                //child说我包裹自己
                //parent(改为AT_MOST)说最大不超过width
                childWidthSpec=MeasureSpec.makeMeasureSpec(width,MeasureSpec.AT_MOST);
            }else if(lp.width==LayoutParams.MATCH_PARENT){
                //child说我充满parent
                //parent(改为EXACTLY),说就是width这么大
                childWidthSpec=MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY);
            }else{
                //直接满足child要求
                childWidthSpec=MeasureSpec.makeMeasureSpec(lp.width,MeasureSpec.EXACTLY);
            }

            int childHeightSpec;
            if(lp.height==LayoutParams.WRAP_CONTENT){
                childHeightSpec=MeasureSpec.makeMeasureSpec(height,MeasureSpec.AT_MOST);
            }else{
                if(lp.height!=LayoutParams.MATCH_PARENT){
                    height=lp.height;
                }
                childHeightSpec=MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
            }
            //内部会考虑自己的pading,内部使用getChildMeasureSpec
            measureChild(child,childWidthSpec,childHeightSpec);
            //measureChildren 让自己的所有孩子进行测量，内部调用measureChild
        }

        setMeasuredDimension(widthSize,heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int paddingLeft=getPaddingLeft();
        final int paddingTop=getPaddingTop();

        final int childCount=getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child=getChildAt(i);
            final MarginLayoutParams lp= (MarginLayoutParams) child.getLayoutParams();

            final int childHeight=child.getMeasuredHeight();

            int childTop=paddingTop;
            final int childBottom=childTop+childHeight;
            final int childLeft=paddingLeft+lp.leftMargin;
            final int childRight=childLeft+child.getMeasuredWidth();

            child.layout(childLeft,childTop,childRight,childBottom);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    private class DragHelperCallback extends ViewDragHelper.Callback{

        //决定哪个view可以触发拖动
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child==mPanelView;
        }

        //决定Vertical方向最终移动到的位置
        //在边缘模式时 top值连续<0,这就可能多次返回0 导致shouldInterceptTouchEvent中直接cancel
        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            int min=0;
            int max=getMeasuredHeight();
            //必须 min=<top<=max
            return Math.min(Math.max(min,top),max);
        }

        //如果mPanelView触摸位置下没有可处理事件的view,则直接执行了onTouchEvent(mViewDragHelper.processTouchEvent),可以拖动
        //如果有,执行onInterceptTouchEvent(mViewDragHelper.shouldInterceptTouchEvent) 则必须重写这个方法，内部调用这个方法决定是否拦截
        //vertical 方向的拖动范围
        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return getMeasuredHeight();
        }

        //在寻找当前触摸点下的子View时会调用此方法，寻找到的View会提供给tryCaptureViewForDrag()来尝试捕获。
        //如果需要改变子View的遍历查询顺序可改写此方法，例如让下层的View优先于上层的View被选中。
        @Override
        public int getOrderedChildIndex(int index) {
            return super.getOrderedChildIndex(index);
        }

        //边缘触摸触发拖动开始时回调 需用mViewDragHelper.setEdgeTrackingEnabled 开启
        //edgeFlags : 当前触摸的flag 有: EDGE_LEFT,EDGE_TOP,EDGE_RIGHT,EDGE_BOTTOM
        //onEdgeTouched 比 onEdgeDragStarted 先执行
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
            //边缘情况下，没法直接tryCaptureView，需要我们手动Capture
            mViewDragHelper.captureChildView(mPanelView,pointerId);
            System.out.println("onEdgeDragStarted");
        }

        //如果parentView订阅了边缘触摸,则如果有边缘触摸就回调的接口 ViewDragHelper.EDGE_LEFT
        //edgeFlags : 当前触摸的flag 有: EDGE_LEFT,EDGE_TOP,EDGE_RIGHT,EDGE_BOTTOM
        //pointerId : 用来描述边缘触摸操作的id
        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            System.out.println("onEdgeTouched");
        }

        //是否锁定该边缘的触摸,默认返回false,返回true表示锁定
        @Override
        public boolean onEdgeLock(int edgeFlags) {
            return super.onEdgeLock(edgeFlags);
        }

        //当View的拖拽状态改变时回调,state为STATE_IDLE,STATE_DRAGGING,STATE_SETTLING的一种
        //STATE_IDLE:　当前未被拖拽
        //STATE_DRAGGING：正在被拖拽
        //STATE_SETTLING:　被拖拽后需要被安放到一个位置中的状态
        @Override
        public void onViewDragStateChanged(int state) {}

        //当View被拖拽位置发生改变时回调
        //changedView ：被拖拽的View
        //left : 被拖拽后View的left边缘坐标
        //top : 被拖拽后View的top边缘坐标
        //dx : 拖动的x偏移量
        //dy : 拖动的y偏移量
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            //eg:可以同步改动一些view的位置，大小等
        }

        //当一个View被捕获到准备开始拖动时回调,
        //capturedChild : 捕获的View
        //activePointerId : 对应的PointerId
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {}

        //当被捕获拖拽的View被释放是回调
        //releasedChild : 被释放的View
        //xvel : 释放View的x方向上的加速度
        //yvel : 释放View的y方向上的加速度
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {}
    }

    //xml映射完View执行
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount()!=2){
            throw new IllegalArgumentException("panel layout must have exactly 2 children");
        }
        mMainView=getChildAt(0);
        mPanelView=getChildAt(1);

    }

    //只要child没有指定LayoutParams，会获取这个作为默认
    //eg: addView时没指定LayoutParams
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    //通过layout xml 解析后生成LayoutParams
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    //parent 提供了LayoutParams,最上面是ViewGroup的LayoutParams
    //这里可以实现自己的LayoutParams提供给child
    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}

//shouldInterceptTouchEvent：
//
//        DOWN:
//        getOrderedChildIndex(findTopChildUnder)
//        ->onEdgeTouched
//
//        MOVE:
//        getOrderedChildIndex(findTopChildUnder)
//        ->getViewHorizontalDragRange &
//        getViewVerticalDragRange(checkTouchSlop)(MOVE中可能不止一次)
//        ->clampViewPositionHorizontal&
//        clampViewPositionVertical
//        ->onEdgeDragStarted
//        ->tryCaptureView
//        ->onViewCaptured
//        ->onViewDragStateChanged
//
//processTouchEvent:
//
//        DOWN:
//        getOrderedChildIndex(findTopChildUnder)
//        ->tryCaptureView
//        ->onViewCaptured
//        ->onViewDragStateChanged
//        ->onEdgeTouched
//        MOVE:
//        ->STATE==DRAGGING:dragTo
//        ->STATE!=DRAGGING:
//        onEdgeDragStarted
//        ->getOrderedChildIndex(findTopChildUnder)
//        ->getViewHorizontalDragRange&
//        getViewVerticalDragRange(checkTouchSlop)
//        ->tryCaptureView
//        ->onViewCaptured
//        ->onViewDragStateChanged