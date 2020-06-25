package com.fabernovel.faberbabel.internal.resourceextend

import com.fabernovel.faberbabel.R
import android.content.Context
import android.content.res.Resources

internal class FaberbabelResources(
    context: Context,
    private val resourcesManager: ResourcesManager
) : Resources(context.assets, context.resources.displayMetrics, context.resources.configuration) {

    /**
     * Returns the resource String from faberbabel cache.
     * If the key of target resource is not present in faberbabel cache, the default String resource
     * from android is returned
     * In case if there is no resource associated to given id in default android resources, throw
     * "NotFoundException"
     *
     * @param id The identifier of target String resource
     * @return A String resource
     */
    @Throws(NotFoundException::class)
    override fun getString(id: Int): String {
        val wordingKey = this.getResourceEntryName(id)
        val resource = resourcesManager.getString(wordingKey)
        return if (resource != null) {
            resource
        } else {
            super.getString(id)
        }
    }

    /**
     * Returns the resource String from faberbabel cache.
     * If the key of target resource is not present in faberbabel cache, the default String resource
     * from android is returned
     * In case if there is no resource associated to given id in default android resources, throw
     * "NotFoundException"
     *
     * @param id The identifier of target String resource
     * @param formatArgs Arguments that will be replaced in formatted String
     * @return A String resource
     */
    @Throws(NotFoundException::class)
    override fun getString(
        id: Int,
        vararg formatArgs: Any?
    ): String {
        val wordingKey = this.getResourceEntryName(id)
        val resource = resourcesManager.getString(wordingKey, formatArgs)
        return if (resource != null) {
            resource
        } else {
            super.getString(id)
        }
    }

    /**
     * Returns the resource String from faberbabel cache.
     * If the key of target resource is not present in faberbabel cache, the default String resource
     * from android is returned
     * In case if there is no resource associated to given id in default android resources, throw
     * "NotFoundException"
     *
     * @param id The identifier of target String resource
     * @return A String resource in char sequence format
     */
    @Throws(NotFoundException::class)
    override fun getText(id: Int): CharSequence {
        val wordingKey = this.getResourceEntryName(id)
        val resource = resourcesManager.getText(wordingKey)
        return if (resource != null) {
            resource
        } else {
            super.getText(id)
        }
    }

    /**
     * Returns the resource String from faberbabel cache.
     * If the key of target resource is not present in faberbabel cache, the default String resource
     * from android is returned
     * In case if there is no resource associated to given id in default android resources, throw
     * "NotFoundException"
     *
     * @param id The identifier of target String resource
     * @param def The default CharSequence to return.
     * @return A CharSequence resource
     */
    @Throws(NotFoundException::class)
    override fun getText(
        id: Int,
        def: CharSequence?
    ): CharSequence? {
        val wordingKey = this.getResourceEntryName(id)
        val resource = resourcesManager.getText(wordingKey)
        return if (resource != null) {
            resource
        } else {
            super.getText(id, def)
        }
    }

    /**
     * Returns the resource String from faberbabel cache.
     * If the key of target resource is not present in faberbabel cache, the default String resource
     * from android is returned
     * In case if there is no resource associated to given id in default android resources, throw
     * "NotFoundException"
     *
     * @param id The identifier of target String resource
     * @param quantity An Integer that define plural type.
     * @param formatArgs Arguments that will be replaced in formatted String
     * @return A String resource
     */
    @Throws(NotFoundException::class)
    override fun getQuantityString(
        id: Int,
        quantity: Int,
        vararg formatArgs: Any?
    ): String {
        val wordingKey = this.getResourceEntryName(id)
        val quantityType = super.getQuantityText(R.plurals.fbb_plurals_types, quantity)
        val resource = resourcesManager.getQuantityString(wordingKey, quantityType, formatArgs)
        return if (resource != null) {
            resource
        } else {
            super.getQuantityString(id, quantity, formatArgs)
        }
    }

    /**
     * Returns the resource String from faberbabel cache.
     * If the key of target resource is not present in faberbabel cache, the default String resource
     * from android is returned
     * In case if there is no resource associated to given id in default android resources, throw
     * "NotFoundException"
     *
     * @param id The identifier of target String resource
     * @param quantity An Integer that define plural type.
     * @return A CharSequence resource
     */
    @Throws(NotFoundException::class)
    override fun getQuantityText(
        id: Int,
        quantity: Int
    ): CharSequence {
        val wordingKey = this.getResourceEntryName(id)
        val quantityType = super.getQuantityText(R.plurals.fbb_plurals_types, quantity)
        val resource = this.resourcesManager.getQuantityText(wordingKey, quantityType)
        return if (resource != null) {
            resource
        } else {
            super.getQuantityText(id, quantity)
        }
    }
}
