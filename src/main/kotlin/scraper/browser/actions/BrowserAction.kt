package scraper.browser.actions

import com.google.common.collect.Multimap
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

/**
 * Created by andrea on 28/04/16.
 */

abstract class BrowserAction : Action {
    open lateinit var resultMap: Multimap<String, Any?>
    open lateinit var webDriver: WebDriver
    override var from: String? = null
    override var to: String? = null

    override fun produce(destType: DestType): Any? {
        return if (destType == DestType.JSOUP) {
            this.webDriver.pageSource
        } else {
            this.webDriver.findElement(By.tagName("html"))
        }
    }
}