package com.cwl.common.exts

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest

/**

 * @Author cwl

 * @Date 2021/1/29 11:31

 */

/**
 * view在给定的x,y下返回true
 * view 和提供x的MotionEvent必须子父关系, 不能隔代
 *
 * X position to test in the parent's coordinate system 父坐标系中的值
 */
fun View.isViewUnder(x:Int,y:Int):Boolean{
//    return x>=left && x<=right && y>=top && y<=bottom
    return x in left .. right && y in top .. bottom
}

//androidx.core.view 中提供了一些操作
//val View.isGone get() = visibility==View.GONE

inline val View.isVisibility get() = visibility==View.VISIBLE


@FlowPreview
@ExperimentalCoroutinesApi
fun TextView.textWatcherFlow()= callbackFlow<String> {
    val textWatcher=object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            offer(p0.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

    }
    addTextChangedListener(textWatcher)
    awaitClose { removeTextChangedListener(textWatcher) }
}.buffer(Channel.CONFLATED)//始终最新的
    .debounce(300)