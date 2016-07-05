package scraper.browser.actions

import org.openqa.selenium.WebDriver

/**
 * Created by andrea on 28/04/16.
 */

abstract class BrowserAction : Action {
     lateinit open var webDriver: WebDriver
}