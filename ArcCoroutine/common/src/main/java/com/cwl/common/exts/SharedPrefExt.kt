package com.cwl.common.exts

import android.content.Context
import android.content.SharedPreferences
import com.cwl.common.util.ContextProvider

/**
 * 不推荐用sp
 */

fun sharedPref(name:String="shared_pref_data",mode:Int= Context.MODE_PRIVATE)=
    ContextProvider.appCtx!!.getSharedPreferences(name,mode)

fun <V> SharedPreferences.put(key:String,value:V)=edit().apply {
    when(value){
        is Int->putInt(key,value)
        is Float->putFloat(key,value)
        is Long->putLong(key,value)
        is Boolean->putBoolean(key,value)
        is String->putString(key,value)
        is Set<*>->putStringSet(key,value as? Set<String>?:throw IllegalArgumentException("Unsupported types"))
    }
}.apply()

fun SharedPreferences.remove(key:String)=edit().remove(key).apply()

fun SharedPreferences.clear()=edit().clear().apply()

fun Pair<String,*>.putSharePref()=sharedPref().put(first,second)

fun <V> String.putSharePref(value:V)=sharedPref().put(this,value)

inline fun <reified V> String.getSharePref(default:V?=null)=when(V::class){
    Int::class->sharedPref().getInt(this, (default?:-1) as Int)
    Float::class->sharedPref().getFloat(this, (default?:-1f) as Float)
    Long::class->sharedPref().getLong(this,(default?:-1L) as Long)
    Boolean::class->sharedPref().getBoolean(this, (default?:false) as Boolean)
    String::class->sharedPref().getString(this, default?.toString())
    Set::class->sharedPref().getStringSet(this,default as? MutableSet<String>)
    else->{}
}

fun String.removeSharePref()=sharedPref().remove(this)

fun clearSharePref()=sharedPref().clear()
