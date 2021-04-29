package com.cwl.arccoroutine

import com.cwl.common.coroutine.flatMap
import com.cwl.common.exts.toDateMills
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FlowUnitTest {
    @Test
    fun flowTest1() {
        val intFlow= flow<Int> {
            (1..3).forEach {
                if(it==3) throw ArithmeticException() //如果在异常抛出前emit值，则值还是能发出去
                emit(it)
                delay(100)
            }
        }.catch {t: Throwable ->
            println(t)
        }.onCompletion {t->
            //相当于final 始终会执行，t有值说明有没捕获的异常
            print(t)
        }

       runBlocking {
           intFlow.flowOn(Dispatchers.IO).collect {
               println(it)
           }
           //冷数据流，不消费不生产，多次消费多次生产
           intFlow.flowOn(Dispatchers.IO).collect {
               println(it)
           }
       }

    }

    @Test
    fun flowTest2(){
        val intFlow= flow<Int> {
            (1..3).forEach {
                emit(it)
                delay(100)
            }
        }.flowOn(Dispatchers.IO).onEach { println(it) }

        //分离生产和消费
       runBlocking {
            intFlow.collect()

            intFlow.launchIn(GlobalScope).join()//直接指定coroutine scope上消费,join保证运行完才能结束
        }
        println("--------------")
        runBlocking{
            var job=intFlow.launchIn(GlobalScope)
            delay(100)
            //flow取消只需要取消collect这种末端操作符所处的coroutine即可
            job.cancelAndJoin()
        }
    }

    @Test
    fun flowTest3(){
        runBlocking{
            //flow中不能切换调度器
            flow { // BAD!!
                emit(1)
                withContext(Dispatchers.IO){
                    emit(2)
                }
            }.collect { println(it) }
        }

        //correct
        channelFlow {
            send(1)
            withContext(Dispatchers.IO) {
                send(2)
            }
        }


        //flow's create
        listOf(1, 2, 3, 4).asFlow()
        flowOf(1, 2, 3, 4)

    }

    @Test
    fun flowTest4(){
        //下面List本身是初始化list的，这里当循环用了

        //增加buffer进行背压
        flow {
            List(100) {
                emit(it)
            }
        }.buffer()

        //conflate进行背压  新数据直接覆盖老数据
        runBlocking {
            flow {
                List(100) {
                    emit(it)
                }
            }.conflate()
                .collect { value ->
                    println("Collecting $value")
                    delay(100)
                    println("$value collected")
                }
        }

        println("--------------")

        //*Latest 背压会对每个数据进行处理，如果旧的还没处理完就来新的，则直接取消旧的处理新的   //eg: mapLatest、flatMapLatest
        runBlocking {
            flow {
                List(10) {
                    emit(it)
                }
            }.collectLatest{ value ->
                println("Collecting $value")
                delay(100)
                println("$value collected")
            }
        }

    }

    @Test
    fun flowTest5(){
        //Flow 的变换 eg: map filter
        flow {
            List(5){ emit(it) }
        }.map {
            it * 2
        }

        runBlocking {
            flow {
                List(3){ emit(it) }
            }.map {
                //---> 0 1 2
                flow { List(it) { emit(it) } }
                //--->list(0) 不产生  list(1) 产生0  list(3) 产生0,1
            }.flattenConcat()//合并flow
                .collect { println(it) }//结果0 0 1

            //flattenMerge 也是合并，不保证顺序
        }
    }

    @Test
    fun flowTest6(){
        runBlocking {
            val nums = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
            val strs = flowOf("one", "two", "three").onEach { delay(400) } // 每 400 毫秒发射一次字符串
            val startTime = System.currentTimeMillis() // 记录开始的时间
            nums.zip(strs) { a, b -> "$a -> $b" } // 使用“zip”组合单个字符串
                .collect { value -> // 收集并打印
                    println("$value at ${System.currentTimeMillis() - startTime} ms from start")
                }


        }
        println("--------------")

        runBlocking {
            val nums = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
            val strs = flowOf("one", "two", "three").onEach { delay(400) } // 每 400 毫秒发射一次字符串
            val startTime = System.currentTimeMillis() // 记录开始的时间
            nums.combine(strs) { a, b -> "$a -> $b" } // 使用“combine”组合单个字符串
                .collect { value -> // 收集并打印
                    println("$value at ${System.currentTimeMillis() - startTime} ms from start")
                }


        }

    }


    //不能运行只是看下代码
    @Test
    fun flowTest7(){
        suspend fun fun1()= GlobalScope.async {
            delay(1000)
            "fun1"
        }
        suspend fun fun2()= GlobalScope.async {
            delay(1000)
            "fun2"
        }
        runBlocking {
            listOf(::fun1,::fun2).map {
                it.call() // it()
            }.map {
                flow { emit(it.await()) }
            }.merge().onEach { println(it) }
                .collect()
        }

    }



    @Test
    fun ts(){


    }
}



