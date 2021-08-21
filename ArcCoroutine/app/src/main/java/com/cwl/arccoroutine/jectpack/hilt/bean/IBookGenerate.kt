package com.cwl.arccoroutine.jectpack.hilt.bean

import javax.inject.Inject

/**
 * @Author cwl
 * @Date 2021/8/21 14:59
 * @Description
 */
interface IBookGenerate {
    fun generateBook(): Any
}

class Book2GenerateImpl @Inject constructor() : IBookGenerate {
    override fun generateBook(): Any {
        return Book2("book2", "qd")
    }

}

class Book3GenerateImpl @Inject constructor(): IBookGenerate {
    override fun generateBook(): Any {
        return Book3("book3", "qd")
    }

}