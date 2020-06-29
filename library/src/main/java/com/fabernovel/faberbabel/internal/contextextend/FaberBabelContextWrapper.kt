package com.fabernovel.faberbabel.internal.contextextend

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import com.fabernovel.faberbabel.internal.resourceextend.FaberBabelResources
import com.fabernovel.faberbabel.internal.resourceextend.ResourcesManager

internal class FaberBabelContextWrapper(
    context: Context?,
    private val resourcesManager: ResourcesManager
) : ContextWrapper(context) {
    private var faberBabelResources: Resources? = null

    /**
     * Returns the Resources that was override by faberbabel
     *
     * @return An extension of Resources
     */
    override fun getResources(): Resources? {
        if (this.faberBabelResources == null) {
            this.faberBabelResources = FaberBabelResources(
                this.baseContext.applicationContext,
                resourcesManager
            )
        }
        return this.faberBabelResources
    }
}
