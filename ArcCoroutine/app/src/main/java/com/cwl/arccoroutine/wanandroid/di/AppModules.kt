package com.cwl.arccoroutine.wanandroid.di

import com.cwl.arccoroutine.wanandroid.data.repository.HotArticlesRepository
import com.cwl.arccoroutine.wanandroid.ui.hotarticles.HotArticlesViewModel
import org.koin.dsl.module

/**

 * @Author cwl

 * @Date 2021/1/4 15:07

 */

val dataSourceModule=module{

}

val repositoryModule= module {
    factory { HotArticlesRepository() }
}

val viewModelModule=module{
    factory { HotArticlesViewModel(get()) }
}

val appModule= listOf(dataSourceModule,repositoryModule,viewModelModule)