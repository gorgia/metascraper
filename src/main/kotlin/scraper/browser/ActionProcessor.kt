package scraper.browser

import com.google.common.collect.Multimap
import org.openqa.selenium.WebDriver
import scraper.browser.actions.Action
import scraper.browser.actions.BrowserAction
import scraper.utils.log

/**
 * Created by andrea on 30/06/16.
 */
interface ActionProcessor {

    fun process(actions: List<Action>, resultMap: Multimap<String, Any?>, webDriver: WebDriver): Multimap<String, Any?> {
        actions.forEach { action ->
            if (action is BrowserAction) {
                action.webDriver = webDriver
            }
            try {
                action.execute(resultMap)
            } catch(e: Exception) {
                log().error("Exception during execution of action: ${action.toString()}", e)
            }
        }
        return resultMap
    }


}