package scraper.browser.actions

import com.google.common.collect.Multimap
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
    var selectorType: String = "css" //OR xpath
    var destElType: String = "web" //jsoup
    var selector: String = ""
    var attribute: String? = null
    var collapseList = true
    var wait: Long = 3


    override fun execute(resultMap: Multimap<String, Any?>): Multimap<String, Any?> {
        var resultList: List<*> = ArrayList<Any?>()
        try {
            var fromObj: Any?
            if (selector.isNullOrEmpty()) {
                throw MissingParamException("selector")
            }
            if (to.isNullOrEmpty()) {
                throw MissingParamException("Where should I put results? Param \"to\" is missing!")
            }
            if (from.isNullOrEmpty()) {
                fromObj = GET(this.webDriver).produce(destElType)
            } else {
                fromObj = resultMap.find(from!!)
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

        return resultMap
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
                var jsoupElement = Jsoup.parse(fromObj)
                resultList  = getFromJsoupElement(jsoupElement,selector)
            }
        }
        return resultList
    }

    private fun getFromSearchContext(searchContext: SearchContext, selector: String, destElementType: String = destElType): List<*> {
        var resultList: List<*> = ArrayList<Any?>()
        var webElementsList: List<WebElement> = searchContext.findElements(getSeleniumSelector(selectorType, selector))
        if (!attribute.isNullOrBlank()) {
            var attributes: MutableList<String?> = ArrayList()
            webElementsList.forEach { we -> attributes.add(we.attribute(this.attribute!!)) }
            resultList = attributes
        } else {
            if ("web".equals(destElementType)) {
                return webElementsList
            } else if ("jsoup".equals(destElementType)) {
                var documentsList: MutableList<Document> = ArrayList()
                webElementsList.forEach { we -> documentsList.add(Jsoup.parse(we.getAttribute("innerHTML"))) }
                resultList = documentsList
            }
        }
        return resultList
    }

    private fun getFromJsoupElement(element: Element, selector: String): List<*> {
        var attributes = ArrayList<String?>()
        var elements = element.select(selector)
        if (attribute.isNullOrBlank()) return elements
        elements.forEach({ ir ->
            attributes.add(ir.attribute(attribute!!))
        })
        return attributes
    }



    fun Element.attribute(attributeKey: String): String? {
        if (attributeKey.isNullOrBlank()) return null
        if ("text".equals(attributeKey)) {
            return this.text()
        }
        return this.attr(attributeKey)
    }

    fun WebElement.attribute(attribute: String): String? {
        if ("text".equals(attribute)) {
            return this.text
        }
        return this.getAttribute(attribute)
    }
}
