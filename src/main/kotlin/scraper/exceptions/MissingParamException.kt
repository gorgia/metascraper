package socialnet.browser.back.messages.actions.exceptions

/**
 * Created by andrea on 15/04/16.
 */
class MissingParamException  : BrowserCommandException{
    constructor(missingParam : String) : super("Missing Param: " + missingParam) {

    }
}