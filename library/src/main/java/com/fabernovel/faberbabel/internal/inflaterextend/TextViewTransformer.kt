package com.fabernovel.faberbabel.internal.inflaterextend

import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.fabernovel.faberbabel.internal.inflaterextend.ViewTransformerManager.Companion.ATTRIBUTE_PREFIX
import com.fabernovel.faberbabel.internal.resourceextend.FaberBabelResources

/**
 * This class purpose is to edit the widget text field during its xml file inflation to inject
 * FaberBabel wording so that it replace the default wording of the string resource present in the
 * application.
 */
internal class TextViewTransformer(
    private val resources: FaberBabelResources
) : Transformer {

    override fun transform(
        view: View?,
        attrs: AttributeSet?
    ): View? {
        if (view == null || attrs == null) {
            return view
        }
        for (index in 0 until attrs.attributeCount) {
            val attributeName = attrs.getAttributeName(index)
            when (attributeName) {
                ATTRIBUTE_ANDROID_TEXT, ATTRIBUTE_TEXT -> {
                    val value = attrs.getAttributeValue(index)
                    if (value != null && value.startsWith(ATTRIBUTE_PREFIX)) {
                        setTextForView(
                            view,
                            resources.getString(attrs.getAttributeResourceValue(index, 0))
                        )
                    }
                }
                ATTRIBUTE_ANDROID_HINT, ATTRIBUTE_HINT -> {
                    val value = attrs.getAttributeValue(index)
                    if (value != null && value.startsWith(ATTRIBUTE_PREFIX)) {
                        setHintForView(
                            view,
                            resources.getString(attrs.getAttributeResourceValue(index, 0))
                        )
                    }
                }
            }
        }
        return view
    }

    private fun setTextForView(view: View, text: String) {
        (view as TextView).text = text
    }

    private fun setHintForView(view: View, text: String) {
        (view as TextView).hint = text
    }

    companion object {
        private const val ATTRIBUTE_TEXT = "text"
        private const val ATTRIBUTE_ANDROID_TEXT = "android:text"
        private const val ATTRIBUTE_HINT = "hint"
        private const val ATTRIBUTE_ANDROID_HINT = "android:hint"
    }
}