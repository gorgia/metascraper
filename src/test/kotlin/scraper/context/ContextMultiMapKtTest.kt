package scraper.context

import com.google.common.collect.LinkedListMultimap
import com.google.common.collect.Multimap
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import scraper.ApplicationScraper
import scraper.utils.log

/**
 * Created by andrea on 13/07/16.
 */
@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest(classes = arrayOf(ApplicationScraper::class))
class ContextMultiMapKtTest {
    @Test
    open fun testInsert() {
        var multiMap: Multimap<String, Any?> = LinkedListMultimap.create<String, Any?>()
        var index = "friend.test"
        multiMap.insert(index, "test")
        log().info("Creation succeded")
    }
}


