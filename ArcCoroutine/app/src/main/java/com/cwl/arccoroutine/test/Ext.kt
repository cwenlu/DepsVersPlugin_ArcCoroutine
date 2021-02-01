package com.cwl.arccoroutine

import android.content.Context
import android.content.Intent
import kotlin.reflect.KClass

/**

 * @Author cwl

 * @Date 2019-09-13 14:55

 */

fun Context.showActivity(kclazz:KClass<*>){
    startActivity(Intent(this,kclazz.java))
}