package com.cwl.customviewbox.ConstraintHelper

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.ViewAnimationUtils
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.hypot

/**
 * @Author cwl
 * @Date 2021/8/16 09:52
 * @Description Circular Reveal 揭露动画
 */
class CircularRevealHelper @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintHelper(context, attrs, defStyleAttr) {
    override fun updatePostLayout(container: ConstraintLayout?) {
        super.updatePostLayout(container)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val views = getViews(container)
            for (view in views) {
                val createCircularReveal = ViewAnimationUtils.createCircularReveal(
                    view, view.width / 2, view.height / 2, 0f,
                    hypot((view.width / 2).toFloat(), (view.height / 2).toFloat()/*揭露动画结束半径(直角三角形的斜边长)*/)
                )
                createCircularReveal.duration = 3000
                createCircularReveal.start()
            }
        }
    }
}