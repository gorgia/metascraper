package scraper.browser.actions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.stereotype.Component

/**
 * Created by andrea on 28/04/16.
 */
@Component
open class ActionFactory {

    companion object {
        val objectMapper = ObjectMapper()
    }


    open fun createActionList(jsonString: String): List<Action> {
        val jsonObject: JSONObject = JSONObject(jsonString)
        var actionsObject: JSONArray? = jsonObject.getJSONArray("actions")
        return objectMapper.readValue(actionsObject.toString(), TypeFactory.defaultInstance().constructCollectionLikeType(List::class.java, Action::class.java))
    }


}