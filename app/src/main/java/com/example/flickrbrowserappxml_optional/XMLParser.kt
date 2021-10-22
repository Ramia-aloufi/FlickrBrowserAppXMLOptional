package com.example.flickrbrowserappxml_optional


import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

data class AppName(val name: String?)

class XMLParser {
    private val ns: String? = null
    fun parse(inputStream: InputStream): MutableList<Item> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readSongsRssFeed(parser)
        }
    }

    private fun readSongsRssFeed(parser: XmlPullParser): MutableList<Item> {

        val app = mutableListOf<Item>()

        parser.require(XmlPullParser.START_TAG, ns, "rsp")

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "photos") {
                parser.require(XmlPullParser.START_TAG, ns, "photos")
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }

                    if (parser.name == "photo") {
                        parser.require(XmlPullParser.START_TAG, ns, "photo")

                        var id = parser.getAttributeValue(null, "id")
                        var owner = parser.getAttributeValue(null, "owner")
                        var secret = parser.getAttributeValue(null, "secret")
                        var server = parser.getAttributeValue(null, "server")
                        var farm = parser.getAttributeValue(null, "farm")
                        var title = parser.getAttributeValue(null, "title")

                        val img = "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg"


                        Log.d("id", "$id")
                        Log.d("owner", "$owner")
                        Log.d("secret", "$secret")
                        Log.d("server", "$server")
                        Log.d("farm", "$farm")
                        Log.d("tttttt", "$title")
                        app.add(Item(title,img))
                        parser.nextTag()

                    }

                }
            }else {
                skip(parser)
            }
        }
        return app
    }
    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "title")

        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    private fun readName(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "photo")
        var title = ""
        when (parser.name) {
            "title" -> title = readText(parser)

            else -> skip(parser)
        }
//        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "photo")
        return title
    }

    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}