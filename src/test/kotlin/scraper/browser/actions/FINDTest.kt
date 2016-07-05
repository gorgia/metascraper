package scraper.browser.actions

import com.google.common.collect.Multimap
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import scraper.Application
import scraper.browser.BrowserInvoker
import scraper.utils.log
import scraper.utils.readFile
import socialnet.browser.back.messages.actions.context.find
import socialnet.browser.back.messages.actions.context.toJson
import kotlin.test.assertNotNull


/**
 * Created by andrea on 07/06/16.
 */
@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = arrayOf(Application::class))
class FINDTest {

    var testStringPath : String = ""

    @Autowired
    lateinit var browserInvoker: BrowserInvoker

    @Autowired
    lateinit var actionFactory: ActionFactory

    @Ignore
    @Test
    fun execute() {
        val action  = actionFactory.createActionList(readFile(testStringPath))
        var result  = browserInvoker.process(action)
        log().info(result.toJson().toString())
    }
    @Test
    @Ignore
    fun executeLogin(){
        testStringPath = "src/test/resources/logintest.json"
        val action  = actionFactory.createActionList(readFile(testStringPath))
        var result  = browserInvoker.process(action, browserInvoker.resultMap, browserInvoker.webDriver)
        assertNotNull(result["pagelet_welcome_box"])
        log().info(result.toJson().toString())
    }

    @Test
    fun facebookFriendsList(){
        testStringPath = "src/test/resources/facebookfriendstest.json"
        val action  = actionFactory.createActionList(readFile(testStringPath))
        var result  = browserInvoker.process(action)

        var check : Any? = result.find("hovercardfriendlist.friend")
        if(check is Multimap<*,*>)
            check = (check as Multimap<String, Any?>).values()

        log().info(check?.toJson().toString())
    }

}