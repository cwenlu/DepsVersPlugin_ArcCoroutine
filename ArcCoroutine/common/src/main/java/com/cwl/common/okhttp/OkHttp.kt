package com.cwl.common.okhttp

import android.content.Context
import com.cwl.common.di.koin
import com.cwl.common.exts.toMap
import com.cwl.okhttpdsl.http.config.OkHttpConfig
import com.cwl.okhttpdsl.http.config.RequestBuilder

fun String.rb()= RequestBuilder(this)

fun RequestParams.rb()=RequestBuilder(url).params(this.toMap())

inline fun <reified R:RequestParams> R.httpJson()=RequestBuilder(url).json(this)

/**
 * bean作为请求载体时继承
 */
abstract class RequestParams{
    abstract var url:String
}

/**
 * 全局配置
 */
fun OkHttpConfig(context: Context, block: OkHttpConfig.() -> Unit) = koin.get<OkHttpConfig>().apply{
    this.context=context
    block()
}