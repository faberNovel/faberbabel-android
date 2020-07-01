package com.fabernovel.faberbabel.internal.inflaterextend

import android.util.Log
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

internal object ReflectionUtils {
    private val TAG = ReflectionUtils::class.java.simpleName
    fun getField(clazz: Class<*>, fieldName: String?): Field? {
        try {
            val f = clazz.getDeclaredField(fieldName!!)
            f.isAccessible = true
            return f
        } catch (ignored: NoSuchFieldException) {
        }
        return null
    }

    operator fun getValue(field: Field, obj: Any?): Any? {
        try {
            return field[obj]
        } catch (ignored: IllegalAccessException) {
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
