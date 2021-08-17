package com.cwl.common.exts

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Environment

/**

 * @Author cwl

 * @Date 2020-07-17 13:31

 */



/**
 * return the path of /system
 */
fun rootPath()= Environment.getRootDirectory().absolutePath


/**
 * return the path of /data
 */
fun dataPath()= Environment.getDataDirectory().absolutePath


/**
 * return the path of /cache
 */
fun downloadCachePath()= Environment.getDownloadCacheDirectory().absolutePath


//-------------/data/data/package--------------

/**
 * Return the path of /data/data/package
 */
fun Context.internalDataPath()= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) dataDir.absolutePath else applicationInfo.dataDir

/**
 * Return the path of /data/data/package/code_cache
 */
fun Context.internalCodeCachePath()=if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) codeCacheDir.absolutePath else applicationInfo.dataDir+"/code_cache"

/**
 * Return the path of /data/data/package/cache
 */
fun Context.internalCachePath()=cacheDir.absolutePath

/**
 * Return the path of /data/data/package/databases
 */
fun Context.internalDbPath()=internalDataPath()+"/databases"

/**
 * Return the path of /data/data/package/databases/name
 */
fun Context.internalDbPath(name:String)=getDatabasePath(name).absolutePath

/**
 * Return the path of /data/data/package/files
 */
fun Context.internalFilesPath()=filesDir.absolutePath

/**
 * Return the path of /data/data/package/shared_prefs
 */
fun Context.internalSpPath()=internalDataPath()+"/shared_prefs"

fun Context.internalNoBackupPath()=if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) noBackupFilesDir.absolutePath else internalDataPath()+"/no_backup"




//--------------------/storage/emulated/0/Android/data/package------------------------


/**
 * Return the path of /storage/emulated/0/Android/data/package.
 */
fun Context.externalDataPath()=if(externalStorageEnable()) externalCacheDir?.parent else ""

/**
 * Return the path of /storage/emulated/0/Android/data/package/cache
 */
fun Context.externalCachePath()=if(externalStorageEnable()) externalCacheDir?.absolutePath else ""



/**
 * if type is null,Return the path of /storage/emulated/0/Android/data/package/files
 * if type is "Environment.DIRECTORY_MUSIC",Return the path of /storage/emulated/0/Android/data/package/files/Music
 */
fun Context.externalPath4Type(type:String?)=if(externalStorageEnable()){
    if(type=="Documents"/*Environment.DIRECTORY_DOCUMENTS*/ && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) getExternalFilesDir(null)?.absolutePath+"/Documents"
    else getExternalFilesDir(type)?.absolutePath
} else ""



//---------------/storage/emulated/0/Android/obb/package-------------------

fun Context.externalObbPath()=if(externalStorageEnable()) obbDir.absolutePath else ""












//---------------------/storage/emulated/0-----都过时了------------------

private fun externalStorageEnable()=Environment.MEDIA_MOUNTED==Environment.getExternalStorageState()

/**
 * Return the path of /storage/emulated/0
 */
fun externalStoragePath()=if(externalStorageEnable()) Environment.getExternalStorageDirectory().absolutePath else ""

/**
 * Return the path of /storage/emulated/0/Music
 */
fun externalMusicPath()=if(externalStorageEnable()) Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath else ""

/**
 * Return the path of /storage/emulated/0/Podcasts
 */
fun externalPodcastsPath()=if(externalStorageEnable()) Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS).absolutePath else ""

/**
 * Return the path of /storage/emulated/0/Ringtones
 */
fun externalRingtonesPath()=if(externalStorageEnable()) Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).absolutePath else ""

/**
 * Return the path of /storage/emulated/0/Alarms
 */
fun externalAlarmsPath()=if(externalStorageEnable()) Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).absolutePath else ""

/**
 * Return the path of /storage/emulated/0/Notifications
 */
fun externalNotificationsPath()=if(externalStorageEnable()) Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_NOTIFICATIONS).absolutePath else ""

/**
 * Return the path of /storage/emulated/0/Movies
 */
fun externalMoviesPath()=if(externalStorageEnable()) Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).absolutePath else ""

/**
 * Return the path of /storage/emulated/0/Download
 */
fun externalDownloadsPath()=if(externalStorageEnable()) Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath else ""

/**
 * Return the path of /storage/emulated/0/DCIM
 */
fun externalDcimPath()=if(externalStorageEnable()) Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath else ""

/**
 * Return the path of /storage/emulated/0/Documents
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
fun externalDocumentsPath()=if(externalStorageEnable()) Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).absolutePath else ""


