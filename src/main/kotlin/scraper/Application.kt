package scraper

import com.fasterxml.jackson.databind.ObjectMapper
import org.openqa.selenium.WebDriver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Lazy
import org.springframework.jms.annotation.EnableJms
import scraper.browser.WebDriverFactory


/**
 * Created by andrea on 03/03/16.
 */


@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJms
open class Application {
    val mapper = ObjectMapper()

    @Autowired
    lateinit var webDriverFactory: WebDriverFactory



    @Bean
    @Lazy
    open fun getWebDriver(): WebDriver {
        return webDriverFactory.create()
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}