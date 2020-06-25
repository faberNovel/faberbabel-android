package com.fabernovel.faberbabel.internal.contextextend

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import com.fabernovel.faberbabel.internal.resourceextend.FaberbabelResources
import com.fabernovel.faberbabel.internal.resourceextend.ResourcesManager

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
