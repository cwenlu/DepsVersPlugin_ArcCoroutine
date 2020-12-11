package com.cwl.common.okhttp.core

/**
 * 进度回调方法
 */
typealias ProgressListener=(currentLength:Long,totalLength:Long,percent:Float)->Unit