package scraper.browser.actions

import com.google.common.collect.Multimap
import org.apache.commons.io.FileUtils
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import scraper.utils.log
import socialnet.browser.back.messages.actions.context.insert
import java.io.File

/**
 * Created by andrea on 24/06/16.
 */
class SCREENSHOT : BrowserAction() {
    override var from: String? = "document"
    override var to: String? = "screenshot"
    var outputType = "json" //file

    override fun execute(resultMap: Multimap<String, Any?>): Multimap<String, Any?> {
        val screenShotTaker = this.webDriver as TakesScreenshot
        var screenshotFile: Any? = null

        if ("json".equals(outputType)) {
            screenshotFile = screenShotTaker.getScreenshotAs(OutputType.BASE64)
        } else if ("file".equals(outputType)) {
            screenshotFile = screenShotTaker.getScreenshotAs(OutputType.FILE)
            if (screenshotFile != null) {
                FileUtils.copyFile(screenshotFile, File(to+".jpg"))
                log().info("Screenshot taken at: $to")
            }
        }
        resultMap.insert(to!!, screenshotFile)
        return resultMap
    }
}