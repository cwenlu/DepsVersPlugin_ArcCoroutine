package com.cwl.common.exts

import android.content.Context
import android.content.res.Resources
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment

/**

 * @Author cwl

 * @Date 2019-08-31 16:49

 */

fun Context.toColor(@ColorRes colorRes: Int,theme:Resources.Theme?=null)=ResourcesCompat.getColor(resources,colorRes,theme)

fun Fragment.toColor(@ColorRes colorRes: Int,theme:Resources.Theme?=null)=context?.toColor(colorRes,theme)

/**

 * 资源名字转成资源id

 *

 */
fun Context.resName2Id(resType:String="mipmap",resName:String)=
getResources().getIdentifier(resName,resType,getPackageName())

fun Fragment.resName2Id(resType:String="mipmap",resName:String)
=context?.resName2Id(resType,resName)