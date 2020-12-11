package com.cwl.common.exts

import java.util.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**

 * @Author cwl

 * @Date 2019-08-24 09:57

 */

/**
 * bean转map
 */
fun Any.toMap():Map<String, Any>{
    val map = HashMap<String,Any>()
    val declaredFields = javaClass.declaredFields
    for (declaredField in declaredFields) {
        declaredField.isAccessible = true
        try {
            val value = declaredField.get(this)
            if (value != null) {
                map.put(declaredField.name, value)
            }

        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }
    //屏蔽冗余
    //https://segmentfault.com/q/1010000008078789
    map.remove("\$change")
    map.remove("serialVersionUID")
    return map
}

/**
 * 获取一个对象某个字段的值
 * 只是类自身的属性不包含父类
 */
fun Any.getPropertyValue(propertyName:String):Any?{
    try {
        var declaredField = javaClass.getDeclaredField(propertyName)
        declaredField.isAccessible=true
        return declaredField.get(this)
    }catch (e:Exception){
        e.printStackTrace()
    }
    return null
}

/**
 * 设置一个对象某个字段
 * 只是类自身的属性不包含父类
 * @return 成功true，不成功false
 */
fun Any.setPropertyValue(propertyName:String,value:Any?):Boolean{
    try {
        var declaredField = javaClass.getDeclaredField(propertyName)
        declaredField.isAccessible=true
        declaredField.set(this,value)
        return true
    }catch (e:Exception){
        e.printStackTrace()
    }
    return false
}

/**
 * 一个对象是否包含某个字段
 */
fun Any.containsProperty(propertyName:String):Boolean{
    javaClass.declaredFields.forEach {
        if(it.name==propertyName) return true
    }
    return false
}

/**
 * 复制属性值到目标对象
 * @isCover 是否保留target不为null的属性值，true保留（默认），false不保留
 */
fun Any.copyTo(target:Any,isCover:Boolean=true){
    var sourceFields=javaClass.declaredFields
    sourceFields.forEach {
        var propertyValue = getPropertyValue(it.name)
        if(isCover){
            if(target.getPropertyValue(it.name)==null && propertyValue!=null){
                target.setPropertyValue(it.name,propertyValue)
            }
        }else{
            target.setPropertyValue(it.name,propertyValue)
        }
    }
}

/**
 * 返回类属性值封装信息
 */
fun Any.description()
        = javaClass.kotlin.memberProperties
    .map {
        it.isAccessible=true
        "${it.name}: ${it.get(this@description)}"
    }
    .joinToString(separator = " , ",prefix = "[",postfix = "]")