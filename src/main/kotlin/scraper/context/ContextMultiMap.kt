package socialnet.browser.back.messages.actions.context


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.guava.GuavaModule
import com.google.common.collect.LinkedListMultimap
import com.google.common.collect.Multimap
import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.Predicate
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider
import org.jsoup.nodes.Element
import org.openqa.selenium.WebElement
import org.slf4j.LoggerFactory
import scraper.jackson.ElementSerializer
import scraper.jackson.WebElementSerializer
import java.util.*


private val log = LoggerFactory.getLogger(Map::class.java)

fun Any.toJson(): String {
    val mapper = ObjectMapper()
    mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true)
    val module = SimpleModule()
    module.addSerializer(WebElement::class.java, WebElementSerializer())
    module.addSerializer(Element::class.java, ElementSerializer())
    mapper.registerModule(module)
    mapper.registerModule(GuavaModule())
    return mapper.writeValueAsString(this)
}


fun Multimap<String, *>.find(stringIndex: String): Any? {
    var indexOfDot = stringIndex.indexOf(".")
    if (indexOfDot < 0) {
        val list = this.get(stringIndex)
        if (list.isEmpty()) return null
        if (list.size == 1) return list.first()
        else return list
    }
    var firstSplit = stringIndex.substring(0, indexOfDot)
    var secondSplit = stringIndex.substring(indexOfDot + 1)

    val match: Any? = this.find(firstSplit)
    if (match is Collection<*>) {
        var resultList = LinkedList<Any?>()
        match.forEach { element ->
            var el = element as Multimap<String, *>
            resultList.add(el.find(secondSplit))
        }
        return resultList
    }
    return (match as Multimap<String, *>).find(secondSplit)
    return null
}


fun Multimap<String, Any?>.insert(stringIndex: String, value: Any?) {
    var indexOfDot = stringIndex.indexOf(".")
    //case A: no dot found in key
    if (indexOfDot < 0) {
        if (stringIndex.toLowerCase().contains("list"))
            this.put(stringIndex, value)
        else {
            this.removeAll(stringIndex)
            this.put(stringIndex, value)
        }
        return
    }

    //case B: dot found
    var firstSplit: String = stringIndex.substring(0, indexOfDot)
    var secondSplit: String = stringIndex.substring(indexOfDot + 1)
    var match = this.find(firstSplit)
    //case B.1 match not found in first split ==> create map at first split
    if (match == null || match !is LinkedListMultimap<*, *>) {
        this.put(firstSplit, LinkedListMultimap.create<String, Comparable<Any?>>())
        match = this.find(firstSplit)
    }
    //case
    (match as Multimap<String, Any?>).insert(secondSplit, value)
}

val Multimap<String, Any?>.children: Any?
    get() = children


fun <T> org.json.JSONObject.find(path: String, vararg filters: Predicate): T {
    return JsonPath.read<T>(this.toString(), "$.$path")
}

fun org.json.JSONObject.insert(path: String, value: Any?, vararg filters: Predicate) {
    val lastDot: Int = path.lastIndexOf(".")
    var key: String
    var effectivePath: String?
    var effectiveValue: Any?
    val configuration = Configuration.defaultConfiguration();
    val mappingProvider = JacksonMappingProvider();
    configuration.mappingProvider(mappingProvider);
    val context = JsonPath.parse(this)
    if (lastDot < 1) {
        this.put(path, value)
    } else {
        key = path.substring(lastDot + 1)
        effectivePath = path.removeRange(lastDot, path.length - 1)
        JsonPath.parse(this).put("$.$effectivePath", key, value, *filters)
    }

}




