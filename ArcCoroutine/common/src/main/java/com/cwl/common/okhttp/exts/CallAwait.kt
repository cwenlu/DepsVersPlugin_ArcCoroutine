package com.cwl.okhttpdsl.http.exts

import com.cwl.common.di.koin
import com.cwl.common.okhttp.core.ProgressListener
import com.cwl.okhttpdsl.http.config.OkHttpConfig
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.IOException
import kotlin.coroutines.resumeWithException

suspend inline fun <reified T> Call.await(file: File?=null,noinline progressListener: ProgressListener?=null): T = suspendCancellableCoroutine { continuation->
    enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            if(continuation.isCancelled) return
            continuation.resumeWithException(e)
        }

        override fun onResponse(call: Call, response: Response) {
            if(continuation.isCancelled) return
            continuation.resumeWith(runCatching {
                if(response.isSuccessful){
                    if(T::class === File::class) response.body2File(file?:File(koin.get<OkHttpConfig>().diskCacheDirectory,parseFileName(response)),progressListener) as T
                    else response.convert<T>()?:throw NullPointerException("Response body is null: $response")

                }else{
                    throw HttpException(response.code, response.message)
                }
            })
        }
    })
    registerOnCompletion(continuation)
}

fun Call.registerOnCompletion(continuation: CancellableContinuation<*>) =
    continuation.invokeOnCancellation {
        try {
            cancel()
        } catch (ex: Throwable) {
            //Ignore cancel exception
        }
    }

class HttpException (val code:Int,message:String) :Exception(message)

/**
 * 解析文件头
 * Content-Disposition:attachment;filename=FileName.txt
 * Content-Disposition: attachment; filename*="UTF-8''%E6%9B%BF%E6%8D%A2%E5%AE%9E%E9%AA%8C%E6%8A%A5%E5%91%8A.pdf"
 */
fun parseFileName(response: Response):String{
    var dispositionHeader = response.header("Content-Disposition")
    if(dispositionHeader.isNullOrEmpty()){
        return System.currentTimeMillis().toString()
    }else{
        dispositionHeader=dispositionHeader.replace(" ","")
        dispositionHeader=dispositionHeader.replace("attachment;filename=","")
        dispositionHeader=dispositionHeader.replace("filename*=utf-8", "")
        return dispositionHeader
    }
}