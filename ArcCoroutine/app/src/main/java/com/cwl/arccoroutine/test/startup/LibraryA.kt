package com.cwl.arccoroutine.test.startup

import android.content.Context
import androidx.startup.Initializer

/**

 * @Author cwl

 * @Date 2020/12/24 10:10

 */
class LibraryA:Initializer<LibraryA.Dependency> {

    class Dependency{
        //init返回的对象
    }

    override fun create(context: Context): Dependency {
        //进行初始化
        return Dependency()
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}

class LibraryB:Initializer<LibraryB.Dependency>{
    class Dependency{
        //init返回的对象
    }

    override fun create(context: Context): Dependency {
        //进行初始化
        return LibraryB.Dependency()
    }

    //初始化的依赖关系
    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf(LibraryA::class.java)
    }
}

//LibraryB 依赖 LibraryA，先初始化A，然后B

