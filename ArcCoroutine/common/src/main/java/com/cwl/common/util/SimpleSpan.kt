package com.cwl.common.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.RectF
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.*
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.cwl.common.exts.dp2px
import java.util.*
import kotlin.math.roundToInt


class SimpleSpan internal constructor(private val context: Context, text: CharSequence) : SpannableString(text) {
    private val SPAN_MODE = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    private var rangeList= arrayListOf<IntRange>()


    /**
     * 第一个匹配到的
     */
    fun first(firstMatchStr:String):SimpleSpan{
        rangeList.clear()
        val start = toString().indexOf(firstMatchStr)
        val end=start+firstMatchStr.length
        rangeList.add(IntRange(start,end))
        return this

    }

    /**
     * 最后一个匹配到的
     */
    fun last(lastMatchStr:String):SimpleSpan{
        rangeList.clear()
        val start = toString().lastIndexOf(lastMatchStr)
        val end=start+lastMatchStr.length
        rangeList.add(IntRange(start,end))
        return this
    }

    /**
     * 匹配所有的
     */
    fun all(allMatchStr:String):SimpleSpan{
        rangeList.clear()
        var indexesOf = indexesOf(toString(), allMatchStr)
        indexesOf.forEach {
            rangeList.add(IntRange(it,it+allMatchStr.length))
        }
        return this
    }

    /**
     * 设置一个范围
     */
    fun range(range: IntRange):SimpleSpan{
        rangeList.clear()
        rangeList.add(range)
        return this
    }

    fun ranges(vararg ranges:IntRange):SimpleSpan{
        rangeList.clear()
        rangeList.addAll(ranges)
        return this
    }


    fun size(size:Int,dip:Boolean=true):SimpleSpan{
        setSpan(AbsoluteSizeSpan(size,dip))
        return this
    }

    fun scaleSize(scale:Float):SimpleSpan{
        setSpan(RelativeSizeSpan(scale))
        return this
    }

    fun bold():SimpleSpan{
        setSpan(StyleSpan(Typeface.BOLD))
        return this
    }

    fun italic():SimpleSpan{
        setSpan(StyleSpan(Typeface.ITALIC))
        return this
    }

    /**
     * 设置字体
     */
    fun font(font:String):SimpleSpan{
        setSpan(TypefaceSpan(font))
        return this
    }

    @RequiresApi(28)
    fun font(typeface: Typeface):SimpleSpan{
        setSpan(TypefaceSpan(typeface))
        return this
    }

    fun strikethrough():SimpleSpan{
        setSpan(StrikethroughSpan())
        return this
    }

    fun underline():SimpleSpan{
        setSpan(UnderlineSpan())
        return this
    }

    fun background(@ColorRes colorRes:Int):SimpleSpan{
        var color = ContextCompat.getColor(context, colorRes)
        setSpan(BackgroundColorSpan(color))
        return this
    }

    fun background(@ColorRes bgColorRes:Int, radiusDp: Int, @ColorRes textColor:Int?=null):SimpleSpan{
        var color=ContextCompat.getColor(context,bgColorRes)
        var tc=if(textColor!=null) ContextCompat.getColor(context,textColor) else null
        setSpan(RoundedBackgroundSpan(color,tc,context.dp2px(radiusDp.toFloat())))
        return this
    }

    fun textColor(@ColorRes colorRes:Int):SimpleSpan{
        var color=ContextCompat.getColor(context, colorRes)
        setSpan(ForegroundColorSpan(color))
        return this
    }

    /**
     * 下标
     */
    fun subscript():SimpleSpan{
        setSpan(SubscriptSpan())
        return this
    }

    /**
     * 上标
     */
    fun superscript():SimpleSpan{
        setSpan(SuperscriptSpan())
        return this
    }



    private fun setSpan(what:Any){
        rangeList.forEach {
            setSpan(what,it.first,it.last, SPAN_MODE)
        }
    }


    /**
     * 循环找到所有匹配的索引
     */
    private fun indexesOf(src: String, target: String): List<Int> {
        val positions = ArrayList<Int>()
        var index = src.indexOf(target)
        while (index >= 0) {
            positions.add(index)
            index = src.indexOf(target, index + 1)
        }
        return positions
    }
}


internal class RoundedBackgroundSpan(
    @ColorInt var backgroundColor:Int,
    @ColorInt var textColor:Int?=null,
    var radius: Int=0
) : ReplacementSpan() {
    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: FontMetricsInt?): Int {
        return paint.measureText(text?.subSequence(start,end).toString()).roundToInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val isLeftEdge = x === 0.0f
        val extra1Dp = ((paint as TextPaint).density * 1 + 0.5f).toInt()
        val width = paint.measureText(text?.subSequence(start, end).toString())
        val newTop = (bottom - paint.getFontSpacing() - paint.descent()) as Int + 2 * extra1Dp
        val newBottom = bottom - extra1Dp
        val newLeft = (if (isLeftEdge) x else x - radius) as Int
        val newRight = (if (isLeftEdge) x + width + 2 * radius else x + width + radius) as Int
        val rect = RectF(newLeft.toFloat(), newTop.toFloat(), newRight.toFloat(), newBottom.toFloat())
        paint.setColor(backgroundColor)
        canvas.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), paint)

        if(textColor!=null && text!=null){
            val textX = if (isLeftEdge) x + radius else x
            paint.setColor(textColor!!)
            canvas.drawText(text, start, end, textX, y.toFloat(), paint)
        }
    }


}