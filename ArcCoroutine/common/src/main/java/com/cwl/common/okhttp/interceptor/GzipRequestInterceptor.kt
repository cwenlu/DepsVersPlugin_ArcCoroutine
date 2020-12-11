package com.cwl.okhttpdsl.http.interceptor

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Response
import okio.BufferedSink
import okio.GzipSink
import okio.Okio
import java.io.IOException

/**
 * gzip压缩拦截器
 */
class GzipRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequest = chain.request()
        if(originalRequest.body()==null || originalRequest.header("Content-Encoding")==null){
            return chain.proceed(originalRequest)
        }
        var compressedRequest = originalRequest.newBuilder()
//                https://www.jianshu.com/p/a9d861732445
//                设置了这个需要自己处理返回流，okhttp不会自动处理gzip了
//            .header("Accept-Encoding", "gzip")
            .header("Content-Encoding", "gzip")
            .method(originalRequest.method(), gzip(originalRequest.body()!!))
            .build()
        return chain.proceed(compressedRequest)

    }

    private fun gzip(body: RequestBody): RequestBody {
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return body.contentType()
            }

            override fun contentLength(): Long {
                return -1
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                val gzipSink = Okio.buffer(GzipSink(sink))
                body.writeTo(gzipSink)
                gzipSink.close()
            }
        }
    }
}