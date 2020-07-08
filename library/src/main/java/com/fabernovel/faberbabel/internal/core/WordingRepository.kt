package com.fabernovel.faberbabel.internal.core

import com.fabernovel.faberbabel.internal.data.model.Config
import com.fabernovel.faberbabel.internal.data.model.StringResource

internal interface WordingRepository {
    fun getWording(): Map<String, StringResource>?
    suspend fun fetchWording(wordingConfig: Config)
}
