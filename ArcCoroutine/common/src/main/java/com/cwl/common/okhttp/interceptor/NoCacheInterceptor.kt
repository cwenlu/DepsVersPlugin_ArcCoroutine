
import okhttp3.Interceptor
import okhttp3.Response

class NoCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().header("Cache-Control", "no-cache").build()
        var originalResponse = chain.proceed(request)
        originalResponse = originalResponse.newBuilder().header("Cache-Control", "no-cache").build()
        return originalResponse
    }

}