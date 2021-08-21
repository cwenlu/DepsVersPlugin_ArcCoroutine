package com.cwl.arccoroutine.jectpack.hilt.data

import com.cwl.arccoroutine.jectpack.hilt.bean.Book
import com.cwl.arccoroutine.jectpack.hilt.bean.Book2
import com.cwl.arccoroutine.jectpack.hilt.di.StringGenerateModule
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * @Author cwl
 * @Date 2021/8/21 11:09
 * @Description
 */
class BookRepository @Inject constructor() {
    init {
        Timber.i("${javaClass.simpleName} init...")
    }
    fun generateBooks() = listOf(
        Book("book1","cwl1",12f),
        Book("book2","cwl2",17f),
        Book("book3","cwl3",27f),
        Book(),
    )
}

@Singleton
class SingletionBookRepository @Inject constructor(){
    init {
        Timber.i("${javaClass.simpleName} init...")
    }

    fun generateBooks() = listOf(
        Book("book1","cwl1",12f),
        Book("book2","cwl2",17f),
        Book("book3","cwl3",27f),
    )
}