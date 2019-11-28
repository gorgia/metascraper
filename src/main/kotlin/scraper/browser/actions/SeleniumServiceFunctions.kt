package scraper.browser.actions

import org.openqa.selenium.By

/**
 * Created by andrea on 30/06/16.
 */
fun getSeleniumSelector(selectorType: SelectorType?, selector: String): By {
    return when (selectorType) {
        SelectorType.CSS -> By.cssSelector(selector)
        SelectorType.XPATH -> By.xpath(selector)
        SelectorType.ID -> By.id(selector)
        SelectorType.CLASSNAME -> By.className(selector)
        SelectorType.PARTIALLINK -> By.partialLinkText(selector)
        SelectorType.TAG -> By.tagName(selector)
        else -> By.id(selector)
    }
}