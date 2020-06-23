package com.fabernovel.faberbabel.appwording.contextextend

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import com.fabernovel.faberbabel.appwording.resourceextend.FaberbabelResources
import com.fabernovel.faberbabel.appwording.resourceextend.ResourcesManager

internal class FaberbabelContextWrapper(
    context: Context?,
    private val resourcesManager: ResourcesManager
) : ContextWrapper(context) {
    var faberbabelResources: Resources? = null

    /**
     * Returns the Resources that was override by faberbabel
     *
     * @return An extension of Resources
     */
    override fun getResources(): Resources? {
        if (this.faberbabelResources == null) {
            this.faberbabelResources = FaberbabelResources(
                this.baseContext.applicationContext,
                resourcesManager
            )
        }
        return this.faberbabelResources
    }
}