buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        //gradle kotlin 插件
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
        classpath(kotlin("gradle-plugin",version= "1.4.21"))
    }
}


apply(plugin = "kotlin")
apply(plugin = "java-gradle-plugin")
plugins{
    `kotlin-dsl`//内部定义好的一个快捷
}

repositories {
    // 需要添加 jcenter 否则会提示找不到 gradlePlugin
    jcenter()
}

dependencies {
    implementation(gradleApi())
//    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.21")
}

kotlin{
    //
}

//compileKotlin {
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//}
//compileTestKotlin {
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//}


gradlePlugin {
    plugins {
        create("DepsVersPlugin"){
            id = "com.cwl.depsversplugin"
            implementationClass = "com.cwl.depsversplugin.DepsVersPlugin"
        }
    }
}