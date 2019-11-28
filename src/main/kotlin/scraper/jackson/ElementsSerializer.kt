package scraper.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import org.jsoup.select.Elements
import java.util.*

/**
 * Created by andrea on 18/04/16.
 */
class ElementsSerializer : JsonSerializer<Elements>() {
    val mapper = ObjectMapper()

    override fun serialize(value: Elements?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        val elementsHtml: ArrayList<String> = ArrayList()
        value?.forEach { element ->
            elementsHtml.add(element.html())
        }
        gen?.writeObject(elementsHtml)
    }
}

