// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlin_version by extra("1.4.21")
    repositories {
        google()
        jcenter()
        
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath(kotlin("gradle-plugin",version= "1.4.0"))
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    // 这个 id 就是在 versionPlugin 文件夹下 build.gradle 文件内定义的id
    id("com.cwl.depsversplugin") apply false
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url="https://jitpack.io")
    }
}

subprojects {
    apply(plugin="com.cwl.depsversplugin")
    if (name == "app") {
        //判断是否是某个子项目，然后做操作
    }
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}


