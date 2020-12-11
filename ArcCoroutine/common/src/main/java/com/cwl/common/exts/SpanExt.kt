package com.cwl.common.exts

import androidx.annotation.ColorRes
import com.cwl.common.util.ContextProvider
import com.cwl.common.util.SimpleSpan


fun CharSequence.toSimpleSpan() = SimpleSpan(ContextProvider.appCtx!!, this)
/**
 * 指定尺寸(绝对值)，颜色
 */
fun CharSequence.sizeColor(range: IntRange, size: Int? = null, @ColorRes colorRes: Int? = null) =
    toSimpleSpan().range(range).apply {
        if (size != null) size(size)
        if (colorRes != null) textColor(colorRes)
    }

fun CharSequence.sizeColor(str: String, size: Int? = null, @ColorRes colorRes: Int? = null) =
    toSimpleSpan().first(str).apply {
        if (size != null) size(size)
        if (colorRes != null) textColor(colorRes)
    }

/**
 * 指定尺寸(相对值)，颜色
 */
fun CharSequence.scaleSizeColor(range: IntRange, size: Float? = null, @ColorRes colorRes: Int? = null) =
    toSimpleSpan().range(range).apply {
        if (size != null) scaleSize(size)
        if (colorRes != null) textColor(colorRes)
    }

fun CharSequence.scaleSizeColor(str: String, size: Float? = null, @ColorRes colorRes: Int? = null) =
    toSimpleSpan().first(str).apply {
        if (size != null) scaleSize(size)
        if (colorRes != null) textColor(colorRes)
    }