package com.cwl.arccoroutine.jectpack.hilt.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Qualifier

/**
 * @Author cwl
 * @Date 2021/8/21 13:55
 * @Description
 */
@Module
@InstallIn(SingletonComponent::class)
object StringGenerateModule {
    const val NAME_BOOK= "name_book"
    const val NAME_AUTHOR= "name_author"

    @Provides
    @Named(NAME_BOOK)
    fun charset1() = "charset1"

    @Provides
    @Named(NAME_AUTHOR)
    fun charset2() = "charset2"

    @QualiferBookName
    @Provides
    fun charset3() = "charset3"

    @QualiferBookAuthor
    @Provides
    fun charset4() = "charset4"
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class QualiferBookName

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class QualiferBookAuthor