package com.cwl.common.exts

import com.cwl.common.BuildConfig

/**

 * @Author cwl

 * @Date 2021/4/28 18:05
    一些语法糖
 */

inline fun withTry(block:()->Unit){
    try{
        block()
    }catch (e:Throwable){
        if(BuildConfig.DEBUG) e.printStackTrace()
    }
}
