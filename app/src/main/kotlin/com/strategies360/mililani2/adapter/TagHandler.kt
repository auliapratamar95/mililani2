package com.strategies360.mililani2.adapter

import android.text.Editable
import android.text.Html
import org.xml.sax.XMLReader

class TagHandler: Html.TagHandler {
    var first = true
    var parent = ""
    var index = 1

    @Override
    override fun handleTag(
        opening: Boolean, tag: String, output: Editable,
        xmlReader: XMLReader
    ) {
        if (tag == "ul") parent = "ul"
        else if (tag == "ol") parent = "ol"
        if (tag == "li") {
            if (parent.equals("ul")) {
                first = if (first) {
                    output.append("\n\t•")
                    false
                } else {
                    true
                }
            } else {
                if (first) {
                    output.append("\n\t$index. ")
                    first = false
                    index++
                } else {
                    first = true
                }
            }
        }
    }
}