package com.fabernovel.faberbabel.internal.contextextend

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.view.LayoutInflater
import com.fabernovel.faberbabel.internal.data.service.XmlParser
import com.fabernovel.faberbabel.internal.inflaterextend.BottomNavigationViewTransformer
import com.fabernovel.faberbabel.internal.inflaterextend.FaberBabelLayoutInflater
import com.fabernovel.faberbabel.internal.inflaterextend.TextViewTransformer
import com.fabernovel.faberbabel.internal.inflaterextend.ToolbarTransformer
import com.fabernovel.faberbabel.internal.inflaterextend.ViewTransformerManager
import com.fabernovel.faberbabel.internal.resourceextend.FaberBabelResources
import com.fabernovel.faberbabel.internal.resourceextend.ResourcesManager

internal class FaberBabelContextWrapper(
    context: Context?,
    private val resourcesManager: ResourcesManager,
    private val xmlParser: XmlParser
) : ContextWrapper(context) {
    private var faberBabelResources: FaberBabelResources
    private val viewTransformerManager: ViewTransformerManager

    init {
        faberBabelResources = FaberBabelResources(
            this.baseContext.applicationContext,
            resourcesManager
        )
        viewTransformerManager = ViewTransformerManager(
            TextViewTransformer(),
            ToolbarTransformer(xmlParser, faberBabelResources),
            BottomNavigationViewTransformer(xmlParser, faberBabelResources)
        )
    }

    /**
     * Returns the Resources that was override by faberbabel
     *
     * @return An extension of Resources
     */
    override fun getResources(): Resources? {
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
