package com.cwl.common.coroutine

import androidx.annotation.IntDef
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**

 * @Author cwl

 * @Date 2019-08-28 19:57

 */

object AutoCancel {
    const val ON_PAUSE = 1
    const val ON_STOP = 2
    const val ON_DESTORY = 3
}

@IntDef(AutoCancel.ON_PAUSE, AutoCancel.ON_STOP, AutoCancel.ON_DESTORY)
annotation class AutoCancelState

/**
 * 可自动取消的CoroutineScope
 */
open class AutoCancelableCoroutineScope(context: CoroutineContext, @AutoCancelState val registerAutoCancelState: Int = AutoCancel.ON_DESTORY) :
    CoroutineScope, DefaultLifecycleObserver {
    override val coroutineContext: CoroutineContext = context

    fun cancel() = coroutineContext.cancel()

    fun handleCancel(@AutoCancelState autoCancelState: Int) {
        if (registerAutoCancelState == autoCancelState && coroutineContext[Job]?.isCancelled == true) cancel()
    }

    override fun onPause(owner: LifecycleOwner) = handleCancel(AutoCancel.ON_PAUSE)

    override fun onStop(owner: LifecycleOwner) = handleCancel(AutoCancel.ON_STOP)

    override fun onDestroy(owner: LifecycleOwner) = handleCancel(AutoCancel.ON_DESTORY)
}

fun createAutoCancelableCoroutineScope(dispatcher: CoroutineDispatcher,@AutoCancelState registerAutoCancelState: Int)=
    AutoCancelableCoroutineScope(SupervisorJob() + dispatcher,registerAutoCancelState)

fun LifecycleOwner.uiScope(@AutoCancelState registerAutoCancelState: Int=AutoCancel.ON_DESTORY): AutoCancelableCoroutineScope=createAutoCancelableCoroutineScope(Dispatchers.Main,registerAutoCancelState).apply {
    lifecycle.addObserver(this)
}

fun LifecycleOwner.ioScope(@AutoCancelState registerAutoCancelState: Int=AutoCancel.ON_DESTORY): AutoCancelableCoroutineScope=createAutoCancelableCoroutineScope(Dispatchers.IO,registerAutoCancelState).apply {
    lifecycle.addObserver(this)
}

fun LifecycleOwner.defScope(@AutoCancelState registerAutoCancelState: Int=AutoCancel.ON_DESTORY): AutoCancelableCoroutineScope=createAutoCancelableCoroutineScope(Dispatchers.Default,registerAutoCancelState).apply {
    lifecycle.addObserver(this)
}



fun LifecycleOwner.UI(@AutoCancelState registerAutoCancelState: Int = AutoCancel.ON_DESTORY, block: suspend CoroutineScope.() -> Unit) =uiScope(registerAutoCancelState).launch(block = block)
fun LifecycleOwner.IO(@AutoCancelState registerAutoCancelState: Int = AutoCancel.ON_DESTORY, block: suspend CoroutineScope.() -> Unit) =ioScope(registerAutoCancelState).launch(block = block)
fun LifecycleOwner.DFT(@AutoCancelState registerAutoCancelState: Int = AutoCancel.ON_DESTORY, block: suspend CoroutineScope.() -> Unit) =defScope(registerAutoCancelState).launch(block = block)


suspend fun <T> withUI(block: suspend CoroutineScope.() -> T)= withContext(Dispatchers.Main,block)
suspend fun <T> withIO(block: suspend CoroutineScope.() -> T)= withContext(Dispatchers.IO,block)
suspend fun <T> withDFT(block: suspend CoroutineScope.() -> T)= withContext(Dispatchers.Default,block)