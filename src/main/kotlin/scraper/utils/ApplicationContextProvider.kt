package scraper.utils

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

/**
 * Created by andrea on 01/04/16.
 */


@Component
open class ApplicationContextProvider : ApplicationContextAware {

    companion object {
        lateinit var context: ApplicationContext
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

}