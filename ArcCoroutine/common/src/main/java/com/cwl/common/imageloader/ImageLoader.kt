package com.cwl.common.imageloader

import android.widget.ImageView
import java.io.File

interface ImageLoader<ILO : ImageLoaderOptions> {
    /**
     * 获取loader的options
     */
    fun options():ILO

    fun load(imageView: ImageView,file:File)
    fun load(imageView: ImageView,path:String)
    fun load(imageView: ImageView,resId:Int)

    fun clearMemoryCache()
    fun clearDiskCache()
}