package scraper.browser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.google.common.collect.LinkedListMultimap
import com.google.common.collect.Multimap
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import scraper.browser.actions.Action
import scraper.jackson.WebElementSerializer

/**
 * Created by andrea on 04/04/16.
 */

@Component
@Scope("prototype")
@Lazy
open class BrowserInvoker : ActionProcessor {

    val mapper = ObjectMapper();

    init {
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true)
        val module = SimpleModule();
        module.addSerializer(WebElement::class.java, WebElementSerializer())
        mapper.registerModule(module);
    }

    @Autowired
    lateinit var webDriver: WebDriver

    var resultMap: Multimap<String, Any?> = LinkedListMultimap.create()

    open fun process(actions: List<Action>): Multimap<String, Any?> {
        return this.process(actions, this.resultMap, this.webDriver)
    }

}