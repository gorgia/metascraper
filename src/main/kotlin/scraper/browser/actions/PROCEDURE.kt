package scraper.browser.actions

import com.google.common.collect.LinkedListMultimap
import com.google.common.collect.Multimap
import scraper.browser.ActionProcessor
import scraper.context.find
import scraper.context.insert
import scraper.utils.readFile
import socialnet.browser.back.messages.actions.exceptions.MissingParamException

/**
 * Created by andrea on 30/06/16.
 */
class PROCEDURE : BrowserAction(), ActionProcessor {

    override var from: String? = ""

    override var to: String? = "procedureResultList"

    private val actionFactory = ActionFactory()

    var file: String = ""

    var removeFrom = true

    override fun execute(){
        if (file.isEmpty()) {
            throw MissingParamException("PROCEDURE must contain a file path")
        }

        val message = readFile(file)
        val actionList = actionFactory.createActionList(message)
        if (actionList.isEmpty()) {
            throw MissingParamException("PROCEDURE must contain some actions")
        }
        if (from.isNullOrEmpty()) {
            from = actionList.first.from
            if (from.isNullOrEmpty()) throw MissingParamException("PROCEDURE must start from somewhere: missing \"from\" parameter")
        }
        correctFrom(actionList)
        val fromObj: Any? = resultMap.find(from!!)

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
    }


    private fun correctFrom(procedureActionList: List<Action>) {
        val firstFrom = procedureActionList.first().from
        procedureActionList.forEach { action -> if (action.from.equals(firstFrom)) action.from = this.from }
    }

}