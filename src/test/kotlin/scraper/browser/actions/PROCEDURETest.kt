package scraper.browser.actions

import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import scraper.ApplicationScraper

/**
 * Created by andrea on 04/07/16.
 */
@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = arrayOf(ApplicationScraper::class))
class PROCEDURETest {

    val testStringPath = "src/test/resources/logintest.json"
}