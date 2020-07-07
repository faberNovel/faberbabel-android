package com.fabernovel.faberbabel

import com.fabernovel.faberbabel.internal.data.model.StringResource
import com.fabernovel.faberbabel.internal.data.service.XmlParser
import org.junit.Assert
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class XmlParserUnitTest {
    private val xmlParser = XmlParser()

    @Test
    fun testCorrectXml() {
        val correctXmlWithStringsOnly =
            readTestFile("/xmlparsertestfiles/test_xml_with_strings_only.xml")
        val expectedResponse = mutableMapOf<String, StringResource>()
        expectedResponse["hello_world"] = StringResource.SimpleString("Hello")
        expectedResponse["description"] = StringResource.SimpleString("This is Faberbabel.")
        val response = xmlParser.parseXml(correctXmlWithStringsOnly)
        Assert.assertEquals(expectedResponse, response)
    }

    @Test
    fun testCorrectXmlWithPlurals() {
        val correctXmlWithPlurals =
            readTestFile("/xmlparsertestfiles/test_xml_with_plurals.xml")
        val expectedResponse = mutableMapOf<String, StringResource>()
        expectedResponse["hello_world"] = StringResource.SimpleString("Hello")
        expectedResponse["description"] = StringResource.SimpleString("This is Faberbabel.")
        expectedResponse["plural_one"] = StringResource.PluralString(
            "zero faberbabel",
            "one faberbabel",
            "two faberbabels",
            null,
            null,
            null
        )
        expectedResponse["plural_two"] = StringResource.PluralString(
            null,
            null,
            null,
            null,
            null,
            "other faberbabel"
        )
        val response = xmlParser.parseXml(correctXmlWithPlurals)
        Assert.assertEquals(expectedResponse, response)
    }

    @Test
    fun testNotCorrectlyClosedXml() {
        val notCorrectlyClosedXml =
            readTestFile("/xmlparsertestfiles/test_not_correctly_closed_xml_xml")
        val response = xmlParser.parseXml(notCorrectlyClosedXml)
        Assert.assertEquals(emptyMap<String, StringResource>(), response)
    }

    @Test
    fun testXmlWithMissingAttributeValue() {
        val xmlWithMissingAttributeValue =
            readTestFile("/xmlparsertestfiles/test_xml_with_missing_attribute_value.xml")
        val response = xmlParser.parseXml(xmlWithMissingAttributeValue)
        Assert.assertEquals(emptyMap<String, StringResource>(), response)
    }

    @Test
    fun testNotXml() {
        val response = xmlParser.parseXml("Hello")
        Assert.assertEquals(emptyMap<String, StringResource>(), response)
    }

    @Test
    fun testXmlWithoutText() {
        val xmlWithoutText =
            readTestFile("/xmlparsertestfiles/test_xml_without_text.xml")
        val expectedResponse = mutableMapOf<String, StringResource>()
        expectedResponse["description"] = StringResource.SimpleString("This is Faberbabel.")
        expectedResponse["plural_one"] = StringResource.PluralString(
            "zero faberbabel",
            "one faberbabel",
            "two faberbabels",
            null,
            null,
            null
        )
        val response = xmlParser.parseXml(xmlWithoutText)
        Assert.assertEquals(expectedResponse, response)
    }

    @Test
    fun testXmlWithoutKey() {
        val xmlWithoutKey =
            readTestFile("/xmlparsertestfiles/test_xml_without_key.xml")
        val expectedResponse = mutableMapOf<String, StringResource>()
        expectedResponse["hello_world"] = StringResource.SimpleString("Hello")
        expectedResponse["plural_two"] = StringResource.PluralString(
            null,
            null,
            null,
            null,
            null,
            "other faberbabel"
        )
        val response = xmlParser.parseXml(xmlWithoutKey)
        Assert.assertEquals(expectedResponse, response)
    }

    private fun readTestFile(fileName: String): String? {
        val xmlInputStream = javaClass.getResourceAsStream(fileName)
        return if (xmlInputStream != null) {
            BufferedReader(InputStreamReader(xmlInputStream)).readText()
        } else null
    }
}
