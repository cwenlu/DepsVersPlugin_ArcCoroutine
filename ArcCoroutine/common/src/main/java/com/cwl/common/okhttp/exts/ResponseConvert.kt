package com.cwl.okhttpdsl.http.exts

import com.cwl.common.exts.json2Bean
import com.cwl.common.okhttp.core.ProgressListener
import com.cwl.common.util.FileUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Response
import okio.Okio
import okio.buffer
import okio.sink
import java.io.File
import kotlin.coroutines.CoroutineContext

/**
 * 转换器
 * @converter 自定义的转换器
 */
inline fun <reified T> Response.convert(noinline converter: (Response.() -> T)? = null): T =
    converter?.invoke(this) ?: when (T::class) {
        String::class -> body2str() as T
        else -> body2Bean()
    }


fun Response.body2str(): String = body?.string() ?: throw RuntimeException("convert2str failure")

inline fun <reified T> Response.body2Bean(): T =
    body2str().json2Bean<T>() ?: throw RuntimeException("body2Bean failure")

fun Response.body2File(file: File,progressListener: ProgressListener?):File{
    FileUtil.createNewFile(file.absolutePath)
    var body=body?:throw NullPointerException("Response body is null: $this")
    var contentLength = body.contentLength()
    var sink = file.sink().buffer()
    var buffer = sink.buffer
    var bufferSize=200*1024 //200kb
    var source = body.source()
    var len=0
    var currenLength=0L
    do {
        len= source.read(buffer, bufferSize.toLong()).toInt()
        currenLength+=len
        progressListener?.invoke(currenLength,contentLength,100.0f*currenLength/contentLength)
    }while(len!=-1)
    source.close()
    sink.close()
    return file
}



