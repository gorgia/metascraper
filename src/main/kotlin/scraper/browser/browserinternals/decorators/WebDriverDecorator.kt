package scraper.browser.browserinternals.decorators

import org.apache.commons.io.FileUtils
import org.openqa.selenium.*
import scraper.utils.log
import java.io.File
import java.util.*

/**
 * Created by andrea on 05/04/16.
 */

fun WebDriver.scrollToBottomOnce(sleepSeconds: Int) {
    val jse: JavascriptExecutor = this as JavascriptExecutor
    jse.executeScript("window.document.body.scrollTop = document.body.scrollHeight; return true;", sleepSeconds * 1000);
}


fun WebDriver.infiniteScroll(heartBitSeconds: Int): Map<String, Int> {
    var elementsCount = 0
    var newElementsCount = 0
    var map = HashMap<String, Int>()
    while (true) {
        scrollToBottomOnce(heartBitSeconds)
        var startTime = System.currentTimeMillis()
        var endTime: Long
        do {
            scrollToBottomOnce(heartBitSeconds)
            val iterationElementsCount = this.findElements(By.cssSelector("*")).count()
            if (iterationElementsCount > newElementsCount) {
                newElementsCount = iterationElementsCount
                break
            }
            endTime = System.currentTimeMillis()
        } while (endTime - startTime < heartBitSeconds * 1000)
        if (elementsCount == newElementsCount) {
            break
        }
        elementsCount = newElementsCount
    }
    map.put("elementscount", elementsCount)
    return map
}

fun WebDriver.takeScreenshot(filepath: String? = null, outputType: OutputType<*> = OutputType.FILE) : Any{
    val screenShotTaker = this as TakesScreenshot
    val screnshotFile = screenShotTaker.getScreenshotAs(outputType)
    if(outputType == OutputType.BASE64) return screnshotFile
    else if (outputType == OutputType.FILE) {
        if (screnshotFile != null) {
            FileUtils.copyFile(screnshotFile as File, File(filepath ?: "screenshot/$filepath"))
            log().info("Screenshot taken at: $filepath")
        }
    }
    return screnshotFile
}
