package com.cwl.arccoroutine

import android.app.Application
import com.cwl.arccoroutine.test.paging.di.appModuleTest
import com.cwl.arccoroutine.wanandroid.di.appModule
import com.cwl.common.imageloader.ImageLoaderOptions
import com.cwl.common.imageloader.picasso.PicassoImageLoader
import com.cwl.common.imageloader.register
import com.cwl.common.okhttp.OkHttpConfig
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

//import com.cwl.okhttpdsl.http.interceptor.HttpLogInterceptor

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        OkHttpConfig(this){
            //注意这个要在日志拦截器前面不然打印不出设置的请求参数
            basicParamsInterceptor{
//                headers("3" to "3")
//                headers("3:3")
            }
            isCache=true
            config {
//                addInterceptor(HttpLogInterceptor())
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            }

//            baseUrl="http://www.baidu.com/"
            baseUrl="https://www.wanandroid.com/"
        }

        PicassoImageLoader<ImageLoaderOptions>(this).register()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(appModuleTest)
            modules(appModule)
        }

    }


}