package com.cwl.common.exts

import android.graphics.Bitmap
import android.os.Build

/**

 * @Author cwl

 * @Date 2021/1/13 14:30

 */

//最低版本越来越高，差不多可以忽略

/*图片所占内存大小*/
val Bitmap.size
    get() = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) allocationByteCount
    else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) byteCount
    else rowBytes*height

