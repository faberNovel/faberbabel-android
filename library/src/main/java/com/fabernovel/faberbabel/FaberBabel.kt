package com.fabernovel.faberbabel

import android.content.Context
import android.content.ContextWrapper
import com.fabernovel.faberbabel.internal.contextextend.FaberBabelContextWrapper
import com.fabernovel.faberbabel.internal.resourceextend.ResourcesManager
import com.fabernovel.faberbabel.internal.core.WordingRepository
import com.fabernovel.faberbabel.internal.core.WordingResourcesManager
import com.fabernovel.faberbabel.internal.data.WordingRepositoryImpl

class FaberBabel {
    private val repository: WordingRepository
    private val resourcesManager: ResourcesManager

    init {
        repository = WordingRepositoryImpl()
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
        return FaberBabelContextWrapper(context, resourcesManager)
    }
}
