package com.fabernovel.faberbabel.core

import com.fabernovel.faberbabel.data.model.StringResource

interface WordingRepository {
    fun getWording(): Map<String, StringResource>
}
