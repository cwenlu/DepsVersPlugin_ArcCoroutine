package com.cwl.arccoroutine

import android.app.Application
import com.cwl.common.imageloader.ImageLoaderOptions
import com.cwl.common.imageloader.picasso.PicassoImageLoader
import com.cwl.common.imageloader.register
import com.cwl.common.okhttp.OkHttpConfig
import com.cwl.okhttpdsl.http.interceptor.HttpLogInterceptor

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
                addInterceptor(HttpLogInterceptor())
            }

            baseUrl="http://www.baidu.com/"
        }

        PicassoImageLoader<ImageLoaderOptions>(this).register()

    }


}