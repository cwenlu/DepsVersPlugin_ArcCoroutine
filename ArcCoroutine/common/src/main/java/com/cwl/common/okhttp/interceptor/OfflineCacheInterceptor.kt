package com.cwl.okhttpdsl.http.interceptor

import android.content.Context
import com.cwl.common.util.NetworkUtils
import com.cwl.okhttpdsl.http.config.OkHttpConfig
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

import java.io.IOException

/**
 *  离线缓存拦截
 */
class OfflineCacheInterceptor @JvmOverloads constructor(
    private val context: Context,
    cacheControlValue: Int = OkHttpConfig.DEFAULT_MAX_AGE_OFFLINE
) : Interceptor {
    private var cacheControlValue: String = String.format("max-stale=%d", cacheControlValue)

     //强制使用网络
    // .cacheControl(CacheControl.FORCE_NETWORK)
    //强制使用缓存
    //.cacheControl(CacheControl.FORCE_CACHE)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!NetworkUtils.isConnected(context)) {
            request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build()
            val response = chain.proceed(request)
            return response.newBuilder()
                .header("Cache-Control", "public, only-if-cached, $cacheControlValue")
                .removeHeader("Pragma")
                .build()
        }
        return chain.proceed(request)
    }
}
