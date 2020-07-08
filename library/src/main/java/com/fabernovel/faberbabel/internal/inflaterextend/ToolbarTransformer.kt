package com.fabernovel.faberbabel.internal.inflaterextend

import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import android.widget.Toolbar
import com.fabernovel.faberbabel.internal.data.service.MenuItem
import com.fabernovel.faberbabel.internal.data.service.XmlParser
import com.fabernovel.faberbabel.internal.resourceextend.FaberBabelResources

/**
 * This class purpose is to edit the widget text field during its xml file inflation to inject
 * FaberBabel wording so that it replace the default wording of the string resource present in the
 * application.
 */
internal class ToolbarTransformer(
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
                ATTRIBUTE_ANDROID_TITLE, ATTRIBUTE_TITLE -> {
                    val value = attrs.getAttributeValue(index)
                    if (
                        value != null &&
                        value.startsWith(ViewTransformerManager.ATTRIBUTE_PREFIX)
                    ) {
                        setTitleForView(
                            view,
                            resources.getString(
                                attrs.getAttributeResourceValue(index, 0)
                            )
                        )
                    }
                }

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
        if (resources != null) {
            val parser = resources.getLayout(resourceId)
            return xmlParser.parseMenuXml(parser)
        }
        return emptyList()
    }

    private fun setTitleForView(view: View, text: String) {
        when (view) {
            is Toolbar -> view.title = text
            is androidx.appcompat.widget.Toolbar -> view.title = text
        }
    }

    private fun setMenuItemForView(view: View, menuItem: MenuItem) {
        when (view) {
            is Toolbar -> {
                if (menuItem.titleId != 0) {
                    view.menu.findItem(menuItem.itemId).title =
                        resources.getString(menuItem.titleId)
                }

                if (menuItem.condensedTitleId != 0) {
                    view.menu.findItem(menuItem.itemId).title =
                        resources.getString(menuItem.condensedTitleId)
                }
            }
            is androidx.appcompat.widget.Toolbar -> {
                if (menuItem.titleId != 0) {
                    view.menu.findItem(menuItem.itemId).title =
                        resources.getString(menuItem.titleId)
                }

                if (menuItem.condensedTitleId != 0) {
                    view.menu.findItem(menuItem.itemId).title =
                        resources.getString(menuItem.condensedTitleId)
                }
            }
        }
    }

    companion object {
        private const val ATTRIBUTE_TITLE = "title"
        private const val ATTRIBUTE_ANDROID_TITLE = "android:title"
        private const val ATTRIBUTE_MENU = "menu"
        private const val ATTRIBUTE_APP_MENU = "app:menu"
    }
}
