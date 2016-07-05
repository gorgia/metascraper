package scraper.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import org.jsoup.nodes.Element

/**
 * Created by andrea on 18/04/16.
 */
class ElementSerializer : JsonSerializer<Element>() {
    val mapper = ObjectMapper()

    override fun serialize(value: Element?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeObject(value?.html())
    }
}

