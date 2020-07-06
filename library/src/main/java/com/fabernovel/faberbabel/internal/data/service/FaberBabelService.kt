package com.fabernovel.faberbabel.internal.data.service

import com.fabernovel.faberbabel.internal.data.model.Config
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

internal class FaberBabelService(private val okHttpClient: OkHttpClient) {
    fun fetchWording(config: Config, callback: Callback) {
        val url = config.url
        val urlData = url.split("/|://".toRegex())
        val httpUrlBuilder = HttpUrl.Builder()
        httpUrlBuilder.scheme(urlData[SCHEME_INDEX])
        httpUrlBuilder.host(urlData[HOST_INDEX])
        for (pathSegment in urlData.subList(FIRST_PATH_SEGMENT_INDEX, urlData.lastIndex + 1)) {
            httpUrlBuilder.addPathSegment(pathSegment)
        }
        httpUrlBuilder.addPathSegment(config.projectId)
        httpUrlBuilder.addQueryParameter(PLATFORM_PARAMETER_KEY, PLATFORM_PARAMETER_VALUE)
        httpUrlBuilder.addQueryParameter(LANGUAGE_PARAMETER_VALUE, config.languageLocal)
        val httpUrl = httpUrlBuilder.build()
        val request = Request.Builder()
            .url(httpUrl)
            .build()

        okHttpClient.newCall(request).enqueue(callback)
    }

    companion object {
        private const val SCHEME_INDEX = 0
        private const val HOST_INDEX = 1
        private const val FIRST_PATH_SEGMENT_INDEX = 2
        private const val PLATFORM_PARAMETER_KEY = "platform"
        private const val PLATFORM_PARAMETER_VALUE = "android"
        private const val LANGUAGE_PARAMETER_VALUE = "language"
    }
}
