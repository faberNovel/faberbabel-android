package com.fabernovel.faberbabel.internal.data

import com.fabernovel.faberbabel.internal.core.WordingRepository
import com.fabernovel.faberbabel.internal.data.model.StringResource
import java.util.HashMap

internal class WordingRepositoryImpl : WordingRepository {
    private val wordingMap: MutableMap<String, StringResource> = HashMap()

    override fun getWording(): Map<String, StringResource> {
        if (wordingMap.isEmpty()) {
            // Todo fetch wording from service
            wordingMap["wording_from_faberbabel"] =
                StringResource.SimpleString("I'm injected from faberbabel")
            wordingMap["wording_from_faberbabel_to_inflate_text_view"] =
                StringResource.SimpleString("I'm inflated from faberbabel")

            wordingMap["wording_plural_example"] =
                StringResource.PluralString(
                    " I'm plural babel zero",
                    " I'm plural babel one",
                    " I'm plural babel two",
                    " I'm plural babel few",
                    " I'm plural babel many",
                    " I'm plural babel other"
                )

            wordingMap["wording_from_faberbabel_to_inflate_button_text"] =
                StringResource.SimpleString("on faberbabel")
            wordingMap["wording_from_faberbabel_to_inflate_toolbar_title"] =
                StringResource.SimpleString("Faberbabel wording")
        }
        return wordingMap
    }
}
