package com.cwl.common.di

import com.cwl.okhttpdsl.http.config.OkHttpConfig
import okhttp3.OkHttpClient
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.dsl.koinApplication
import org.koin.dsl.module

/**
 * 注意静态块初始化顺序
 */

val httpModule = module {
    single { OkHttpClient.Builder()}
    single { OkHttpConfig() }

    single { get<OkHttpClient.Builder>().build() }//OkHttpClient


}

object CommonKoinContext {
    var koinApp : KoinApplication = koinApplication {
        // declare used modules
        modules(httpModule)
    }
}

val koin=CommonKoinContext.koinApp.koin


open class OkHttpKoinComponent : KoinComponent {
    override fun getKoin(): Koin =koin
}