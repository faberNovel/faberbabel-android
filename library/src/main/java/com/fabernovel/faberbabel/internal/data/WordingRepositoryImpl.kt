package com.fabernovel.faberbabel.internal.data

import android.content.Context
import com.fabernovel.faberbabel.internal.core.WordingRepository
import com.fabernovel.faberbabel.internal.data.model.Config
import com.fabernovel.faberbabel.internal.data.model.StringResource
import com.fabernovel.faberbabel.internal.data.service.FaberBabelService
import com.fabernovel.faberbabel.internal.data.service.XmlParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class WordingRepositoryImpl(
    private val service: FaberBabelService,
    private val xmlParser: XmlParser,
    private val context: Context
) : WordingRepository {
    private var wordingMap: Map<String, StringResource>? = null

    override fun getWording(): Map<String, StringResource>? {
        if (wordingMap == null) {
            runBlocking {
                fetchWordingFromCache()
            }
        }
        return wordingMap
    }

    override suspend fun fetchWording(wordingConfig: Config) {
        withContext(Dispatchers.IO) {
            val wordingFetchResponse = awaitForFetchWordingServiceResponse(wordingConfig)
            when (wordingFetchResponse) {
                is WordingResponse.WordingResources -> {
                    wordingMap = wordingFetchResponse.resources
                }
                is WordingResponse.Error -> {
                    fetchWordingFromCache()
                }
            }
        }
    }

    private suspend fun fetchWordingFromCache() {
        withContext(Dispatchers.IO) {
            val file = File(context.cacheDir, CACHE_FILE_NAME)
            if (file.exists()) {
                wordingMap = xmlParser.parseXml(file.readText())
            }
        }
    }

    private suspend fun awaitForFetchWordingServiceResponse(wordingConfig: Config): WordingResponse {
        return suspendCoroutine { cont ->
            service.fetchWording(
                wordingConfig,
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        cont.resume(WordingResponse.Error)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val xmlResponse = response.body?.string()
                        saveWordingToCache(xmlResponse)
                        val wordingResponse = xmlParser.parseXml(xmlResponse)
                        cont.resume(WordingResponse.WordingResources(wordingResponse))
                    }
                }
            )
        }
    }

    private fun saveWordingToCache(xmlResponse: String?) {
        if (xmlResponse != null) {
            val file = File(context.cacheDir, CACHE_FILE_NAME)
            file.writeText(xmlResponse)
        }
    }

    companion object {
        private const val CACHE_FILE_NAME = "cached_wording.xml"
    }
}

internal sealed class WordingResponse {
    data class WordingResources(
        val resources: Map<String, StringResource>
    ) : WordingResponse()

    object Error : WordingResponse()
}
