package com.fabernovel.faberbabel.internal.core

import com.fabernovel.faberbabel.internal.data.model.StringResource

internal interface WordingRepository {
    fun getWording(): Map<String, StringResource>
}
