package scraper.browser.actions

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import scraper.Application
import scraper.browser.BrowserInvoker
import scraper.utils.log
import scraper.utils.readFile

/**
 * Created by andrea on 07/06/16.
 */
@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = arrayOf(Application::class))
class GETTest {

    val testStringPath = "src/test/resources/GET-test.json"

    @Autowired
    lateinit var browserInvoker: BrowserInvoker

    @Autowired
    lateinit var actionFactory: ActionFactory

    @Test
    fun execute() {
        val action  = actionFactory.createActionList(readFile(testStringPath))
        var result = browserInvoker.process(action)
        log().info(result.toString())
    }

}