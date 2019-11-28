package scraper.browser.actions

import com.google.common.collect.Multimap
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

/**
 * Created by andrea on 30/06/16.
 */
class WAIT : BrowserAction() {

    override var from: String? = ""

    override var to: String? = ""
    var selectorType: SelectorType = SelectorType.CSS
    var selector: String? = ""

    var seconds: Long = 1

    override fun execute(resultMap: Multimap<String, Any?>): Multimap<String, Any?> {
        val wait = WebDriverWait(webDriver, seconds)
        if (selector.isNullOrEmpty()) {
            Thread.sleep(seconds * 1000)
        } else {
            wait.until(ExpectedConditions.presenceOfElementLocated(getSeleniumSelector(selectorType, selector!!)))
        }
        return resultMap
    }
}