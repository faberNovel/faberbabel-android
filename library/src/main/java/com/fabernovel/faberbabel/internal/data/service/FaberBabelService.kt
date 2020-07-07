package com.fabernovel.faberbabel.internal.data.service

import com.fabernovel.faberbabel.internal.data.model.Config
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception

internal class FaberBabelService(private val okHttpClient: OkHttpClient) {
    fun fetchWording(config: Config, callback: Callback) {
        try {
            val httpUrl = config.url.toHttpUrl()
                .newBuilder()
                .addPathSegment(config.projectId)
                .addQueryParameter(PLATFORM_PARAMETER_KEY, PLATFORM_PARAMETER_VALUE)
                .addQueryParameter(LANGUAGE_PARAMETER_VALUE, config.languageLocal)
                .build()
            val request = Request.Builder()
                .url(httpUrl)
                .build()

            okHttpClient.newCall(request).enqueue(callback)
        } catch (exception: Exception) {
            // no-op
        }
    }

    companion object {
        private const val PLATFORM_PARAMETER_KEY = "platform"
        private const val PLATFORM_PARAMETER_VALUE = "android"
        private const val LANGUAGE_PARAMETER_VALUE = "language"
    }
}
