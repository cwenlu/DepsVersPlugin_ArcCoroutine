package com.cwl.depsversplugin

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

/**

 * @Author cwl

 * @Date 2020/12/10 14:29

 */

//https://developer.android.google.cn/jetpack/androidx/migrate/class-mappings#androidsupportv7  androidx 类对照表
//https://developer.android.google.cn/jetpack/androidx/migrate/artifact-mappings androidx 依赖对照表

object Versions {
    val kotlin = "1.4.0"
    val appcompat = "1.2.0"
    val recyclerview = "1.1.0"
    val recyclerview_selection = "1.0.0"
    val timber = "4.7.1"
    val lifecycle = "2.2.0"
    val moshi_kotlin = "1.11.0"
    val PersistentCookieJar = "v1.0.1"
    val picasso = "2.71828"
    val glide = "4.11.0"
    val okhttp = "4.9.0"
    val kotlinx_coroutines_core = "1.4.2"
    val koin = "2.2.1"
    val datastore = "1.0.0-alpha05"
    val room = /*"2.2.6"*/"2.3.0-alpha04"
    val paging = "3.0.0-alpha11"
    val startup = "1.0.0"
    val chuck = "1.1.0"
    val leakcanary = "2.5"
    val mapstruct_kotlin = "1.3.1.2"
    val mapstruct = "1.4.1.Final"

    val material = "1.3.0-rc01"

    //widget
    val vbpd = "1.3.1"
    val swiperefreshlayout = "1.2.0-alpha01"
    val viewpager2 = "1.1.0-alpha01"
    val coordinatorlayout = "1.1.0"
    val constraintlayout = "2.0.1"
    val drawerlayout = "1.1.1"
    val customview = "1.1.0"

    //compose
    val compose_material = "1.0.0-alpha11"

    //classpath
    val hugo = "1.2.1"
    val gradle_versions_plugin = "0.36.0"
}


object Okttp {
    // okhttp cookie 持久化  maven { url "https://jitpack.io" }
    val PersistentCookieJar =
        "com.github.franmontiel:PersistentCookieJar:${Versions.PersistentCookieJar}"

    //https://github.com/square/okhttp
    val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"

    //内部依赖了oktt3，使用后不需要再添加okttp3  （处理文件下载上传好像会影响进度回调）
    val okhttp3_logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"

}

object Coroutines {
    val kotlinx_coroutines_core =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinx_coroutines_core}"
    val kotlinx_coroutines_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinx_coroutines_core}"
}

object Image {
    val picasso = "com.squareup.picasso:picasso:${Versions.picasso}"
    val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    val glide_compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
}

object Json {
    // json处理
    val moshi_kotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi_kotlin}"

    //kapt moshi用注解处理器时配置
    val moshi_kotlin_codegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi_kotlin}"
}

object Widget {
    object RecyclerView {
        val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
        val recyclerview_selection =
            "androidx.recyclerview:recyclerview-selection:${Versions.recyclerview_selection}"
    }

    val swiperefreshlayout =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swiperefreshlayout}"
    val paging_runtime = "androidx.paging:paging-runtime:${Versions.paging}"
    val viewpager2 = "androidx.viewpager2:viewpager2:${Versions.viewpager2}"
    val coordinatorlayout =
        "androidx.coordinatorlayout:coordinatorlayout:${Versions.coordinatorlayout}"
    val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    val drawerlayout = "androidx.drawerlayout:drawerlayout:${Versions.drawerlayout}"

    //ViewDragHelper子这里面,recyclerview,drawerlayout等内部都依赖了它
    val customview = "androidx.customview:customview:${Versions.customview}"

    //原来的com.android.support:design  com.google.android.material:material-rc01 没找到
    val material = "com.google.android.material:material:${Versions.material}"

    object Compose {
        val material = "androidx.compose.material:material:${Versions.compose_material}"
    }
}


object DataStore {
    val datastore_preferences = "androidx.datastore:datastore-preferences:${Versions.datastore}"
    val datastore = "androidx.datastore:datastore:${Versions.datastore}"
}


object Room {
    val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    val room_compiler = "androidx.room:room-compiler:${Versions.room}"

    // optional - Kotlin Extensions and Coroutines support for Room
    val room_ktx = "androidx.room:room-ktx:${Versions.room}"
}

/*一些通用的*/
object Deps {


    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"


}

object MapStruct {
    val mapstruct_kotlin = "com.github.pozo:mapstruct-kotlin:${Versions.mapstruct_kotlin}"
    val mapstruct_kotlin_processor =
        "com.github.pozo:mapstruct-kotlin-processor:${Versions.mapstruct_kotlin}"

    val mapstruct = "org.mapstruct:mapstruct:${Versions.mapstruct}"
}

object HelperUtil {

    //方便手机查看日志
    object Chuck {
        //    debugCompile 'com.readystatesoftware.chuck:library:1.1.0'
        //    releaseCompile 'com.readystatesoftware.chuck:library-no-op:1.1.0'

        //https://github.com/jgilfelt/chuck
        val chuck = "com.readystatesoftware.chuck:library:${Versions.chuck}"
        val chuck_no_op = "com.readystatesoftware.chuck:library-no-op:${Versions.chuck}"
    }

    //https://github.com/JakeWharton/timber/
    val timber = "com.jakewharton.timber:timber:${Versions.timber}"

    //debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.5'
    //https://github.com/square/leakcanary
    val leakcanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakcanary}"
}

object ViewBinding {
    //https://github.com/kirich1409/ViewBindingPropertyDelegate
    val viewbindingpropertydelegate =
        "com.kirich1409.viewbindingpropertydelegate:viewbindingpropertydelegate:${Versions.vbpd}"
    val vbpd_noreflection =
        "com.kirich1409.viewbindingpropertydelegate:vbpd-noreflection:${Versions.vbpd}"
}


object StartUp {
    //一般初始化，需要application context，避免开发手动就采用ContentProvider方式，但是因为它是在Application之前执行，如果数量变多会导致启动变慢
    val startup_runtime = "androidx.startup:startup-runtime:${Versions.startup}"
}


//https://github.com/InsertKoinIO/koin
object Koin {
    // Koin for Kotlin
    val koin_core = "org.koin:koin-core:${Versions.koin}"

    // Koin for Android
    val koin_android = "org.koin:koin-android:${Versions.koin}"

    // Koin extended & experimental features
    val koin_core_ext = "org.koin:koin-core-ext:${Versions.koin}"

    // Koin AndroidX Scope features
    val koin_androidx_scope = "org.koin:koin-androidx-scope:${Versions.koin}"

    // Koin AndroidX ViewModel features
    val koin_androidx_viewmodel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"

    // Koin AndroidX Fragment features
    val koin_androidx_fragment = "org.koin:koin-androidx-fragment:${Versions.koin}"

    // Koin AndroidX WorkManager
    val koin_androidx_workmanager = "org.koin:koin-androidx-workmanager:${Versions.koin}"

    // Koin AndroidX Jetpack Compose
    val koin_androidx_compose = "org.koin:koin-androidx-compose:${Versions.koin}"

    // Koin AndroidX Experimental features
    val koin_androidx_ext = "org.koin:koin-androidx-ext:${Versions.koin}"

    val koin_gradle_plugin = "org.koin:koin-gradle-plugin:${Versions.koin}"

}

object Lifecycle {

    // ViewModel
    val lifecycle_viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    // LiveData
    val lifecycle_livedata_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"

    // Lifecycles only (without ViewModel or LiveData)
    val lifecycle_runtime_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"

    // Saved state module for ViewModel
    val lifecycle_viewmodel_savedstate =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycle}"

    // Annotation processor  ====kapt
    val lifecycle_compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"

    // alternately - if using Java8, use the following instead of lifecycle-compiler
    val lifecycle_common_java8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"

    // optional - helpers for implementing LifecycleOwner in a Service
    val lifecycle_service = "androidx.lifecycle:lifecycle-service:${Versions.lifecycle}"

    // optional - ProcessLifecycleOwner provides a lifecycle for the whole application process
    val lifecycle_process = "androidx.lifecycle:lifecycle-process:${Versions.lifecycle}"

    // optional - ReactiveStreams support for LiveData
    val lifecycle_reactivestreams_ktx =
        "androidx.lifecycle:lifecycle-reactivestreams-ktx:${Versions.lifecycle}"
}

object Kt {
    val stdlibJdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    val test = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}

object Plugin {

    //https://github.com/JakeWharton/hugo
    // 可打印方法耗时，入参，返回值
    val hugo = "com.jakewharton.hugo:hugo-plugin:${Versions.hugo}"


    //https://github.com/ben-manes/gradle-versions-plugin 检查依赖版本
    val gradle_versions_plugin =
        "com.github.ben-manes:gradle-versions-plugin:${Versions.gradle_versions_plugin}"
}


/**
 * 项目通用的一些依赖配置(创建项目生成的)
 */
fun Project.generalCommonDeps(kotlinVersion: String) {
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
fun Project.clspath(vararg deps: Any) {
    dependencies {
        deps.forEach {
            "classpath"(it)
        }
    }
}

/**
 * 注册多个implementation方式的依赖 模块内使用
 */
fun Project.impl(vararg deps: Any) {
    dependencies {
        deps.forEach {
            "implementation"(it)
        }
    }

}

/**
 * 注册多个api方式的依赖 会传递
 */
fun Project.api(vararg deps: Any) {
    dependencies {
        deps.forEach {
            "api"(it)
        }
    }
}

/**
 * 注册多个编译时注解器
 */
fun Project.kapt(vararg deps: Any) {
    dependencies {
        deps.forEach {
            "kapt"(it)
        }
    }
}

/**
 * 注册多个只编译时用的依赖
 */
fun Project.cmpOnly(vararg deps: Any) {
    dependencies {
        deps.forEach {
            "compileOnly"(it)
        }
    }
}