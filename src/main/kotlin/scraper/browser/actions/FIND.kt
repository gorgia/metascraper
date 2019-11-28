package scraper.browser.actions

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement
import scraper.context.find
import scraper.context.insert
import scraper.utils.log
import socialnet.browser.back.messages.actions.exceptions.MissingParamException
import java.util.*

/**
 * Created by andrea on 28/04/16.
 */

open class FIND : BrowserAction() {
    override var from: String? = null
    override var to: String? = "results"
    var selectorType: SelectorType = SelectorType.CSS//"css" //OR xpath
    var destElType: DestType = DestType.WEB //jsoup
    var selector: String = ""
    var attribute: String? = null
    var collapseList = true
    var wait: Long = 3


    override fun execute() {
        var resultList: List<*> = ArrayList<Any?>()
        try {
            val fromObj: Any? = if (from.isNullOrEmpty()) {
                OPEN(this.webDriver).produce(destElType)
            } else {
                resultMap.find(from!!)
            }
            if (selector.isNullOrEmpty()) {
                throw MissingParamException("selector")
            }
            if (to.isNullOrEmpty()) {
                throw MissingParamException("Where should I put results? Param \"to\" is missing!")
            }
            if (fromObj is Collection<*> && fromObj.isNotEmpty()) {
                fromObj.forEach { singleFrom ->
                    resultList = process(singleFrom)
                }
            } else {
                resultList = process(fromObj)
            }

            if (resultList.size == 1 && collapseList) {
                resultMap.insert(to!!, resultList.first())
            } else {
                resultMap.insert(to!!, resultList)
            }

        } catch(e: Exception) {
            log().error("Error during FIND action", e)
        }
    }

    private fun process(fromObj: Any?): List<*> {
        var resultList: List<*> = ArrayList<Any?>()
        when (fromObj) {
            is SearchContext -> {
                resultList = getFromSearchContext(fromObj, selector, destElType)
            }
            is Element -> {
                resultList = getFromJsoupElement(fromObj,selector)
            }
            is String -> {
                val jsoupElement = Jsoup.parse(fromObj)
                resultList  = getFromJsoupElement(jsoupElement,selector)
            }
        }
        return resultList
    }

    private fun getFromSearchContext(searchContext: SearchContext, selector: String, destElementType: DestType = destElType): List<*> {
        var resultList: List<*> = ArrayList<Any?>()
        val webElementsList: List<WebElement> = searchContext.findElements(getSeleniumSelector(selectorType, selector))
        if (!attribute.isNullOrBlank()) {
            val attributes: MutableList<String?> = ArrayList()
            webElementsList.forEach { we -> attributes.add(we.attribute(this.attribute!!)) }
            resultList = attributes
        } else {
            if (DestType.WEB == destElementType) {
                return webElementsList
            } else if (DestType.JSOUP == destElementType) {
                val documentsList: MutableList<Document> = ArrayList()
                webElementsList.forEach { we -> documentsList.add(Jsoup.parse(we.getAttribute("innerHTML"))) }
                resultList = documentsList
            }
        }
        return resultList
    }

    private fun getFromJsoupElement(element: Element, selector: String): List<*> {
        val attributes = ArrayList<String?>()
        val elements = element.select(selector)
        if (attribute.isNullOrBlank()) return elements
        elements.forEach { ir ->
            attributes.add(ir.attribute(attribute!!))
        }
        return attributes
    }



    private fun Element.attribute(attributeKey: String): String? {
        if (attributeKey.isNullOrBlank()) return null
        if ("text" == attributeKey) {
            return this.text()
        }
        return this.attr(attributeKey)
    }

    private fun WebElement.attribute(attribute: String): String? {
        if ("text".equals(attribute)) {
            return this.text
        }
        return this.getAttribute(attribute)
    }
}
