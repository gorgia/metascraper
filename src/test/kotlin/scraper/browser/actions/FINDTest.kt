package scraper.browser.actions

import com.google.common.collect.Multimap
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import scraper.ApplicationScraper
import scraper.browser.BrowserInvoker
import scraper.context.find
import scraper.context.toJson
import scraper.utils.log
import scraper.utils.readFile
import java.util.regex.Pattern
import kotlin.test.assertNotNull


/**
 * Created by andrea on 07/06/16.
 */
@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = arrayOf(ApplicationScraper::class))
class FINDTest {

    var testStringPath: String = ""

    @Autowired
    lateinit var browserInvoker: BrowserInvoker

    @Autowired
    lateinit var actionFactory: ActionFactory

    @Ignore
    @Test
    fun execute() {
        val action = actionFactory.createActionList(readFile(testStringPath))
        var result = browserInvoker.process(action)
        log().info(result.toJson().toString())
    }

    @Test
    @Ignore
    fun executeLogin() {
        testStringPath = "src/test/resources/logintest.json"
        val action = actionFactory.createActionList(readFile(testStringPath))
        var result = browserInvoker.process(action, browserInvoker.resultMap, browserInvoker.webDriver)
        var pagelet = result.find("pagelet_welcome_box")
        assertTrue(pagelet != null)
        log().info(result.toJson().toString())
    }

    @Test
    fun facebookFriendsList() {
        testStringPath = "src/test/resources/facebookfriendstest.json"
        val action = actionFactory.createActionList(readFile(testStringPath))
        var result = browserInvoker.process(action)

        var check: Any? = result.find("hovercardfriendlist.friend")
        if (check is Multimap<*, *>) {
            check = (check as Multimap<String, Any?>).values()
            log().info(check?.toJson().toString())
        }
        log().info(check?.toJson().toString())
        log().info("facebookFriendList end")
    }

    @Test
    @Ignore
    fun retrieveIp() {
        log().info("External ip retrieve test start.")
        testStringPath = "src/test/resources/procedures/retrieveip.json"
        val action = actionFactory.createActionList(readFile(testStringPath))
        var result = browserInvoker.process(action)
        var ip: Any? = result.find("ip")
        var ipString: String = ""
        if (ip is String)
            ipString = extractIpFromText(ip)
        assertNotNull(ip)
        assertNotNull(ipString)
        log().info(ipString)
    }

    private fun extractIpFromText(text: String): String {
        val IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)"
        val pattern = Pattern.compile(IPADDRESS_PATTERN)
        val matcher = pattern.matcher(text)
        if (matcher.find()) {
            return matcher.group(0)
        } else {
            return "0.0.0.0"
        }
    }

}