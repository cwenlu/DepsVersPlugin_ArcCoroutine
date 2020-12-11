package com.cwl.depsversplugin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
/**

 * @Author cwl

 * @Date 2020/12/10 14:29

 */

object Versions{
    val kotlin="1.4.0"
    val appcompat = "1.2.0"
    val recyclerview="1.1.0"
    val recyclerview_selection="1.0.0"
    val timber = "4.7.1"
    val lifecycle="2.2.0"
    val moshi_kotlin="1.11.0"
    val PersistentCookieJar="v1.0.1"
    val picasso="2.71828"
    val glide="4.11.0"
    val okhttp="4.9.0"
    val kotlinx_coroutines_core="1.4.2"
    val koin="2.2.1"

    //classpath
    val hugo="1.2.1"
}





object Okttp{
    // okhttp cookie 持久化  maven { url "https://jitpack.io" }
    val PersistentCookieJar="com.github.franmontiel:PersistentCookieJar:${Versions.PersistentCookieJar}"

    //https://github.com/square/okhttp
    val okhttp="com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    //内部依赖了oktt3，使用后不需要再添加okttp3
    val okhttp3_logging_interceptor="com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"

}

object Coroutines{
    val kotlinx_coroutines_core="org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinx_coroutines_core}"
}

object Image{
    val picasso="com.squareup.picasso:picasso:${Versions.picasso}"
    val glide="com.github.bumptech.glide:glide:${Versions.glide}"
}

object Json{
    // json处理
    val moshi_kotlin="com.squareup.moshi:moshi-kotlin:${Versions.moshi_kotlin}"
}

object Recyclerview{
    val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    val recyclerview_selection="androidx.recyclerview:recyclerview-selection:${Versions.recyclerview_selection}"
}

/*一些通用的*/
object Deps{

    //https://github.com/JakeWharton/timber/
    val timber="com.jakewharton.timber:${Versions.timber}"

    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"

}

object Compiler{
    val glide_compiler="com.github.bumptech.glide:compiler:${Versions.glide}"

    // Annotation processor  ====kapt
    val lifecycle_compiler="androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"

    //kapt moshi用注解处理器时配置
    val moshi_kotlin_codegen="com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi_kotlin}"
}

//https://github.com/InsertKoinIO/koin
object Koin{
    // Koin for Kotlin
    val koin_core="org.koin:koin-core:${Versions.koin}"
    // Koin extended & experimental features
    val koin_core_ext="org.koin:koin-core-ext:${Versions.koin}"

    // Koin AndroidX Scope features
    val koin_androidx_scope="org.koin:koin-androidx-scope:${Versions.koin}"
    // Koin AndroidX ViewModel features
    val koin_androidx_viewmodel="org.koin:koin-androidx-viewmodel:${Versions.koin}"
    // Koin AndroidX Fragment features
    val koin_androidx_fragment="org.koin:koin-androidx-fragment:${Versions.koin}"
    // Koin AndroidX WorkManager
    val koin_androidx_workmanager="org.koin:koin-androidx-workmanager:${Versions.koin}"
    // Koin AndroidX Jetpack Compose
    val koin_androidx_compose="org.koin:koin-androidx-compose:${Versions.koin}"
    // Koin AndroidX Experimental features
    val koin_androidx_ext="org.koin:koin-androidx-ext:${Versions.koin}"
}

object Lifecycle{

    // ViewModel
    val lifecycle_viewmodel_ktx="androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    // LiveData
    val lifecycle_livedata_ktx="androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"

    // Lifecycles only (without ViewModel or LiveData)
    val lifecycle_runtime_ktx="androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"

    // Saved state module for ViewModel
    val lifecycle_viewmodel_savedstate="androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycle}"

//    // Annotation processor  ====kapt
//    val lifecycle_compiler="androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"

    // alternately - if using Java8, use the following instead of lifecycle-compiler
    val lifecycle_common_java8="androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"

    // optional - helpers for implementing LifecycleOwner in a Service
    val lifecycle_service="androidx.lifecycle:lifecycle-service:${Versions.lifecycle}"

    // optional - ProcessLifecycleOwner provides a lifecycle for the whole application process
    val lifecycle_process="androidx.lifecycle:lifecycle-process:${Versions.lifecycle}"

    // optional - ReactiveStreams support for LiveData
    val lifecycle_reactivestreams_ktx="androidx.lifecycle:lifecycle-reactivestreams-ktx:${Versions.lifecycle}"
}

object Kt {
    val stdlibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    val test = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}

object Plugin{

    //https://github.com/JakeWharton/hugo
    // 可打印方法耗时，入参，返回值
    val hugo="com.jakewharton.hugo:hugo-plugin:${Versions.hugo}"

    val koin="org.koin:koin-gradle-plugin:${Versions.koin}"

}


/**
 * 项目通用的一些依赖配置(创建项目生成的)
 */
fun Project.projectGeneralDeps(kotlinVersion: String) {
    dependencies {
        "testImplementation"("junit:junit:4.12")
        "androidTestImplementation"("androidx.test:runner:1.2.0")
        "androidTestImplementation"("androidx.test.espresso:espresso-core:3.2.0")
        "implementation"(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
        "implementation"(kotlin("stdlib-jdk7", kotlinVersion))
    }
}


/**
 * 注册多个插件classpath
 */
fun Project.clspath(vararg deps:Any){
    dependencies{
        deps.forEach {
            "classpath"(it)
        }
    }
}

/**
 * 注册多个implementation方式的依赖
 */
fun Project.impl(vararg deps:String){
    dependencies{
        deps.forEach {
            "implementation"(it)
        }
    }

}

/**
 * 注册多个api方式的依赖
 */
fun Project.api(vararg deps:String){
    dependencies{
        deps.forEach {
            "api"(it)
        }
    }
}

/**
 * 注册多个编译时注解器
 */
fun Project.kapt(vararg deps:String){
    dependencies{
        deps.forEach {
            "kapt"(it)
        }
    }
}

/**
 * 注册多个只编译时用的依赖
 */
fun Project.cmpOnly(vararg deps:String){
    dependencies{
        deps.forEach {
            "compileOnly"(it)
        }
    }
}