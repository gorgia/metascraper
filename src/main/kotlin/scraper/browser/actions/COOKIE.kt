package scraper.browser.actions


class COOKIE: BrowserAction(){
    override var from: String? = "cookies"
    override var to: String? = null

    override fun execute()  {

        this.webDriver.manage().cookies.addAll(from)
    }

    fun stringToCookies(){
        
    }

}