package scraper.browser.actions

import com.google.common.collect.LinkedListMultimap
import com.google.common.collect.Multimap
import scraper.browser.ActionProcessor
import scraper.utils.readFile
import socialnet.browser.back.messages.actions.context.find
import socialnet.browser.back.messages.actions.context.insert
import socialnet.browser.back.messages.actions.exceptions.MissingParamException

/**
 * Created by andrea on 30/06/16.
 */
class PROCEDURE : BrowserAction(), ActionProcessor {

    override var from: String? = ""

    override var to: String? = "procedureResultList"

    val actionFactory = ActionFactory()

    var file: String = ""

    var removeFrom = true

    override fun execute(resultMap: Multimap<String, Any?>): Multimap<String, Any?> {
        if (file.isNullOrEmpty()) {
            throw MissingParamException("PROCEDURE must contain a file path")
        }
        val message = readFile(file)
        val actionList = actionFactory.createActionList(message)
        if (actionList.isEmpty()) {
            throw MissingParamException("PROCEDURE must contain some actions")
        }
        var fromObj: Any? = null
        if (from != null && from!!.isNotEmpty()) {
            fromObj = resultMap.find(from!!)
        }
        if (fromObj is Collection<*>) {
            fromObj.forEach { fromO ->
                var singleResultMap: Multimap<String, Any?> = LinkedListMultimap.create<String, Any?>() //create a temporary data structure
                singleResultMap.insert(from!!, fromO)
                singleResultMap = process(actionList, singleResultMap, this.webDriver)
                if (!from.equals(to) && removeFrom) {
                    singleResultMap.removeAll(from) //remove elaboration data, keeping results
                }
                resultMap.insert(to!!, singleResultMap)

            }
        } else {
            process(actionList, resultMap, this.webDriver)
        }
        return resultMap
    }


}