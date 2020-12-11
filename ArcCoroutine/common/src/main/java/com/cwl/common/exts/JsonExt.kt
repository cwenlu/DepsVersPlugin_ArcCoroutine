package com.cwl.common.exts

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**

 * @Author cwl

 * @Date 2019-08-24 11:54

 */

var simpleMoshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

fun generateMoshi(block:Moshi.Builder.()->Unit)=Moshi.Builder().apply(block).build()

inline fun <reified T> String.json2Bean(noinline block:(Moshi.Builder.()->Unit)?=null):T?=generateAdapter<T>(if(block==null) null else generateMoshi(block)).fromJson(this)

inline fun <reified T> T.bean2Json(noinline block:(Moshi.Builder.()->Unit)?=null)=generateAdapter<T>(if(block==null) null else generateMoshi(block)).toJson(this)


inline fun <reified T> String.json2List(noinline block:(Moshi.Builder.()->Unit)?=null):List<T>?=generateListAdapter<T>(if(block==null) null else generateMoshi(block)).fromJson(this)

inline fun <reified T> List<T>.list2Json(noinline block:(Moshi.Builder.()->Unit)?=null)=generateListAdapter<T>(if(block==null) null else generateMoshi(block)).toJson(this)

//map的V一般填充Any

inline fun <reified K,reified V> String.json2Map(noinline block:(Moshi.Builder.()->Unit)?=null):Map<K,V>?=generateMapAdapter<K,V>(if(block==null) null else generateMoshi(block)).fromJson(this)

inline fun <reified K,reified V> Map<K,V>.map2Json(noinline block:(Moshi.Builder.()->Unit)?=null)=generateMapAdapter<K,V>(if(block==null) null else generateMoshi(block)).toJson(this)

/**
 * 构造一般对象adapter
 */
inline fun <reified T> generateAdapter(moshi: Moshi?=null)=(moshi?:simpleMoshi).adapter(T::class.java)

/**
 * 构造list adapter
 */
inline fun <reified T> generateListAdapter(moshi: Moshi?=null): JsonAdapter<List<T>> {
    var newParameterizedType = Types.newParameterizedType(List::class.java, T::class.java)
    return (moshi?:simpleMoshi).adapter(newParameterizedType)
}

/**
 * 构造map adapter
 */
inline fun <reified K,reified V> generateMapAdapter(moshi: Moshi ?=null): JsonAdapter<Map<K, V>> {
    var newParameterizedType = Types.newParameterizedType(Map::class.java, K::class.java, V::class.java)
    return (moshi?:simpleMoshi).adapter(newParameterizedType)
}