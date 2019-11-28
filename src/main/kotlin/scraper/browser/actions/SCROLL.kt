package scraper.browser.actions

import com.google.common.collect.Multimap
import scraper.browser.browserinternals.decorators.infiniteScroll
import scraper.context.insert

/**
 * Created by andrea on 28/04/16.
 */

open class SCROLL : BrowserAction() {
    override var from: String? = ""
    override var to: String? = "scrolledDocument"
    var destType: DestType = DestType.JSOUP // or jsoup
    var heartbit = 1

    override fun execute(resultMap: Multimap<String, Any?>): Multimap<String, Any?> {
        this.webDriver.infiniteScroll(heartbit)
        resultMap.insert(to!!, OPEN(this.webDriver).produce(destType))
        return resultMap
    }
}