package com.cwl.common.okhttp.core

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import timber.log.Timber

/**
 * 上传进度body
 */
class UploadProgressRequestBody @JvmOverloads constructor(var requestBody: RequestBody,var progressListener: ProgressListener?=null) : RequestBody() {

    override fun contentType(): MediaType? =requestBody.contentType()

    override fun contentLength(): Long {
        try {
           return requestBody.contentLength()
        }catch (ex:Exception){
            ex.printStackTrace()
        }
        return -1
    }

    override fun writeTo(sink: BufferedSink) {
        var countingSink=CountingSink(sink)
        var bufferedSink = countingSink.buffer()
        requestBody.writeTo(bufferedSink)
        bufferedSink.flush()
    }


    inner class CountingSink(sink: Sink) : ForwardingSink(sink){
        var currentLength=0L
        var totalLength=0L

        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            currentLength+=byteCount
            if(totalLength==0L) totalLength=contentLength()
            progressListener?.invoke(currentLength,totalLength,100.0f*currentLength/totalLength)
            Timber.i("currentLength:$currentLength,totalLength:$totalLength")
        }
    }
}