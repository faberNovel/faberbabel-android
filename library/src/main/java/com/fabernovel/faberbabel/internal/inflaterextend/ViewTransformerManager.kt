package com.fabernovel.faberbabel.internal.inflaterextend

import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

internal class ViewTransformerManager(
    private val textViewTransformer: TextViewTransformer,
    private val toolbarTransformer: ToolbarTransformer,
    private val bottomNavigationViewTransformer: BottomNavigationViewTransformer
) {

    /**
     * The manager tries to match a transformers for each view
     * and return the final result as a new view if the required transformer exist.
     *
     * @param view  that will be transformed.
     * @param attrs attributes of the view.
     * @return the transformed view.
     */
    fun transform(view: View?, attrs: AttributeSet?): View? {
        if (view == null) {
            return null
        }
        return when (view) {
            is TextView -> textViewTransformer.transform(view, attrs)
            is Toolbar -> toolbarTransformer.transform(view, attrs)
            is android.widget.Toolbar -> toolbarTransformer.transform(view, attrs)
            is BottomNavigationView -> bottomNavigationViewTransformer.transform(view, attrs)
            else -> view
        }
    }

    companion object {
        const val ATTRIBUTE_PREFIX = "@"
    }
}

internal interface Transformer {

    fun transform(
        view: View?,
        attrs: AttributeSet?
    ): View?
}
