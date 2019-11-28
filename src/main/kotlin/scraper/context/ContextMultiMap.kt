package scraper.context


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
import org.json.JSONObject
import org.jsoup.nodes.Element
import org.openqa.selenium.WebElement
import scraper.jackson.ElementSerializer
import scraper.jackson.WebElementSerializer
import java.util.*


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
    val indexOfDot = stringIndex.indexOf(".")
    if (indexOfDot < 0) {
        val list = this.get(stringIndex)
        if (list.isEmpty()) return null
        if (list.size == 1) return list.first() //not sure if i want this line
        else return list
    }
    val firstSplit = stringIndex.substring(0, indexOfDot)
    val secondSplit = stringIndex.substring(indexOfDot + 1)

    val match: Any? = this.find(firstSplit)
    if (match is Collection<*>) {
        val resultList = LinkedList<Any?>()
        match.forEach { element ->
            val el = element as Multimap<String, *>
            resultList.add(el.find(secondSplit))
        }
        return resultList
    } else if (match != null && match is Multimap<*, *>)
        return (match as Multimap<String, *>).find(secondSplit)
    return null
}


fun Multimap<String, Any?>.insert(stringIndex: String, value: Any?) {
    val indexOfDot = stringIndex.indexOf(".")
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
    val firstSplit: String = stringIndex.substring(0, indexOfDot)
    val secondSplit: String = stringIndex.substring(indexOfDot + 1)
    var match = this.find(firstSplit)
    //case B.1 match not found in first split ==> create map at first split
    if (match == null) {
        this.put(firstSplit, LinkedListMultimap.create<String, Any?>())
        match = this.find(firstSplit)!!
    }
    if (match is Multimap<*, *>) {
        (match as LinkedListMultimap<String, Any?>).insert(secondSplit, value)
    }
    else throw Exception("impossible to insert. Found a non-Collection element in multimap at index $match")
}


val Multimap<String, Any?>.children: Any?
    get() = children


fun <T> JSONObject.find(path: String, vararg filters: Predicate): T {
    return JsonPath.read<T>(this.toString(), "$.$path")
}

fun JSONObject.insert(path: String, value: Any?, vararg filters: Predicate) {
    val lastDot: Int = path.lastIndexOf(".")
    val key: String
    val effectivePath: String?
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




