package scraper.browser.actions

import org.openqa.selenium.By

/**
 * Created by andrea on 30/06/16.
 */
fun getSeleniumSelector(selectorType: String, selector: String): By {
    when (selectorType) {
        "css" -> return By.cssSelector(selector)
        "xpath" -> return By.xpath(selector)
        "id" -> return By.id(selector)
        "classname" -> return By.className(selector)
        "partialLink" -> return By.partialLinkText(selector)
        "tag" -> return By.tagName(selector)
    }
    return By.id(selector)
}