package com.fabernovel.faberbabel.internal.inflaterextend

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.xmlpull.v1.XmlPullParser
import java.lang.reflect.Field

internal class FaberBabelLayoutInflater(
    original: LayoutInflater?,
    newContext: Context?,
    private val viewTransformerManager: ViewTransformerManager,
    cloned: Boolean
) : LayoutInflater(original, newContext) {

    private var constructorArgs: Field? = null
    private var isPrivateFactorySet = false

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

    private inner class WrapperFactory internal constructor(
        private val factory: Factory
    ) :
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

    private inner class WrapperFactory2 internal constructor(
        private val factory2: Factory2
    ) :
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

    private fun createCustomViewInternal(
        view: View?,
        name: String,
        viewContext: Context,
        attrs: AttributeSet
    ): View? {
        var currentView = view
        if (view == null && name.indexOf('.') > -1) {
            if (constructorArgs == null) {
                constructorArgs =
                    ReflectionUtils.getField(LayoutInflater::class.java, "mConstructorArgs")
            }
            val constructorArgsList =
                ReflectionUtils.getValue(constructorArgs, this) as Array<Any?>?

            if (constructorArgsList != null && constructorArgsList.isNotEmpty()) {
                val lastContext = constructorArgsList[0]

                constructorArgsList[0] = viewContext
                try {
                    currentView = createView(name, null, attrs)
                } catch (ignored: ClassNotFoundException) {
                    // no-op
                } finally {
                    constructorArgsList[0] = lastContext
                }
            }
        }
        return currentView
    }

    private fun setPrivateFactoryInternal() {
        if (isPrivateFactorySet) return
        if (context !is Factory2) {
            isPrivateFactorySet = true
            return
        }
        val setPrivateFactoryMethod = ReflectionUtils
            .getMethod(LayoutInflater::class.java, "setPrivateFactory")
        if (setPrivateFactoryMethod != null) {
            val newFactory = PrivateWrapperFactory2(context as Factory2)
            ReflectionUtils.invokeMethod(
                this,
                setPrivateFactoryMethod,
                newFactory
            )
        }
        isPrivateFactorySet = true
    }

    override fun inflate(
        parser: XmlPullParser?,
        root: ViewGroup?,
        attachToRoot: Boolean
    ): View? {
        setPrivateFactoryInternal()
        return super.inflate(parser, root, attachToRoot)
    }

    private inner class PrivateWrapperFactory2 constructor(
        private val factory2: Factory2
    ) :
        Factory2 {
        override fun onCreateView(
            parent: View?,
            name: String,
            context: Context,
            attrs: AttributeSet
        ): View? {
            var view = factory2.onCreateView(parent, name, context, attrs)
            view = createCustomViewInternal(view, name, context, attrs)
            return applyChange(view, attrs)
        }

        override fun onCreateView(
            name: String,
            context: Context,
            attrs: AttributeSet
        ): View? {
            var view = factory2.onCreateView(name, context, attrs)
            view = createCustomViewInternal(view, name, context, attrs)
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