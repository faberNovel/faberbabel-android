package com.fabernovel.faberbabel.internal.core

import com.fabernovel.faberbabel.internal.data.model.StringResource
import com.fabernovel.faberbabel.internal.resourceextend.ResourcesManager
import java.util.IllegalFormatException

internal class WordingResourcesManager(private val repository: WordingRepository) :
    ResourcesManager {
    override fun getString(wordingKey: String): String? {
        val resource = getResource(wordingKey)
        return when (resource) {
            is StringResource.SimpleString -> resource.value
            is StringResource.PluralString ->
                throw IllegalArgumentException(Errors.NOT_PLURAL_RESOURCE_ERROR)
            null -> null
        }
    }

    override fun getString(wordingKey: String, formatArgs: Array<out Any?>): String? {
        val resource = getResource(wordingKey)
        return when (resource) {
            is StringResource.SimpleString -> {
                try {
                    resource.value.format(formatArgs)
                } catch (exception: IllegalFormatException) {
                    null
                }
            }

            is StringResource.PluralString ->
                throw IllegalArgumentException(Errors.NOT_PLURAL_RESOURCE_ERROR)
            null -> null
        }
    }

    override fun getText(wordingKey: String): CharSequence? {
        val resource = getResource(wordingKey)
        return when (resource) {
            is StringResource.SimpleString -> {
                resource.value
            }
            is StringResource.PluralString ->
                throw IllegalArgumentException(Errors.NOT_PLURAL_RESOURCE_ERROR)
            null -> null
        }
    }

    override fun getQuantityString(
        wordingKey: String,
        quantity: CharSequence,
        formatArgs: Array<out Any?>
    ): String? {
        val resource = getResource(wordingKey)
        return when (resource) {
            is StringResource.SimpleString ->
                throw IllegalArgumentException(Errors.NOT_SIMPLE_RESOURCE_ERROR)
            is StringResource.PluralString ->
                try {
                    matchQuantityType(resource, quantity)?.format(formatArgs)
                } catch (exception: IllegalFormatException) {
                    null
                }
            null -> null
        }
    }

    override fun getQuantityText(wordingKey: String, quantity: CharSequence): CharSequence? {
        val resource = getResource(wordingKey)
        return when (resource) {
            is StringResource.SimpleString ->
                throw IllegalArgumentException(Errors.NOT_SIMPLE_RESOURCE_ERROR)
            is StringResource.PluralString -> matchQuantityType(resource, quantity)
            null -> null
        }
    }

    private fun matchQuantityType(
        resource: StringResource.PluralString,
        quantityType: CharSequence
    ): String? {
        return when (quantityType) {
            ZERO -> resource.zero
            ONE -> resource.one
            TWO -> resource.two
            FEW -> resource.few
            MANY -> resource.many
            OTHER -> resource.other
            else -> throw IllegalArgumentException(Errors.NOT_RIGHT_QUANTITY_TYPE_ERROR)
        }
    }

    private fun getResource(resourceKey: String): StringResource? {
        val wording = repository.getWording()
        return if (wording != null) {
            wording[resourceKey]
        } else null
    }

    companion object {
        private const val ZERO = "zero"
        private const val ONE = "one"
        private const val TWO = "two"
        private const val FEW = "few"
        private const val MANY = "many"
        private const val OTHER = "other"
    }
}
