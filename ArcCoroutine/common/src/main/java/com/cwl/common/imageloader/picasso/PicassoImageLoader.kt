package com.cwl.common.imageloader.picasso

import android.content.Context
import android.widget.ImageView
import com.cwl.common.imageloader.ImageLoader
import com.cwl.common.imageloader.ImageLoaderOptions
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.io.File
import java.lang.reflect.ParameterizedType

open class PicassoImageLoader<ILO : ImageLoaderOptions>(var context: Context) :
    ImageLoader<ImageLoaderOptions> {

    private var PICASSO_CACHE="picasso-cache"
    private var lruCache:LruCache = LruCache(context)
    private var picasso:Picasso=Picasso.Builder(context.applicationContext).memoryCache(lruCache).build()

    fun config(block:Picasso.Builder.()->Unit){
        picasso=Picasso.Builder(context.applicationContext).apply {
            memoryCache(lruCache)
            block()
        }.build()
    }
    /**
     * 扩展重写了ImageLoaderOptions的同时可以继承PicassoImageLoader重写这个方法
     */
    protected open fun loadOptions(requestCreator: RequestCreator):RequestCreator{
        options()?.apply {
            if(targetWidth>0 && targetHeight>0){
                requestCreator.resize(targetWidth,targetHeight)
            }
            if(isCenterInside) requestCreator.centerInside()
            else if(isCenterCrop) requestCreator.centerCrop()

            if(placeholderResId!=null) requestCreator.placeholder(placeholderResId!!)
            if(errorResId!=null) requestCreator.error(errorResId!!)
            if(radius!=null) requestCreator.transform(RoundTransform(radius!!))
            requestCreator.config(config)
        }
        return requestCreator
    }

    /**
     * 如果继承PicassoImageLoader进行扩展，需要重写
     */
    override fun options(): ILO{
        var ilo = (javaClass.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0] as Class<ILO>
        return ilo.newInstance()

    }

    override fun load(imageView: ImageView, file: File) = loadOptions(picasso.load(file)).into(imageView)

    override fun load(imageView: ImageView, path: String) = loadOptions(picasso.load(path)).into(imageView)

    override fun load(imageView: ImageView, resId: Int) = loadOptions(picasso.load(resId)).into(imageView)

    override fun clearMemoryCache()=lruCache.clear()

    override fun clearDiskCache(){
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}