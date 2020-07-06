package com.fabernovel.faberbabel

import com.fabernovel.faberbabel.internal.data.model.StringResource
import com.fabernovel.faberbabel.internal.data.service.XmlParser
import org.junit.Assert
import org.junit.Test

class XmlParserUnitTest {
    private val correctXmlWithStringsOnly = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<resources>\n" +
        "    <string name=\"hello_world\">\"Hello\"</string>\n" +
        "    <string name=\"description\">\"This is Faberbabel.\"</string>\n" +
        "</resources>\n"

    private val correctXmlWithPlurals = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<resources>\n" +
        "    <string name=\"hello_world\">\"Hello\"</string>\n" +
        "    <string name=\"description\">\"This is Faberbabel.\"</string>\n" +
        "<plurals name=\"plural_one\">\n" +
        "        <item quantity=\"zero\">\"zero faberbabel\"</item>\n" +
        "        <item quantity=\"one\">\"one faberbabel\"</item>\n" +
        "        <item quantity=\"two\">\"two faberbabels\"</item>\n" +
        " </plurals>\n" +
        "<plurals name=\"plural_two\">\n" +
        "        <item quantity=\"other\">\"other faberbabel\"</item>\n" +
        "</plurals>\n" +
        "</resources>\n"

    private val notCorrectlyClosedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<resources>\n" +
        "    <string name=\"hello_world\">\"Hello\"</string>\n" +
        "    <string name=\"description\">\"This is Faberbabel.\"" +
        "<plurals name=\"plural_one\">\n" +
        "        <item quantity=\"zero\">\"zero faberbabel\"</item>\n" +
        "        <item quantity=\"one\">\"one faberbabel\"</item>\n" +
        "        <item quantity=\"two\">\"two faberbabels\"</item>\n" +
        "<plurals name=\"plural_two\">\n" +
        "        <item quantity=\"other\">\"other faberbabel\"</item>\n" +
        "</plurals>\n" +
        "</resources>\n"

    private val xmlWithMissingAttributeValue = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<resources>\n" +
        "    <string name=\"hello_world\">\"Hello\"</string>\n" +
        "    <string name=>\"This is Faberbabel.\"</string>\n" +
        "<plurals name=\"plural_one\">\n" +
        "        <item quantity=\"zero\">\"zero faberbabel\"</item>\n" +
        "        <item quantity=\"one\">\"one faberbabel\"</item>\n" +
        "        <item quantity=\"two\">\"two faberbabels\"</item>\n" +
        "<plurals name=\"plural_two\">\n" +
        "        <item quantity=\"other\">\"other faberbabel\"</item>\n" +
        "</plurals>\n" +
        "</resources>\n"

    private val XmlWithoutText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<resources>\n" +
        "    <string name=\"hello_world\"></string>\n" +
        "    <string name=\"description\">\"This is Faberbabel.\"</string>\n" +
        "<plurals name=\"plural_one\">\n" +
        "        <item quantity=\"zero\">\"zero faberbabel\"</item>\n" +
        "        <item quantity=\"one\">\"one faberbabel\"</item>\n" +
        "        <item quantity=\"two\">\"two faberbabels\"</item>\n" +
        "</plurals>\n" +
        "<plurals name=\"plural_two\">\n" +
        "        <item quantity=\"other\"></item>\n" +
        " </plurals>\n" +
        "</resources>\n"

    private val XmlWithoutKey = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<resources>\n" +
        "    <string name=\"hello_world\">\"Hello\"</string>\n" +
        "    <string>\"This is Faberbabel.\"</string>\n" +
        "<plurals name=\"plural_one\">\n" +
        "        <item>\"zero faberbabel\"</item>\n" +
        "        <item quantity=\"one\">\"one faberbabel\"</item>\n" +
        "        <item quantity=\"two\">\"two faberbabels\"</item>\n" +
        "</plurals>\n" +
        "<plurals name=\"plural_two\">\n" +
        "        <item quantity=\"other\">\"other faberbabel\"</item>\n" +
        " </plurals>\n" +
        "</resources>\n"

    private val xmlParser = XmlParser()

    @Test
    fun testCorrectXml() {
        val expectedResponse = mutableMapOf<String, StringResource>()
        expectedResponse["hello_world"] = StringResource.SimpleString("Hello")
        expectedResponse["description"] = StringResource.SimpleString("This is Faberbabel.")
        val response = xmlParser.parseXml(correctXmlWithStringsOnly)
        Assert.assertEquals(expectedResponse, response)
    }

    @Test
    fun testCorrectXmlWithPlurals() {
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
        val response = xmlParser.parseXml(notCorrectlyClosedXml)
        Assert.assertEquals(emptyMap<String, StringResource>(), response)
    }

    @Test
    fun testXmlWithMissingAttributeValue() {
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
        val response = xmlParser.parseXml(XmlWithoutText)
        Assert.assertEquals(expectedResponse, response)
    }

    @Test
    fun testXmlWithoutKey() {
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
        val response = xmlParser.parseXml(XmlWithoutKey)
        Assert.assertEquals(expectedResponse, response)
    }
}
