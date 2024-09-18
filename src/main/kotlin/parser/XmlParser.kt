package pt.ruiandrade.capasjornais.parser

import org.w3c.dom.Document
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

class XmlParser {
    fun parse(xmlData: String): Document {
        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        return dBuilder.parse(xmlData.byteInputStream())
    }

    fun extractNewspapers(doc: Document, namespaceUri: String): List<Element> {
        val nodeList = doc.getElementsByTagNameNS(namespaceUri, "newspaper")
        return (0 until nodeList.length).map { nodeList.item(it) as Element }
    }
}
