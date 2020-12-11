package com.cwl.okhttpdsl.http.interceptor

import com.cwl.okhttpdsl.http.config.OkHttpConfig
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class OnlineCacheInterceptor @JvmOverloads constructor( cacheControlValue: Int=OkHttpConfig.DEFAULT_MAX_AGE_ONLINE) : Interceptor {


    private var cacheControlValue:String=String.format("max-age=%d",cacheControlValue)

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())
        var cacheControl = originalResponse.header("Cache-Control")
        cacheControl?.apply {
            if(isEmpty() || contains("no-store") || contains("no-cache")
                || contains("must-revalidate") || contains("max-age")
                || contains("max-stale")){

                Timber.i(originalResponse.headers().toString())
                //也可以使用CacheControl类
                return originalResponse.newBuilder()
                    .header("Cache-Control","public, $cacheControlValue")
                    .removeHeader("Pragma")
                    .build()
            }
        }

        return originalResponse
    }
}