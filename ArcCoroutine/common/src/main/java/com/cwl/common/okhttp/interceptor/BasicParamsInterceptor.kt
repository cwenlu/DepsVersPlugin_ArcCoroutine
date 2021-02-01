package com.cwl.okhttpdsl.http.interceptor

import okhttp3.*
import okio.Buffer

/**
 * 通用基础参数拦截器
 */
class BasicParamsInterceptor private constructor(
    var headersMap: HashMap<String, String>,
    var headersLineList: List<String>,
    var paramsMap: HashMap<String, Any>,
    var urlParamsMap: HashMap<String, Any>
) : Interceptor {

    private constructor(builder: Builder) : this(builder.headersMap, builder.headersLineList,builder.paramsMap, builder.urlParamsMap)


    companion object {
        inline fun build(block: Builder.() -> Unit): BasicParamsInterceptor = Builder().apply(block).build()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var requestBuilder = request.newBuilder()

        //headers
        //map形式
        var headerBuilder = request.headers.newBuilder()
        if (headersMap.isNotEmpty()) {
            headersMap.forEach {
                headerBuilder.add(it.key, it.value)
            }
            requestBuilder.headers(headerBuilder.build())
        }
        //字符串形式
        if (headersLineList.isNotEmpty()) {
            headersLineList.forEach {
                headerBuilder.add(it)
            }
            requestBuilder.headers(headerBuilder.build())
        }

        //params
        if (urlParamsMap.isNotEmpty()) {
            request = injectParamsIntoUrl(request.url.newBuilder(), requestBuilder)
        }

        var canInjectIntoBody = canInjectIntoBody(request)
        if (canInjectIntoBody.first) {
            when (canInjectIntoBody.second?.subtype) {
                "x-www-form-urlencoded" -> injectParams4Form(requestBuilder,
                    request.body!!,canInjectIntoBody.second!!)
                "form-data"->{
                    //
                }
                "json2List"->{
                    //
                }
            }
        }

        return chain.proceed(requestBuilder.build())
    }

    private fun injectParams4Form(requestBuilder:Request.Builder,requestBody: RequestBody,mediaType: MediaType) {
        if (paramsMap.isNotEmpty()) {
            var formBuilder = FormBody.Builder()
            paramsMap.forEach {
                formBuilder.add(it.key, it.value.toString())
            }
            var formBody = formBuilder.build()
            var originalBodyString = body2String(requestBody)
            originalBodyString+=if(originalBodyString.isNotEmpty()) "&" else "" + body2String(formBody)
            requestBuilder.post(RequestBody.create(mediaType,originalBodyString))
        }
    }

    private fun injectParams4FormData(){

    }

    private fun injectParamsIntoUrl(httpUrlBuilder: HttpUrl.Builder, requestBuilder: Request.Builder): Request {
        urlParamsMap.forEach {
            httpUrlBuilder.addQueryParameter(it.key, it.value.toString())
        }
        requestBuilder.url(httpUrlBuilder.build())
        return requestBuilder.build()
    }

    private fun canInjectIntoBody(request: Request): Pair<Boolean, MediaType?> {
        if ("POST" != request.method) return Pair(false, null)
        var body: RequestBody = request.body ?: return Pair(false, null)
        var contentType: MediaType = body.contentType() ?: return Pair(false, null)
        return Pair(true, contentType)
    }

    private fun body2String(requestBody: RequestBody):String{
        var buffer = Buffer()
        requestBody.writeTo(buffer)
        return buffer.readUtf8()
    }

    class Builder {
        //头
        var headersMap = hashMapOf<String, String>()
        //"key: value"形式头
        var headersLineList = arrayListOf<String>()
        //参数
        var paramsMap = hashMapOf<String, Any>()
        var urlParamsMap = hashMapOf<String, Any>()

        fun headers(vararg pairs: Pair<String, Any>) {
            pairs.forEach {
                headersMap[it.first] = it.second.toString()
            }
        }

        fun headers(vararg params:String){
            headersLineList.addAll(params)
        }

        fun params(vararg pairs: Pair<String, Any>) {
            pairs.forEach {
                paramsMap[it.first] = it.second
            }
        }

        fun params(vararg params:String){
            params.forEach {
                var split = it.split(":")
                if(split.size!=2) throw IllegalArgumentException("params err")
                paramsMap[split[0]] = split[1]
            }
        }

        fun urlParams(vararg pairs:Pair<String,Any>){
            pairs.forEach {
                urlParamsMap[it.first] = it.second
            }
        }

        fun urlParams(vararg params:String){
            params.forEach {
                var split = it.split(":")
                if(split.size!=2) throw IllegalArgumentException("params err")
                urlParamsMap[split[0]] = split[1]
            }
        }

        fun build(): BasicParamsInterceptor = BasicParamsInterceptor(this)
    }

}