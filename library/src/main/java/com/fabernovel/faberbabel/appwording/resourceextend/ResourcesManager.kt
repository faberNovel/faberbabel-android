package com.fabernovel.faberbabel.appwording.resourceextend

internal interface ResourcesManager {
    fun getString(wordingKey: String): String?
    fun getString(wordingKey: String, formatArgs: Array<out Any?>): String?
    fun getText(wordingKey: String): CharSequence?

    fun getQuantityString(
        wordingKey: String,
        quantity: CharSequence,
        formatArgs: Array<out Any?>
    ): String?

    fun getQuantityText(
        wordingKey: String,
        quantity: CharSequence
    ): CharSequence?
}
