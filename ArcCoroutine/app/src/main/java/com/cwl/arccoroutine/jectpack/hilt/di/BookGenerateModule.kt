package com.cwl.arccoroutine.jectpack.hilt.di

import com.cwl.arccoroutine.jectpack.hilt.bean.Book2GenerateImpl
import com.cwl.arccoroutine.jectpack.hilt.bean.Book3GenerateImpl
import com.cwl.arccoroutine.jectpack.hilt.bean.IBookGenerate
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Qualifier

/**
 * @Author cwl
 * @Date 2021/8/21 15:07
 * @Description
 */
@Module
@InstallIn(ViewModelComponent::class)
interface BookGenerateModule {//可以用接口也可以用抽象类
    @QualiferBook2
    @Binds
    fun book2Generate(impl: Book2GenerateImpl):IBookGenerate

    @QualiferBook3
    @Binds
    fun book3Generate(impl: Book3GenerateImpl):IBookGenerate
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class QualiferBook2

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class QualiferBook3