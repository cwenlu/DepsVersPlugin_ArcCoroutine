package com.cwl.arccoroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.produce
import org.junit.Test

/**

 * @Author cwl

 * @Date 2021/5/2 15:07

 */
class ChannelUnitTest {
    // UNLIMITED 生产者可以无限制发送数据,直到oom。不会suspned
    // RENDEZVOUS 发送一个数据后就会suspned,等取走后则恢复可以发送新数据
    // CONFLATED 一直发送，如果有数据还没被消费，则会覆盖。(相当于只会缓冲1个) 不会suspned
    // BUFFERED 缓冲区满后会suspned,有位置后会恢复

    @Test
    fun channelTest1(){
        runBlocking {
            var channel = Channel<Int>(Channel.CONFLATED)
            launch {
                var i=0
                while (i<10){
                    channel.send(i++)
                    delay(500)
                }
            }
            delay(1100)
            println(channel.receive())

//            var iterator = channel.iterator()
//            while (iterator.hasNext()) {
//                println(iterator.next())
//            }
//
//            for (i in channel) {
//                println(i)
//            }

        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun channelTest2(){
        runBlocking {
            //构造生产者返回消费者
           val recieve= produce<Int> {
                var i=0
                while(i<5){
                    send(i++)
                    delay(500)
                }
            }

            for (i in recieve) {
                println(i)
            }
            println("-----------")

            //构造消费者返回生产者
            val send=actor<Int> {
                while (isActive){
                    println(receive())
                }
            }
            for (i in 1..3){
                send.send(i)
                delay(500)
            }
        }
    }

}