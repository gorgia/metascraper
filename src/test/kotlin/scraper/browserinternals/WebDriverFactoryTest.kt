package scraper.browserinternals

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import scraper.ApplicationScraper
import scraper.browser.WebDriverFactory
import kotlin.test.assertNotNull

/**
 * Created by andrea on 07/06/16.
 */
@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = [ApplicationScraper::class])
class WebDriverFactoryTest {

    @Autowired
    lateinit var webDriverFactory: WebDriverFactory

    @Test
    fun create() {
            val webDriver = webDriverFactory.create()
            assertNotNull(webDriver)
    }

    @Test
    fun retrieveBrowserFromRemoteGrid() {

    }

}