package com.cwl.okhttpdsl.http.config

import com.cwl.common.di.koin
import com.cwl.common.exts.bean2Json
import com.cwl.common.okhttp.RequestParams
import com.cwl.common.okhttp.core.ProgressListener
import com.cwl.common.okhttp.core.UploadProgressRequestBody
import com.cwl.okhttpdsl.http.exts.await
import com.cwl.okhttpdsl.http.util.MediaTypes
import okhttp3.*
import java.io.File

class RequestBuilder @JvmOverloads constructor(
    private var url: String = "",
    private var headers: ArrayList<Pair<String, String>> = arrayListOf(),
    private var params: ArrayList<Pair<String, Any>> = arrayListOf(),
    private var okHttpConfig: OkHttpConfig = koin.get()
) {
    /**
     * 下载文件保存名字/路径
     */
    var fileNameOrPath: String? = null
    var json:String?=null

    fun config(extend: Boolean = true, block: OkHttpConfig.() -> Unit): RequestBuilder {
        okHttpConfig.newConfig(extend).apply(block)
        return this
    }

    fun headers(vararg headers: Pair<String, Any>): RequestBuilder {
        headers.forEach { this.headers.add(Pair(it.first, "${it.second}")) }
        return this
    }

    fun headers(map: Map<String, Any>): RequestBuilder {
        map.forEach { this.headers.add(Pair(it.key, "${it.value}")) }
        return this
    }

    fun params(vararg params: Pair<String, Any>): RequestBuilder {
        params.forEach {
            this.params.add(
                Pair(
                    it.first,
                    if (it.second is File || it.second is Array<*> || it.second is List<*>) it.second else "${it.second}"
                )
            )
        }
        return this
    }

    fun params(map: Map<String, Any>): RequestBuilder {
        map.forEach {
            this.params.add(
                Pair(
                    it.key,
                    if (it.value is File || it.value is Array<*>) it.value else "${it.value}"
                )
            )
        }
        return this
    }

    fun json(json:String):RequestBuilder{
        this.json=json
        return this
    }

    inline fun <reified R:RequestParams> json(r:R):RequestBuilder{
         json=r.bean2Json()
         return this
     }

    /**
     * 保存文件路径
     * @isPath 是否为路径
     * @fileNameOrPath isPath==true 为路径 反之为文件名
     */
    fun saveFilePath(isPath: Boolean = false, fileNameOrPath: String) {
        if (isPath) this.fileNameOrPath = fileNameOrPath
        else this.fileNameOrPath =
            koin.get<OkHttpConfig>().diskCacheDirectory.absolutePath + File.separator + fileNameOrPath
    }


    private fun isMultiPart() = params.any { it.second is File || it.second is Array<*> || it.second is List<*> }
    private fun newCall(request: Request) = okHttpConfig.okHttpClien().newCall(request)
    private fun newCall(requestBody: RequestBody) = newCall(
        Request.Builder().url(
            if (url.startsWith("http://") || url.startsWith("https://")) url
            else okHttpConfig.baseUrl + url
        ).post(requestBody).build()
    )

    fun get() = newCall(buildGetRequest())

    fun postJson() = newCall(generateRequestBody(MediaTypes.APPLICATION_JSON_TYPE, json?:""))

    fun post(progressListener: ProgressListener? = null) = newCall(
        if (isMultiPart()) {
            if (progressListener != null) UploadProgressRequestBody(generatorMultipartBody(), progressListener)
            else generatorMultipartBody()
        } else generateFormBody()
    )


    suspend inline fun <reified T> getAwait() = get().await<T>()
    suspend inline fun <reified T> postJsonAwait() = postJson().await<T>()
    suspend inline fun <reified T> postAwait(noinline progressListener: ProgressListener? = null) =
        post(progressListener).await<T>()

    suspend inline fun dowloadAwait(get:Boolean=true,noinline progressListener: ProgressListener?) = (if(get)get() else post()).await<File>(
        if (fileNameOrPath.isNullOrEmpty()) null
        else File(fileNameOrPath), progressListener
    )


    private fun buildGetRequest(): Request {
        var parse: HttpUrl? = HttpUrl.parse(
            if (url.startsWith("http://") || url.startsWith("https://")) url
            else okHttpConfig.baseUrl + url
        ) ?: throw RuntimeException("HttpUrl.parse error")
        var build = parse!!.newBuilder().apply {
            params.forEach {
                addQueryParameter(it.first, "${it.second}")
            }
        }.build()
        return Request.Builder().url(build).apply {
            headers.forEach {
                addHeader(it.first, it.second)
            }
        }.build()
    }

    /**
     * 构建文本请求体 eg: application/json2List;charset=utf-8
     */
    private fun generateRequestBody(mediaType: MediaType?, str: String) = RequestBody.create(mediaType, str)

    private fun generateFormBody(): FormBody = FormBody.Builder().apply {
        params.forEach {
            add(it.first, "${it.second}")
        }
    }.build()

    /**
     * 文件参数混
     */
    private fun generatorMultipartBody() = MultipartBody.Builder().setType(MultipartBody.FORM).apply {
        params.forEach {
            when (it.second) {
                is File -> (it.second as File).apply {
                    val fileType = OkHttpConfig.getMimeType(name)
                    addFormDataPart(it.first, name, RequestBody.create(MediaType.parse(fileType), this))
                }
                is Array<*> -> (it.second as? Array<File>)?.forEach { file ->
                    val fileType = OkHttpConfig.getMimeType(file.name)
                    addFormDataPart(it.first, file.name, RequestBody.create(MediaType.parse(fileType), file))
                }
                is List<*> -> (it.second as? List<File>)?.forEach { file ->
                    val fileType = OkHttpConfig.getMimeType(file.name)
                    addFormDataPart(it.first, file.name, RequestBody.create(MediaType.parse(fileType), file))
                }
                else -> addFormDataPart(it.first, "${it.second}")
            }
        }
    }.build()


}