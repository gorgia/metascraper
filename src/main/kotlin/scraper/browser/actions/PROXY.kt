package scraper.browser.actions

import com.google.common.collect.Multimap
import org.openqa.selenium.chrome.ChromeDriver

/**
 * Created by andrea on 28/04/16.
 */

open class PROXY : BrowserAction() {
    val type: String = "http"
    override fun execute(){
        if (this.webDriver is ChromeDriver){
            this.webDriver.manage().
        }
    }

    override var from: String? = ""
    override var to: String? = "scrolledDocument"
    var heartbit = 1


}