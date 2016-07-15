package scraper.browser.actions


import com.google.common.collect.Multimap
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import scraper.context.insert

/**
 * Created by andrea on 28/04/16.
 */

open class GET() : BrowserAction() {

    override var from: String? = ""
    override var to: String? = "document"
    var type: String = "web" // or css
    var wait: String = ""

    constructor(webDriver: WebDriver) : this() {
        this.webDriver = webDriver

    }


    override fun execute(resultMap: Multimap<String, Any?>): Multimap<String, Any?> {
        if (from.isNullOrEmpty()) {
            //this.webDriver.navigate().refresh()
        } else {
            this.webDriver!!.get(from)
        }
        if (to != null && to!!.isNotBlank())
            resultMap.insert(to!!,produce(type))
        return resultMap
    }

    fun produce(type : String) : Any?{
        if ("css".equals(type)) {
            return this.webDriver!!.pageSource
        } else {
            return this.webDriver!!.findElement(By.tagName("html"))
        }
    }
}