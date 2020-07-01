package com.fabernovel.faberbabel.internal.inflaterextend

import android.util.AttributeSet
import android.view.View
import android.widget.Toolbar

internal class ToolbarTransformer :
    Transformer {

    override fun transform(
        view: View?,
        attrs: AttributeSet?
    ): View? {
        if (view == null) {
            return view
        }
        val resources = view.context.resources
        for (index in 0 until attrs!!.attributeCount) {
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
            }
        }
        return view
    }

    private fun setTitleForView(view: View, text: String) {
        when (view) {
            is Toolbar -> view.title = text
            is androidx.appcompat.widget.Toolbar -> view.title = text
        }
    }

    companion object {
        private const val ATTRIBUTE_TITLE = "title"
        private const val ATTRIBUTE_ANDROID_TITLE = "android:title"
    }
}