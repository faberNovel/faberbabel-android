package com.fabernovel.faberbabel.internal.data.model

internal sealed class StringResource {

    data class SimpleString(val value: String) : StringResource()

    data class PluralString(
        val zero: String?,
        val one: String?,
        val two: String?,
        val few: String?,
        val many: String?,
        val other: String?
    ) : StringResource()

    companion object {
        const val ZERO = "zero"
        const val ONE = "one"
        const val TWO = "two"
        const val FEW = "few"
        const val MANY = "many"
        const val OTHER = "other"
    }
}
