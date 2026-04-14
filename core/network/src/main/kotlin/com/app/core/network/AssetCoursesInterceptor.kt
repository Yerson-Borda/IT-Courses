package com.app.core.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.MediaType.Companion.toMediaType

class AssetCoursesInterceptor(
    private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!request.url.encodedPath.endsWith("/courses")) {
            return chain.proceed(request)
        }

        val json = context.assets.open("courses.json").bufferedReader().use { reader ->
            reader.readText()
        }

        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .addHeader("content-type", "application/json")
            .body(
                json.toByteArray().toResponseBody(
                    "application/json; charset=utf-8".toMediaType()
                )
            )
            .build()
    }
}
