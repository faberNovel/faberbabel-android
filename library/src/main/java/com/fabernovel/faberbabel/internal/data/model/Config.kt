package com.fabernovel.faberbabel.internal.data.model

/**
 * This data class contains required data to request wording from web service
 * It contains the service url, the project Id and the application language code
 * all in string format
 */
data class Config(
    val url: String,
    val projectId: String,
    val languageLocal : String
)
