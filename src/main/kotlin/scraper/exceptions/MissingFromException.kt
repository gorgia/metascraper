package socialnet.browser.back.messages.actions.exceptions

/**
 * Created by andrea on 15/04/16.
 */
class MissingFromException  : BrowserCommandException{
    constructor(missing : String) : super("Missing From: " + missing) {

    }
}