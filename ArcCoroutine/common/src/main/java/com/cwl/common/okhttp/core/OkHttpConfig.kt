package com.cwl.okhttpdsl.http.config

import android.content.Context
import com.cwl.common.BuildConfig
import com.cwl.common.di.koin
import com.cwl.common.util.FileUtil
import com.cwl.okhttpdsl.http.interceptor.BasicParamsInterceptor
import com.cwl.okhttpdsl.http.interceptor.GzipRequestInterceptor
import com.cwl.okhttpdsl.http.interceptor.OfflineCacheInterceptor
import com.cwl.okhttpdsl.http.interceptor.OnlineCacheInterceptor
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.Cache
import okhttp3.OkHttpClient
import timber.log.Timber
import java.io.File
import java.net.URLConnection
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

/**
 * 网络配置,默认使用全局配置
 *
 * @param okHttpClientBuilder
 * @param retrofitBuilder
 */

class OkHttpConfig @JvmOverloads internal constructor(
    var okHttpClientBuilder: OkHttpClient.Builder = koin.get()
) {

    init {
        //方便在脱离Android的环境里测试
        var clazz = Class.forName("com.cwl.common.BuildConfig")
        if (clazz!=null && BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

//        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    //----留着删除用的handle----
    private var gzipInterceptor: GzipRequestInterceptor? = null
    private var onlineCacheInterceptor: OnlineCacheInterceptor? = null
    private var offlineCacheInterceptor: OfflineCacheInterceptor? = null
    //------


    internal companion object {
        val DEFAULT_TIMEOUT = 60L
        val DEFAULT_RETRY_DELAY_MILLIS = 1000
        val DEFAULT_RETRY_COUNT = 0
        /**
         * 默认在线最大缓存时间 s
         */
        val DEFAULT_MAX_AGE_ONLINE = 60
        /**
         * 离线缓存时间
         */
        val DEFAULT_MAX_AGE_OFFLINE = 24 * 60 * 60
        /**
         * 默认http缓存目录
         */
        val DEFAULT_CACHE_HTTP_DIR = "http_cache"
        /**
         * 磁盘缓存目录
         */
        val DEFAULT_CACHE_DISK_DIR = "disk_cache"
        /**
         * http cache 默认大小 10M (字节)
         */
        val DEFAULT_CACHE_HTTP_MAX_SIZE = 1024 * 1024 * 10L


        /**
         * 获取文件MimeType
         *
         * @param filename
         * @return
         */
        fun getMimeType(filename: String): String {
            val filenameMap = URLConnection.getFileNameMap()
            var contentTypeFor: String? = filenameMap.getContentTypeFor(filename)
            if (contentTypeFor == null) {
                contentTypeFor = "application/octet-stream"
            }
            return contentTypeFor
        }
    }

    var context: Context?=null

    fun safeGetContext()=context?.applicationContext?:throw NullPointerException("context is null")

    /**
     * http cache 路径file
     */
    val httpCacheDirectory by lazy {File(FileUtil.getDiskCachePath(safeGetContext()), DEFAULT_CACHE_HTTP_DIR)}

    /**
     * disk cache
     */
    val diskCacheDirectory by lazy{File(FileUtil.getDiskCachePath(safeGetContext()), DEFAULT_CACHE_DISK_DIR)}

    constructor(okHttpConfig: OkHttpConfig) : this(
        okHttpConfig.okHttpClien().newBuilder()
    ) {
        context=okHttpConfig.context
        retryDelayMillis = okHttpConfig.retryDelayMillis
        retryCount = okHttpConfig.retryCount
        gzipInterceptor = okHttpConfig.gzipInterceptor
    }

    /**
     * 基础地址
     */
    var baseUrl: String? = null

    /**
     * 请求失败重试间隔时间
     */
    var retryDelayMillis: Int = DEFAULT_RETRY_DELAY_MILLIS
        set(value) {
            if (field >= 0) field = value
        }
    /**
     * 请求失败重试次数
     */
    var retryCount: Int = DEFAULT_RETRY_COUNT
        set(value) {
            if (field >= 0) field = value
        }

    /**
     * okhttp 配置 一些简答的okhttp相关配置用这个不单独封装
     */
    inline fun config(okHttpConfig: OkHttpClient.Builder.() -> Unit) = okHttpClientBuilder.apply(okHttpConfig)


    /**
     * @param extend 是否继承配置
     */
    @JvmOverloads
    fun newConfig(extend: Boolean = true): OkHttpConfig = if (extend) OkHttpConfig(this) else OkHttpConfig()

    fun okHttpClien() = okHttpClientBuilder.build()


    @JvmOverloads
    fun connectTimeout(timeout: Long, unit: TimeUnit = TimeUnit.SECONDS) =
        okHttpClientBuilder.connectTimeout(if (timeout > -1) timeout else DEFAULT_TIMEOUT, unit)

    @JvmOverloads
    fun writeTimeout(timeout: Long, unit: TimeUnit = TimeUnit.SECONDS) =
        okHttpClientBuilder.writeTimeout(timeout, unit)

    @JvmOverloads
    fun readTimeout(timeout: Long, unit: TimeUnit = TimeUnit.SECONDS) =
        okHttpClientBuilder.readTimeout(timeout, unit)

    /**
     * 同步cookie
     */
    fun synCookie() = okHttpClientBuilder.cookieJar(
        PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(safeGetContext())
        )
    )

    /**
     * 是否cache
     */
    var isCache: Boolean by Delegates.observable(false) { _, oldValue, newValue ->
        if (newValue) {
            okHttpClientBuilder.cache(Cache(httpCacheDirectory, DEFAULT_CACHE_HTTP_MAX_SIZE))
                .addNetworkInterceptor(OnlineCacheInterceptor())
                .addInterceptor(OfflineCacheInterceptor(safeGetContext()))

        } else {
            okHttpClientBuilder.cache(null).apply {
                interceptors().remove(offlineCacheInterceptor)
                networkInterceptors().remove(onlineCacheInterceptor)
            }

        }

    }

    /**
     * 是否gzip
     */
    var isGzip: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        if (newValue) okHttpClientBuilder.addInterceptor(GzipRequestInterceptor())
        else okHttpClientBuilder.interceptors().remove(gzipInterceptor)
    }

    /**
     * 通用参数配置
     */
    fun basicParamsInterceptor(block: BasicParamsInterceptor.Builder.() -> Unit) =
        okHttpClientBuilder.addInterceptor(BasicParamsInterceptor.build(block))
}


//followRedirects 支持重定向