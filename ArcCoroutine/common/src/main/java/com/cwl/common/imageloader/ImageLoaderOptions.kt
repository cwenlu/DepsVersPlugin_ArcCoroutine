package com.cwl.common.imageloader

import android.graphics.Bitmap

open class ImageLoaderOptions {
    var placeholderResId:Int?=null
    var errorResId:Int?=null
    var isCenterCrop:Boolean=false
    var isCenterInside:Boolean=false
    var targetWidth:Int=-1
    var targetHeight:Int=-1
    var radius:Float?=null
    var config:Bitmap.Config=Bitmap.Config.RGB_565
}