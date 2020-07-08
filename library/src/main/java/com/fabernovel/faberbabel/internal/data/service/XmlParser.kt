package com.fabernovel.faberbabel.internal.data.service

import com.fabernovel.faberbabel.internal.data.model.StringResource
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

internal class XmlParser {
    fun parseXml(xmlResponse: String?): Map<String, StringResource> {
        val resourcesMap = mutableMapOf<String, StringResource>()
        if (xmlResponse != null) {
            val xmlFactory = XmlPullParserFactory.newInstance()
            xmlFactory.isNamespaceAware = true
            val parser = xmlFactory.newPullParser()
            parser.setInput(StringReader(xmlResponse))
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tag = parser.name

                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (tag) {
                            TAG_STRING -> {
                                val mappedResource = readString(parser)
                                if (mappedResource != null) {
                                    resourcesMap[mappedResource.first] = mappedResource.second
                                }
                            }
                            TAG_PLURALS -> {
                                val mappedResource = readPlural(parser)
                                if (mappedResource != null) {
                                    resourcesMap[mappedResource.first] = mappedResource.second
                                }
                            }
                        }
                    }
                }
                try {
                    eventType = parser.next()
                } catch (exception: XmlPullParserException) {
                    return emptyMap()
                }
            }
        }
        return resourcesMap
    }

    private fun readString(parser: XmlPullParser): Pair<String, StringResource>? {
        var key: String? = null
        for (attributeIndex in 0 until parser.attributeCount) {
            val attributeName = parser.getAttributeName(attributeIndex)
            if (attributeName == ATTRIBUTE_NAME) {
                key = parser.getAttributeValue(attributeIndex)
            }
        }
        if (key == null) return skipToTagEnd(parser)
        val textEventType = parser.next()
        if (textEventType != XmlPullParser.TEXT) return skipToTagEnd(parser)
        val text = getTextFromParser(parser)

        val closeTagEventType = parser.next()
        if (closeTagEventType != XmlPullParser.END_TAG) return skipToTagEnd(parser)

        return Pair(key, StringResource.SimpleString(text))
    }

    private fun getTextFromParser(parser: XmlPullParser): String {
        return parser.text
            .removePrefix(TEXT_PREFIX_SUFFIX)
            .removeSuffix(TEXT_PREFIX_SUFFIX)
    }

    private fun readPlural(parser: XmlPullParser): Pair<String, StringResource>? {
        var key: String? = null
        var pluralResource: StringResource.PluralString? = null
        for (attributeIndex in 0 until parser.attributeCount) {
            val attributeName = parser.getAttributeName(attributeIndex)
            if (attributeName == ATTRIBUTE_NAME) {
                key = parser.getAttributeValue(attributeIndex)
                break
            }
        }
        if (key == null) return skipToTagEnd(parser)
        var eventType = parser.next()
        while (eventType != XmlPullParser.START_TAG) {
            if (eventType == XmlPullParser.END_DOCUMENT) {
                break
            }
            eventType = parser.next()
        }
        pluralResource = matchPluralResource(parser)
        if (parser.eventType != XmlPullParser.END_TAG) return skipToTagEnd(parser)

        return Pair(key, pluralResource)
    }

    private fun matchPluralResource(
        parser: XmlPullParser
    ): StringResource.PluralString {
        var pluralZero: String? = null
        var pluralOne: String? = null
        var pluralTwo: String? = null
        var pluralFew: String? = null
        var pluralMany: String? = null
        var pluralOther: String? = null

        var tagName = parser.name
        while (tagName != TAG_ITEM) {
            parser.next()
            tagName = parser.name
        }
        loop@ while (tagName == TAG_ITEM) {

            for (attributeIndex in 0 until parser.attributeCount) {
                val attributeName = parser.getAttributeName(attributeIndex)
                if (attributeName == ATTRIBUTE_QUANTITY) {
                    val pluralType = parser.getAttributeValue(attributeIndex)
                    when (pluralType) {
                        StringResource.ZERO -> pluralZero = getPluralText(parser)
                        StringResource.ONE -> pluralOne = getPluralText(parser)
                        StringResource.TWO -> pluralTwo = getPluralText(parser)
                        StringResource.FEW -> pluralFew = getPluralText(parser)
                        StringResource.MANY -> pluralMany = getPluralText(parser)
                        StringResource.OTHER -> pluralOther = getPluralText(parser)
                    }
                }
            }
            when (parser.next()) {
                XmlPullParser.END_TAG -> {
                    if (parser.name == TAG_PLURALS) {
                        break@loop
                    }
                    parser.next()
                }
                XmlPullParser.END_DOCUMENT -> {
                    break@loop
                }
                else -> tagName = parser.name
            }
        }
        return StringResource.PluralString(
            pluralZero,
            pluralOne,
            pluralTwo,
            pluralFew,
            pluralMany,
            pluralOther
        )
    }

    private fun getPluralText(parser: XmlPullParser): String? {
        return if (parser.next() == XmlPullParser.TEXT) {
            getTextFromParser(parser)
        } else null
    }

    private fun <T> skipToTagEnd(parser: XmlPullParser): T? {
        var eventType = parser.eventType
        var startTagFound = 0
        while (eventType != XmlPullParser.END_TAG && startTagFound != 0) {
            if (eventType == XmlPullParser.END_DOCUMENT) return null
            if (eventType == XmlPullParser.END_TAG) {
                startTagFound--
            } else if (eventType == XmlPullParser.START_TAG) {
                startTagFound++
            }
            eventType = parser.next()
        }
        return null
    }

    companion object {
        private const val TAG_STRING = "string"
        private const val TAG_PLURALS = "plurals"
        private const val TAG_ITEM = "item"
        private const val ATTRIBUTE_NAME = "name"
        private const val ATTRIBUTE_QUANTITY = "quantity"
        private const val TEXT_PREFIX_SUFFIX = "\""
    }
}