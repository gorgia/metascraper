package scraper.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebElement
import scraper.utils.log

/**
 * Created by andrea on 18/04/16.
 */
class WebElementSerializer : JsonSerializer<WebElement>() {
    override fun serialize(value: WebElement?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        val jsonNodeFactory: JsonNodeFactory = JsonNodeFactory.instance
        val jsonNode: JsonNode
        try {
            jsonNode = jsonNodeFactory.textNode(value?.getAttribute("innerHTML"))
            gen?.writeObject(jsonNode)
            return
        } catch(jme: Exception) {
            log().info("${value.toString()} \n node no longer available")
            if(value is RemoteWebElement)
                gen?.writeObject("${value.id} no longer available. Writing its content is impossible.")
        }
    }
}


