package com.fabernovel.faberbabel.core

class Errors {
    companion object {
        const val NOT_PLURAL_RESOURCE_ERROR =
            "Error, target resource is a plurals string. You must specify the quantity parameter."
        const val NOT_SIMPLE_RESOURCE_ERROR =
            "Error, target resource is a simple string and not plural. " +
                "You must use appropriate function."
        const val NOT_RIGHT_QUANTITY_TYPE_ERROR =
            "Error, target quantity type is not recognized."
    }
}
