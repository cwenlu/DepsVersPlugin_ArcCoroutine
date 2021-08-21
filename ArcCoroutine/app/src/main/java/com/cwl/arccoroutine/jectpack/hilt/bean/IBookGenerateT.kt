package com.cwl.arccoroutine.jectpack.hilt.bean

import javax.inject.Inject

/**
 * @Author cwl
 * @Date 2021/8/21 15:32
 * @Description
 */
interface IBookGenerateT<T> {
    fun generateBook(): T?
}

class BookGenerateTImpl<T> @Inject constructor() : IBookGenerateT<T> {
    var setT: T? = null
    override fun generateBook(): T? {
        return setT
    }

}