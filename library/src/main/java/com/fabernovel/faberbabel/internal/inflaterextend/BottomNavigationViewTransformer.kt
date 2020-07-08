package com.fabernovel.faberbabel.internal.inflaterextend

import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import com.fabernovel.faberbabel.internal.data.service.MenuItem
import com.fabernovel.faberbabel.internal.data.service.XmlParser
import com.fabernovel.faberbabel.internal.resourceextend.FaberBabelResources
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * This class purpose is to edit the widget text field during its xml file inflation to inject
 * FaberBabel wording so that it replace the default wording of the string resource present in the
 * application.
 */
internal class BottomNavigationViewTransformer(
    private val xmlParser: XmlParser,
    private val resources: FaberBabelResources
) : Transformer {

    override fun transform(
        view: View?,
        attrs: AttributeSet?
    ): View? {
        if (view == null || attrs == null) {
            return view
        }

        val resources = view.context.resources
        for (index in 0 until attrs.attributeCount) {
            val attributeName = attrs.getAttributeName(index)
            when (attributeName) {
                ATTRIBUTE_APP_MENU, ATTRIBUTE_MENU -> {
                    val value = attrs.getAttributeValue(index)
                    if (
                        value != null &&
                        value.startsWith(ViewTransformerManager.ATTRIBUTE_PREFIX)
                    ) {
                        val resourceId = attrs.getAttributeResourceValue(index, 0)
                        val menuItems: List<MenuItem> = getMenuItems(resources, resourceId)
                        for (menuItem in menuItems) {
                            if (menuItem.itemId != 0) {
                                setMenuItemForView(view, menuItem)
                            }
                        }
                    }
                }
            }
        }
        return view
    }

    private fun getMenuItems(resources: Resources?, resourceId: Int): List<MenuItem> {
        if(resources != null) {
            val parser = resources.getLayout(resourceId)
            return xmlParser.parseMenuXml(parser)
        }
        return emptyList()
    }

    private fun setMenuItemForView(view: View, menuItem: MenuItem) {
        when (view) {
            is BottomNavigationView -> {
                if (menuItem.titleId != 0 ) {
                    view.menu.findItem(menuItem.itemId).title =
                        resources.getString(menuItem.titleId)
                }

                if (menuItem.condensedTitleId != 0 ) {
                    view.menu.findItem(menuItem.itemId).title =
                        resources.getString(menuItem.condensedTitleId)
                }
            }
        }
    }

    companion object {
        private const val ATTRIBUTE_MENU = "menu"
        private const val ATTRIBUTE_APP_MENU = "app:menu"
    }
}
