package scraper.browser.actions

import com.google.common.collect.Multimap
import org.openqa.selenium.Keys
import org.openqa.selenium.WebElement
import socialnet.browser.back.messages.actions.context.find
import socialnet.browser.back.messages.actions.context.insert
import socialnet.browser.back.messages.actions.exceptions.MissingParamException

/**
 * Created by andrea on 23/06/16.
 */
class TEXT : BrowserAction() {
    override var from: String? = "webElement"
    override var to: String? = "webElement"

    var text: String = ""


    override fun execute(resultMap: Multimap<String, Any?>): Multimap<String, Any?> {
        var from = resultMap.find(from!!)
        if (from is Collection<*>) {
            from.forEach { f ->
                resultMap.insert(to!!, process(f))
            }
        } else {
            resultMap.insert(to!!, process(from))
        }
        return resultMap
    }


    private fun process(from: Any?): WebElement {
        if (from !is WebElement) {
            throw(MissingParamException("Wrong type of from it must be of type WebElement"))
        }
        var key: Keys? = Keys.getKeyFromUnicode(text[0]) //return null if text is not a special character
        if (key != null) {
            from.sendKeys(key)

        } else {
            from.sendKeys(text)
        }
        return from
    }


}