package com.fabernovel.faberbabel.internal.contextextend

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.view.LayoutInflater
import com.fabernovel.faberbabel.internal.inflaterextend.FaberBabelLayoutInflater
import com.fabernovel.faberbabel.internal.inflaterextend.TextViewTransformer
import com.fabernovel.faberbabel.internal.inflaterextend.ToolbarTransformer
import com.fabernovel.faberbabel.internal.inflaterextend.ViewTransformerManager
import com.fabernovel.faberbabel.internal.resourceextend.FaberBabelResources
import com.fabernovel.faberbabel.internal.resourceextend.ResourcesManager

internal class FaberBabelContextWrapper(
    context: Context?,
    private val resourcesManager: ResourcesManager
) : ContextWrapper(context) {
    private var faberBabelResources: Resources? = null
    private val viewTransformerManager: ViewTransformerManager

    init {
        viewTransformerManager = ViewTransformerManager(
            TextViewTransformer(),
            ToolbarTransformer()
        )
    }

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

    override fun getSystemService(name: String): Any? {
        return when (name) {
            LAYOUT_INFLATER -> {
                FaberBabelLayoutInflater(
                    LayoutInflater.from(baseContext),
                    this,
                    viewTransformerManager,
                    true
                )
            }
            else -> super.getSystemService(name)
        }
    }

    companion object {
        private const val LAYOUT_INFLATER = "layout_inflater"
    }
}
