package com.cwl.arccoroutine.jectpack.hilt.bean

import com.cwl.arccoroutine.jectpack.hilt.di.QualiferBookAuthor
import com.cwl.arccoroutine.jectpack.hilt.di.QualiferBookName
import com.cwl.arccoroutine.jectpack.hilt.di.StringGenerateModule
import javax.inject.Inject
import javax.inject.Named

/**
 * @Author cwl
 * @Date 2021/8/21 11:05
 * @Description
 */
data class Book(
    var name: String = "default_name",
    var author: String = "default_author",
    var price: Float = 0f,
)

data class Book2 @Inject constructor(
    @Named(StringGenerateModule.NAME_BOOK)
    var name: String,
    @Named(StringGenerateModule.NAME_AUTHOR)
    var author: String
)

data class Book3 @Inject constructor(
    @QualiferBookName
    var name: String,
    @QualiferBookAuthor
    var author: String
)