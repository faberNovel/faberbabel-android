package com.fabernovel.faberbabel

import android.content.Context
import android.content.ContextWrapper
import com.fabernovel.faberbabel.internal.contextextend.FaberBabelContextWrapper
import com.fabernovel.faberbabel.internal.resourceextend.ResourcesManager
import com.fabernovel.faberbabel.internal.core.WordingRepository
import com.fabernovel.faberbabel.internal.core.WordingResourcesManager
import com.fabernovel.faberbabel.internal.data.WordingRepositoryImpl
import com.fabernovel.faberbabel.internal.data.model.Config
import com.fabernovel.faberbabel.internal.data.service.XmlParser
import com.fabernovel.faberbabel.internal.data.service.FaberBabelService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient

class FaberBabel(
    private val applicationContext: Context
) {
    private val repository: WordingRepository
    private val resourcesManager: ResourcesManager
    private val xmlParser: XmlParser

    init {
        val okHttpClient = OkHttpClient()
        val service = FaberBabelService(okHttpClient)
        xmlParser = XmlParser()
        repository = WordingRepositoryImpl(service, xmlParser, applicationContext)
        resourcesManager = WordingResourcesManager(repository)
    }

    /**
     * Returns the ContextWrapper that was override by faberbabel
     * This method must be called for each activity by overriding attachBaseContext
     *
     * @param Context that is obtained from attachBaseContext parameters
     * @return An extension of ContextWrapper
     */
    fun provideFaberBabelContextWrapper(context: Context): ContextWrapper {
        return FaberBabelContextWrapper(context, resourcesManager, xmlParser)
    }

    /**
     * Fetch the wording in remote and save it in cache asynchronously. This mean that if the
     * remote wording is pretty heavy and/or internet connection is slow, the first screen on first
     * application start will use the default wording. And on next application start, the wording
     * be fetched from cache before it's updating.
     * This method must be called on start of application or when its language is updated
     *
     * @param Config a data class that contains the url of service, the project id and the
     * language code : "en","fr", "ua", "ru"...
     */
    fun asyncFetchFaberBabelWording(wordingConfig: Config) {
        val scope = CoroutineScope(Job())
        scope.launch {
            repository.fetchWording(wordingConfig)
        }
    }

    /**
     * Fetch the wording in remote and save it in cache synchronously. This mean that if the
     * internet connection is slow, the application will wait until the service give a response or
     * an error. In second case it's the previous cache that will be used, or if there is no cache,
     * the default wording will appears.
     * This method must be called on start of application or when its language is updated
     *
     * @param Config a data class that contains the url of service, the project id and the
     * language code : "en","fr", "ua", "ru"...
     */
    fun syncFetchFaberBabelWording(wordingConfig: Config) {
        runBlocking {
            repository.fetchWording(wordingConfig)
        }
    }
}
