package scraper.browser.actions


import com.google.common.collect.Multimap
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import scraper.context.insert

/**
 * Created by andrea on 28/04/16.
 */

open class OPEN() : BrowserAction() {

    override var from: String? = ""
    override var to: String? = null
    var type: DestType = DestType.JSOUP
    var wait: String = ""

    constructor(webDriver: WebDriver) : this() {
        this.webDriver = webDriver
    }


    override fun execute(){
        if (from.isNullOrEmpty()) {
            //this.webDriver.navigate().refresh()
        } else {
            this.webDriver.get(from)
        }
        if (to != null && to!!.isNotBlank())
            resultMap.insert(to!!,produce(type))
    }
}