package com.cwl.arccoroutine

import com.cwl.arccoroutine.jectpack.TestVM
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.coroutines.startCoroutine

/**

 * @Author cwl

 * @Date 2021/5/1 23:52

 */
class BasicUnitTest {
    //DEFAULT 立即执行协程体
    //ATOMIC 立即执行协程体，但在开始运行之前无法取消
    //UNDISPATCHED 立即在当前线程执行协程体，直到第一个 suspend 调用,后面的就在suspend所在上下文上运行
    //LAZY 执行start,join时才运行
    @Test
    fun basic1() {
        GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            println(Thread.currentThread())
        }
        GlobalScope.launch {
            println(Thread.currentThread())
        }
    }



    @Test
    fun basic2() {
        val string = String()
    }
}

