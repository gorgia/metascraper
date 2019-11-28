package scraper.browser.actions

import org.openqa.selenium.WebElement
import scraper.context.find
import scraper.context.insert
import socialnet.browser.back.messages.actions.exceptions.MissingParamException

/**
 * Created by andrea on 23/06/16.
 */
class CLICK : BrowserAction() {
    override var from: String? = "webElement"
    override var to: String? = "webElement"


    override fun execute() {
        val from = resultMap.find(from!!)
        if (from is Collection<*>) {
            from.forEach { f ->
                resultMap.insert(to!!, process(f))
            }
        } else {
            resultMap.insert(to!!, process(from))
        }
    }


    private fun process(from: Any?): WebElement {
        if (from !is WebElement) {
            throw(MissingParamException("Wrong type of from it must be of type WebElement"))
        }
        from.click()
        return from
    }
}