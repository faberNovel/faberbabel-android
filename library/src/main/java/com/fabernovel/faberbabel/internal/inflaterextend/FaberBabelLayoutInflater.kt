package com.fabernovel.faberbabel.internal.inflaterextend

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View

internal class FaberBabelLayoutInflater(
    original: LayoutInflater?,
    newContext: Context?,
    private val viewTransformerManager: ViewTransformerManager,
    cloned: Boolean
) : LayoutInflater(original, newContext) {

    init {
        if (!cloned) {
            initFactories()
        }
    }

    override fun cloneInContext(newContext: Context): LayoutInflater {
        return FaberBabelLayoutInflater(this, newContext, viewTransformerManager, true)
    }

    private fun initFactories() {
        if (factory2 != null) {
            this.setFactory2(factory2)
        }
        if (factory != null) {
            this.setFactory(factory)
        }
    }

    override fun setFactory(factory: Factory) {
        if (factory !is WrapperFactory) {
            super.setFactory(WrapperFactory(factory))
        } else {
            super.setFactory(factory)
        }
    }

    override fun setFactory2(factory2: Factory2) {
        if (factory2 !is WrapperFactory2) {
            super.setFactory2(WrapperFactory2(factory2))
        } else {
            super.setFactory2(factory2)
        }
    }

    @Throws(ClassNotFoundException::class)
    override fun onCreateView(
        name: String,
        attrs: AttributeSet
    ): View? {
        for (prefix in sClassPrefixList) {
            try {
                val view = createView(name, prefix, attrs)
                if (view != null) {
                    return applyChange(view, attrs)
                }
            } catch (e: ClassNotFoundException) {
                // no-op
            }
        }
        return super.onCreateView(name, attrs)
    }

    private fun applyChange(
        view: View?,
        attrs: AttributeSet
    ): View? {
        return viewTransformerManager.transform(view, attrs)
    }

    private inner class WrapperFactory internal constructor(private val factory: Factory) :
        Factory {
        override fun onCreateView(
            name: String,
            context: Context,
            attrs: AttributeSet
        ): View? {
            val view = factory.onCreateView(name, context, attrs)
            return applyChange(view, attrs)
        }
    }

    private inner class WrapperFactory2 internal constructor(private val factory2: Factory2) :
        Factory2 {
        override fun onCreateView(
            parent: View?,
            name: String,
            context: Context,
            attrs: AttributeSet
        ): View? {
            val view = factory2.onCreateView(parent, name, context, attrs)
            return applyChange(view, attrs)
        }

        override fun onCreateView(
            name: String,
            context: Context,
            attrs: AttributeSet
        ): View? {
            val view = factory2.onCreateView(name, context, attrs)
            return applyChange(view, attrs)
        }
    }

    companion object {
        private val sClassPrefixList = arrayOf(
            "android.widget.",
            "android.webkit.",
            "android.app."
        )
    }
}