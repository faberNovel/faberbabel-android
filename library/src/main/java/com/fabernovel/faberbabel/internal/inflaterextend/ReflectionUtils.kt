package com.fabernovel.faberbabel.internal.inflaterextend

import android.util.Log
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

internal object ReflectionUtils {
    private val TAG = ReflectionUtils::class.java.simpleName
    fun getField(clazz: Class<*>, fieldName: String): Field? {
        try {
            val field = clazz.getDeclaredField(fieldName)
            field.isAccessible = true
            return field
        } catch (ignored: NoSuchFieldException) {
            // no-op
        }
        return null
    }

    operator fun getValue(field: Field?, obj: Any?): Any? {
        if (field == null) {
            return null
        }
        try {
            return field[obj]
        } catch (ignored: IllegalAccessException) {
            // no-op
        }
        return null
    }

    operator fun setValue(
        field: Field,
        obj: Any?,
        value: Any?
    ) {
        try {
            field[obj] = value
        } catch (ignored: IllegalAccessException) {
            // no-op
        }
    }

    fun getMethod(clazz: Class<*>, methodName: String): Method? {
        val methods = clazz.methods
        for (method in methods) {
            if (method.name == methodName) {
                method.isAccessible = true
                return method
            }
        }
        return null
    }

    fun invokeMethod(
        `object`: Any?,
        method: Method?,
        vararg args: Any?
    ) {
        try {
            if (method == null) return
            method.invoke(`object`, *args)
        } catch (e: IllegalAccessException) {
            Log.d(TAG, "Can't invoke method using reflection", e)
        } catch (e: InvocationTargetException) {
            Log.d(TAG, "Can't invoke method using reflection", e)
        }
    }
}
