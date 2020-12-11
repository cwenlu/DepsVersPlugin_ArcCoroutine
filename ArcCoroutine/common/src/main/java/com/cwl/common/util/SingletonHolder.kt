package com.cwl.okhttpdsl.http.util

/**
 * 带参单例生成类
 * eg:
 * class Singleton private constructor(val string: String){
 *      companion object : SingletonHolder<Singleton,String>(::Singleton)
 * }
 * Singleton.getInstance("sss")
 *
 * 如果多个入参A,可以考虑再多增加几个类
 *
 */
open class SingletonHolder<out T,in A>(private val creator:(A)->T){
    @Volatile
    private var instance:T?=null
    fun getInstance(arg:A):T{
        if(instance!=null){
            return instance!!
        }
        return synchronized(this){
            if(instance!=null) instance!!
            else{
                instance=creator(arg)
                return instance!!
            }
        }
    }
}

