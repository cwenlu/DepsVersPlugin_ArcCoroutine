package com.cwl.arccoroutine

import androidx.lifecycle.asLiveData
import com.cwl.common.coroutine.flatMap
import com.cwl.common.exts.toDateMills
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.random.Random

/**
 * flow{} 冷流，每一个收集器(collect)都会对应生成flow集
 * SharedFlow（StateFlow是一个带有.value的SharedFlow） 热流  热流的collect会阻塞,记得在合适的时候cancel
 * shareIn 实现冷流转热流
 * flowOn对上游起作用,可以多次设置
 */
class FlowUnitTest {
    @Test
    fun flowTest1() {
        val intFlow = flow<Int> {
            (1..3).forEach {
//                if (it == 3) throw ArithmeticException() //如果在异常抛出前emit值，则值还是能发出去
                check(it != 3)
                emit(it)
                delay(100)
            }
        }.catch { t: Throwable ->//只能处理上游的
            println("throw-->$t")
        }
            .onCompletion { t ->
                //相当于final 始终会执行，t有值说明有没捕获的异常
                println("complete-->${Thread.currentThread()}--->$t")
            }

        runBlocking {
            intFlow.flowOn(Dispatchers.IO).onCompletion {
                println("ss--->${Thread.currentThread()}")
            }.collect {
                println(it)
            }
            //冷数据流，不消费不生产，多次消费多次生产
            intFlow.flowOn(Dispatchers.IO).collect {
                println(it)
            }
        }

    }

    @Test
    fun flowTest1_1() {
        val intFlow = flow<Int> {
            (1..3).forEach {
//                if (it == 3) throw ArithmeticException() //如果在异常抛出前emit值，则值还是能发出去
                check(it != 3)
                emit(it)
                delay(100)
            }
        }.catch { t: Throwable ->//只能处理上游的
            println("throw-->$t")
        }.map {
            //这里在catch下抛出异常没有捕获到
            check(it != 2) {
                "cus"
            }
            it
        }
            .onCompletion { t ->
                //相当于final 始终会执行，t有值说明有没捕获的异常
                println("complete-->$t")
            }

        runBlocking {
            intFlow.flowOn(Dispatchers.IO).collect {
                println(it)
            }
            //如果collect 有判断抛异常，则可以换到onEach里放在catch前进行捕获,消费只需要collect()就可以了
        }

    }

    @Test
    fun flowTest1_2() {
        val intFlow = flow<Int> {
            (1..3).forEach {
//                if (it == 3) throw ArithmeticException() //如果在异常抛出前emit值，则值还是能发出去
                check(it != 3)
                emit(it)
                delay(100)
            }
        }

        runBlocking {

            //可以 try-catch处理一般不推荐这么写
            try {
                intFlow.flowOn(Dispatchers.IO).collect {
                    println(it)
                }
            } catch (e: Throwable) {
                println(e)
            }

        }

    }


    @Test
    fun flowTest2() {
        val intFlow = flow<Int> {
            (1..3).forEach {
                emit(it)
                delay(100)
            }
        }.flowOn(Dispatchers.IO).onEach { println("${Thread.currentThread()}---$it") }

        //分离生产和消费
        runBlocking {
            intFlow.launchIn(GlobalScope)//单独的上下文中,切换了线程，不会阻塞当前携程
            println("over")
            intFlow.collect()

//            intFlow.launchIn(GlobalScope).join()//直接指定coroutine scope上消费,join保证运行完才能结束
        }
        println("--------------")
        runBlocking {
            var job = intFlow.launchIn(GlobalScope)
            delay(100)
            //flow取消只需要取消collect这种末端操作符所处的coroutine即可
            job.cancelAndJoin()
        }
    }

    @Test
    fun flowTest3() {

        runBlocking {
            //冷流 在没有切换线程的情况下，生产者和消费者是同步非阻塞的
            //flow中不能切换调度器
            flow { // BAD!!
                emit(1)
                withContext(Dispatchers.IO) {
                    emit(2)
                }
            }.collect { println(it) }
        }

        //热流 channelFlow 实现了生产者和消费者异步非阻塞模型。
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
    fun flowTest4() {
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
            }.collectLatest { value ->
                println("Collecting $value")
                delay(100)
                println("$value collected")
            }
        }

    }

    @Test
    fun flowTest5() {
        //Flow 的变换 eg: map filter
        flow {
            List(5) { emit(it) }
        }.map {
            it * 2
        }

        runBlocking {
            flow {
                List(3) { emit(it) }
            }.map {
                //---> 0 1 2
                flow { List(it) { emit(it) } }
                //--->list(0) 不产生  list(1) 产生0  list(2) 产生0,1
            }.flattenConcat()//展开flow
                .collect { println(it) }//结果0 0 1

            //flattenMerge 也是合并，不保证顺序
        }
    }

    @Test
    fun flowTest5_1() {

        runBlocking {
            flow {
                List(3) { emit(it) }
            }.flatMapConcat {//map+flattenConcat  有序
                //---> 0 1 2
                flow {
                    //延迟造成转换过程结果返回时间不一致
                    delay((3 - it) * 100L)
                    List(it) {
                        emit(it)
                    }
                }
                //--->list(0) 不产生  list(1) 产生0  list(2) 产生0,1
            }
                .collect { println(it) }//结果0 0 1

            println("---------")

            flow {
                List(3) { emit(it) }
            }.flatMapMerge {//并发收集结果流，所以是无序的
                //---> 0 1 2
                flow {
                    //延迟造成转换过程结果返回时间不一致
                    delay((3 - it) * 100L)
                    List(it) {
                        emit(it)
                    }
                }
                //--->list(0) 不产生  list(1) 产生0  list(2) 产生0,1
            }
                .collect { println(it) }//结果0 0 1
        }
//        flatMapLatest   falt结合Latest的一类api
    }

    @Test
    fun flowTest6() {
        runBlocking {
            val nums = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
            val strs =
                flowOf("one", "two", "three", "other").onEach { delay(400) } // 每 400 毫秒发射一次字符串
            val startTime = System.currentTimeMillis() // 记录开始的时间
            nums.zip(strs) { a, b -> "$a -> $b" } // 按发射的顺序一一匹配组合,一个flow完成，另一个flow就cancel
                .collect { value -> // 收集并打印
                    println("$value at ${System.currentTimeMillis() - startTime} ms from start")
                }


        }
        println("--------------")

        runBlocking {
            val nums = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
            val strs =
                flowOf("one", "two", "three", "other").onEach { delay(400) } // 每 400 毫秒发射一次字符串
            val startTime = System.currentTimeMillis() // 记录开始的时间
            nums.combine(strs) { a, b -> "$a -> $b" } // 一个flow有值过来就与另一个flow最新值匹配,一个flow完成不影响另一个
                .collect { value -> // 收集并打印
                    println("$value at ${System.currentTimeMillis() - startTime} ms from start")
                }


        }

    }


    //不能运行只是看下代码
    @Test
    fun flowTest7() {
        suspend fun fun1() = GlobalScope.async {
            delay(1000)
            "fun1"
        }

        suspend fun fun2() = GlobalScope.async {
            delay(1000)
            "fun2"
        }
        runBlocking {
            listOf(::fun1, ::fun2).map {
                it.call() // it()
            }.map {
                flow { emit(it.await()) }
            }.merge().onEach { println(it) }
                .collect()
        }

    }


    @Test
    fun flowTest8() {
        val f = flow {
            emit(1)
            delay(90)
            emit(2)
            delay(90)
            emit(3)
            delay(1010)
            emit(4)
            delay(1010)
            emit(5)
        }
        runBlocking {
            f.debounce(1000)//小于指定时间的不能发射，一直小于一直不发射
                .collect {
                    println("debounce-->$it")
                }

            println("--------------")

            f.sample(1000)//每隔指定周期就会发射一次最新值
                .collect {
                    println("sample-->$it")
                }
        }

    }


    @Test
    fun flowTest9() {

        runBlocking {

            launch {
                var i = 0
                while (isActive) {
                    EventBus.postEvent(i++)
                    delay(500)
                }
            }

            var job1 = launch {
                EventBus.events.collect {
                    println("A$it")
                }
            }

            delay(1000)
            var job2 = launch {
                EventBus.events.collect {
                    println("B$it")
                }
            }

            delay(2000)
            //热流需要用cancel退出,因为collect内部是while(true),靠ensureActive监测取消退出的
            job1.cancel()
            job2.cancel()


        }
    }

    @Test
    fun flowTest9_1() {

        runBlocking {

            val _stateFlow = MutableStateFlow(1)
            val stateFlow = _stateFlow.asStateFlow()

            launch {

                launch {
                    //会阻塞所以单独放一个coroutine上下文,外层parent的canel导致自己cancel
                    stateFlow.collect {
                        println("collect--->$it")
                    }
                }

                while (isActive) {
                    _stateFlow.value++
                    delay(500)
                    if (stateFlow.value == 4) break
                }

                cancel()
            }

        }
    }

    @Test
    fun flowTest9_2() {

        runBlocking {
            //started  共享开始停止策略

            val data = (1..10).asFlow().onEach { delay(500) }.onEach { println("generate") }
            val sf = data.shareIn(this, SharingStarted.Eagerly)
                .onEach { println("Eagerly--->$it") }//立即开始共享永不停止

            //当然数据产生者本身结束了还是会结束
        }
    }

    @Test
    fun flowTest9_2_1() {
        runBlocking {
            //started  共享开始停止策略

            val data = (1..10).asFlow().onEach { delay(500) }.onEach { println("generate") }
            val sf = data.shareIn(this, SharingStarted.Lazily)
                .onEach { println("Lazily--->$it") }//有一个收集者时开始共享，永不停止

            //如果一直没收集者一直会阻塞，即时发射者本身是可以结束的
        }
    }


    @Test
    fun flowTest9_2_2() {
        runBlocking {
            //started  共享开始停止策略


            val data =
                (1..Int.MAX_VALUE).asFlow().onEach { delay(500) }.onEach { println("generate") }

            val sf = data.shareIn(
                GlobalScope,
                //stopTimeoutMillis 定义最后一个收集者消失后多久数据产生者停止
                //replayExpirationMillis  定义生产者停止多久后清空重播缓存(replay)
                SharingStarted.WhileSubscribed()
            )//有一个收集者时开始共享，一个收集者都没有的时候停止
            //如果一直没收集者一直会阻塞(阻塞对应的CoroutineScope，这里指GlobalScope)，即时发射者本身是可以结束的

            delay(1000)

            val job = launch {
                sf.collect {
                    println("job1-->$it")
                }
            }
            delay(1000)
            val job2 = launch {
                sf.collect {
                    println("job2-->$it")
                }
            }
            delay(1000)
            job.cancel()
//            delay(1000)
//            job2.cancel()
        }
    }


    @Test
    fun flowTest9_2_3() {
        runBlocking {
            //started  共享开始停止策略
            val data =
                (1..Int.MAX_VALUE).asFlow().onEach { delay(500) }.onEach { println("generate") }

            val sf = data.shareIn(
                this,
                //stopTimeoutMillis 定义最后一个收集者消失后多久数据产生者停止
                //replayExpirationMillis  定义生产者停止多久后清空重播缓存(replay)
                SharingStarted.WhileSubscribed(stopTimeoutMillis = 2000)
            )//有一个收集者时开始共享，一个收集者都没有的时候停止
            //如果一直没收集者一直会阻塞(阻塞对应的CoroutineScope，这里指GlobalScope)，即时发射者本身是可以结束的

            delay(1000)

            val job = launch {
                sf.collect {
                    println("job1-->$it")
                }
            }
            delay(1000)
            job.cancel()

            //sf 在 runBlocking 上 job停止后由于stopTimeoutMillis = 2000，所以还会在生产一会
        }
    }

    @Test
    fun flowTest9_2_4() {
        runBlocking {
            //started  共享开始停止策略
            val data =
                (1..Int.MAX_VALUE).asFlow().onEach { delay(500) }.onEach { println("generate") }

            val sf = data.shareIn(
                this,
                //stopTimeoutMillis 定义最后一个收集者消失后多久数据产生者停止
                //replayExpirationMillis  定义生产者停止多久后清空重播缓存(replay)
                SharingStarted.WhileSubscribed(replayExpirationMillis = 2000),
                replay = 2
            )//有一个收集者时开始共享，一个收集者都没有的时候停止
            //如果一直没收集者一直会阻塞(阻塞对应的CoroutineScope，这里指GlobalScope)，即时发射者本身是可以结束的

            delay(1000)

            val job = launch {
                sf.collect {
                    println("job1-->$it")
                }
            }
            delay(1000)
            job.cancel()

            val job2 = launch {
                sf.collect {
                    println("job2-->$it")
                }
            }
            //sf 在 runBlocking 上 job停止后又马上有个job2订阅，由于replayExpirationMillis=2000,所以job2还能获取到之前的值
        }
    }

    //StateFlow 有类似的 stateIn

    @InternalCoroutinesApi
    @Test
    fun flowTest10(){
        runBlocking {
            var share=MutableSharedFlow<Int>()
            var flow=(1..5).asFlow().onEach { println("generat-->$it") }.onEach { delay(500) }

            launch {
                //shareIn 里面就是这样把flow 用 SharedFlow接收了，然后做了一些处理
                flow.collect(share)
            }

            delay(1000)

//            share.collect {
//                println(it)
//            }

        }
    }


}

object EventBus {
    //replay: Int = 0,  针对新订阅者重新发送之前已发出的数据项数目
    //extraBufferCapacity: Int = 0, 除“replay”外缓冲的数据项数目
    //onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND  设置相关策略来处理缓冲区中已存满要发送的数据项的情况。默认值为 BufferOverflow.SUSPEND，这会使调用方挂起。其他选项包括 DROP_LATEST 或 DROP_OLDEST
    private val mutableEvents = MutableSharedFlow<Int>()

    val events = mutableEvents.asSharedFlow()

    suspend fun postEvent(event: Int) {
        mutableEvents.emit(event)
    }
}


