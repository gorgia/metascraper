package scraper.browser.actions

import com.google.common.collect.Multimap

/**
 * Created by andrea on 28/04/16.
 */

open class PROXY : BrowserAction() {
    override fun execute(resultMap: Multimap<String, Any?>): Multimap<String, Any?> {
        throw UnsupportedOperationException()
    }

    override var from: String? = ""
    override var to: String? = "scrolledDocument"
    var heartbit = 1


}