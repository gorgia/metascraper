package scraper.browser.actions

import org.junit.Test
import scraper.utils.log
import scraper.utils.readFile
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Created by andrea on 07/06/16.
 */
class ActionFactoryTest {

    val filePath = "src/test/resources/commanddemo.json"
    val message = readFile(filePath)

    val actionFactory = ActionFactory()

    @Test
    fun createActionList() {
        val actionList = actionFactory.createActionList(message)
        assertNotNull(actionList)
        assertTrue(actionList.isNotEmpty())
        actionList.forEach { action -> log().info("Action classname is: ${action.javaClass.name}") }
    }

}