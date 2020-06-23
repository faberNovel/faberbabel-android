package com.fabernovel.faberbabel.appwording

import android.content.Context
import android.content.ContextWrapper
import com.fabernovel.faberbabel.appwording.contextextend.FaberbabelContextWrapper
import com.fabernovel.faberbabel.appwording.resourceextend.ResourcesManager
import com.fabernovel.faberbabel.core.WordingRepository
import com.fabernovel.faberbabel.core.WordingResourcesManager
import com.fabernovel.faberbabel.data.WordingRepositoryImpl

class FaberbabelSDK {
    private var repository: WordingRepository? = null
    private var resourcesManager: ResourcesManager? = null

    init {
        repository = WordingRepositoryImpl()
        resourcesManager = WordingResourcesManager(repository!!)
    }

    /**
     * Returns the ContextWrapper that was override by faberbabel
     * This method must be called for each activity by overriding attachBaseContext
     *
     * @param Context that is obtained from attachBaseContext parameters
     * @return An extension of ContextWrapper
     */
    fun provideBabelContext(context: Context?): ContextWrapper {
        return FaberbabelContextWrapper(context, resourcesManager!!)
    }
}
