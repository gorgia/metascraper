package scraper.browser.actions

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.google.common.collect.Multimap
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Created by andrea on 20/04/16.
 */
@Component
@Scope ("prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use= JsonTypeInfo.Id.MINIMAL_CLASS, include=JsonTypeInfo.As.WRAPPER_OBJECT) //REMEMBER THE DOT for classname
interface Action  {
    var from : String?
    var to : String?
    fun execute()
    fun produce(destType: DestType): Any?
}