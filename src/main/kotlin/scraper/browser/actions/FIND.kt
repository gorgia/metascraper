package scraper.browser.actions

import com.google.common.collect.Multimap
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.openqa.selenium.By
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement
import scraper.utils.log
import socialnet.browser.back.messages.actions.context.find
import socialnet.browser.back.messages.actions.context.insert
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


    override fun execute(resultMap: Multimap<String, Any?>): Multimap<String, Any?> {
        var resultList: MutableList<*> = ArrayList<Any?>()
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
            if (resultList.size > 0) {
                if (resultList.size == 1 && collapseList) {
                    resultMap.insert(to!!, resultList.first())
                } else {
                    resultMap.insert(to!!, resultList)
                }
            }
        } catch(e: Exception) {
            log().error("Error during FIND action", e)
        }

        return resultMap
    }

    private fun process(fromObj: Any?): MutableList<*> {
        var resultList: MutableList<*> = ArrayList<Any?>()
        when (fromObj) {
            is SearchContext -> {
                resultList = getFromSearchContext(fromObj, selector, destElType)
            }
            is Element -> {
                var intermediateResultList = fromObj.select(selector)
                var resultList2 = resultList as MutableList<String>
                if (attribute != null) {
                    intermediateResultList.forEach({ ir ->
                        if ("text".equals(attribute)) {
                            resultList2.add(ir.text())
                        } else {
                            resultList2.add(ir.attr(attribute))
                        }
                    })
                }
                resultList = resultList2
            }
            is String -> {
                resultList = Jsoup.parse(fromObj).select(selector)
            }
            is WebElement -> {
                resultList = webDriver!!.findElements(getSeleniumSelector(this.selectorType, this.selector))
            }
        }
        return resultList
    }

    private fun getFromSearchContext(searchContext: SearchContext, selector: String, destElementType: String = destElType): MutableList<*> {
        var list: MutableList<*> = ArrayList<Any?>()
        if ("web".equals(destElementType)) {
            when (selectorType) {
                "css" -> list = searchContext.findElements(By.cssSelector(selector))
                "xpath" -> list = searchContext.findElements(By.xpath(selector))
            }
        } else if ("jsoup".equals(destElementType)) {
            list = ArrayList<Element>()
            when (selectorType) {
                "css" -> searchContext.findElements(By.cssSelector(selector)).forEach { we ->
                    (list as ArrayList<Element>).add(Jsoup.parse(we.getAttribute("innerHTML")))
                }
                "xpath" -> searchContext.findElements(By.xpath(selector)).forEach { we ->
                    (list as ArrayList<Element>).add(Jsoup.parse(we.getAttribute("innerHTML")))
                }
            }
        }
        return list
    }
}

fun Element.attr(attributeKey: String): String {
    if ("text".equals(attributeKey)) {
        return this.text()
    }
    return this.attr(attributeKey)
}