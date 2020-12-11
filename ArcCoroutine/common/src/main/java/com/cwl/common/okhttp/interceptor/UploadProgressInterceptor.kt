package com.cwl.okhttpdsl.http.interceptor

import com.cwl.common.okhttp.core.ProgressListener
import com.cwl.common.okhttp.core.UploadProgressRequestBody
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 上传进度拦截
 */
class UploadProgressInterceptor(var progressListener: ProgressListener) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var originalRequet = chain.request()
        if(originalRequet.body()==null) return chain.proceed(originalRequet)

        var progressRequest = originalRequet.newBuilder()
            .method(originalRequet.method(), UploadProgressRequestBody(originalRequet.body()!!, progressListener))
            .build()
        return chain.proceed(progressRequest)
    }
}