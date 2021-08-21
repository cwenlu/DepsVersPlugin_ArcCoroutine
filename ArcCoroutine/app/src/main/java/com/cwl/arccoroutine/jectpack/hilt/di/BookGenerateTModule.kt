package com.cwl.arccoroutine.jectpack.hilt.di

import com.cwl.arccoroutine.jectpack.hilt.bean.Book
import com.cwl.arccoroutine.jectpack.hilt.bean.BookGenerateTImpl
import com.cwl.arccoroutine.jectpack.hilt.bean.IBookGenerate
import com.cwl.arccoroutine.jectpack.hilt.bean.IBookGenerateT
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Inject
import javax.inject.Named

/**
 * @Author cwl
 * @Date 2021/8/21 15:34
 * @Description
 */

@Module
@InstallIn(ActivityComponent::class)
interface BookGenerateTModule {

//    使用时泛型保持一致才不会报错
//    @Inject
//    @Named("book")
//    lateinit var iBookGenerateT: IBookGenerateT<Book>
    @Binds
    fun bookGenerate(impl: BookGenerateTImpl<Book>): IBookGenerateT<Book>
}