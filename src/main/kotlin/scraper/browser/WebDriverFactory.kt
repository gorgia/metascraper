package scraper.browser


import org.openqa.selenium.Proxy
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.phantomjs.PhantomJSDriverService
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import org.springframework.stereotype.Component
import scraper.utils.conf
import scraper.utils.log
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by andrea on 01/04/16.
 */
@Component
class WebDriverFactory {


    private val pageLoadingTimeSeconds = conf.getLong("socialnet.webDriver.pageLoadingMaxTime")
    private val browserType = conf.getString("socialnet.webDriver.browserType")
    private val seleniumHubGridServerAddress = conf.getString("socialnet.webDriver.seleniumHubGridServerAddress")


    fun create(browserType: String = this.browserType, pageLoadingTimeSeconds: Long = this.pageLoadingTimeSeconds, proxy: String? = null): WebDriver {
        log().debug("Create webDriver with params: browserType $browserType, pageLoadingTimeSeconds $pageLoadingTimeSeconds, proxy: $proxy")
        var capabilities: DesiredCapabilities = DesiredCapabilities()
        var webDriver: WebDriver?
        when (browserType) {
            "chrome" -> capabilities = initChromeBrowser()
            "firefox" -> capabilities = initFirefoxBrowser()
            "phantom" -> capabilities = initPhantomJSBrowser()
            "remote" -> capabilities = initRemotePhantomJS()
            "remote-firefox" -> capabilities = initRemoteFirefox()
            "remote-chrome" -> capabilities = initRemoteChrome()
            "remote-phantom" -> capabilities = initRemotePhantomJS()
        }

        if (proxy != null) setProxy(capabilities)

        when (browserType) {
            "chrome" -> webDriver = ChromeDriver(capabilities)
            "firefox" -> webDriver = FirefoxDriver(capabilities)
            "phantom" -> webDriver = PhantomJSDriver(capabilities)
            "remote" -> webDriver = retrieveBrowserFromRemoteGrid(capabilities)
            "remote-firefox" -> webDriver = retrieveBrowserFromRemoteGrid(capabilities)
            "remote-chrome" -> webDriver = retrieveBrowserFromRemoteGrid(capabilities)
            "remote-phantom" -> webDriver = retrieveBrowserFromRemoteGrid(capabilities)
            else -> {
                webDriver = HtmlUnitDriver()
            }
        }
        webDriver ?: HtmlUnitDriver()
        webDriver!!
        webDriver.manage().timeouts().pageLoadTimeout(pageLoadingTimeSeconds, TimeUnit.SECONDS)
        webDriver.manage().timeouts().setScriptTimeout(pageLoadingTimeSeconds, TimeUnit.SECONDS)
        webDriver.manage().timeouts().implicitlyWait(pageLoadingTimeSeconds, TimeUnit.SECONDS)
        return webDriver
    }

    private fun setProxy(capabilities: DesiredCapabilities, proxyAddress: String? = null) {
        if (proxyAddress == null) return
        val proxy = Proxy()
        proxy.ftpProxy = proxyAddress
        proxy.httpProxy = proxyAddress
        proxy.sslProxy = proxyAddress
        capabilities.setCapability(CapabilityType.PROXY, proxy)
    }

    private fun initFirefoxBrowser(proxy: String? = null): DesiredCapabilities {
        log().info("Init Firefox WebBrowser")
        val customProfile = FirefoxProfile()
        customProfile.setAcceptUntrustedCertificates(true)
        customProfile.setPreference("network.http.connection-timeout", 100)
        customProfile.setPreference("network.http.connection-retry-timeout", 100)
        val capabilities = DesiredCapabilities.firefox()
        capabilities.setCapability(FirefoxDriver.PROFILE, customProfile)
        return capabilities
    }

    private fun initChromeBrowser(): DesiredCapabilities {
        log().info("Init Chrome WebBrowser")
        val options = ChromeOptions()
        options.setBinary("/opt/google/chrome/google-chrome")
        System.setProperty("webDriver.chrome.driver", "/opt/google/chrome/chromedriver")
        val capabilities = DesiredCapabilities.chrome()
        capabilities.setCapability(ChromeOptions.CAPABILITY, options)
        return capabilities

    }

    private fun initPhantomJSBrowser(): DesiredCapabilities {
        log().info("Init PhantomJS WebBrowser")
        val caps = DesiredCapabilities()
        caps.setJavascriptEnabled(true)
        caps.setCapability("takesScreenshot", true)
        caps.setCapability(
                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "/usr/local/lib/node_modules/phantomjs/lib/phantom/bin/phantomjs")

        val cliArgsCap = ArrayList<String>()
        cliArgsCap.add("--web-security=false")
        cliArgsCap.add("--ssl-protocol=any")
        cliArgsCap.add("--ignore-ssl-errors=true")
        cliArgsCap.add("--load-images=no")

        caps.setCapability("web-security", false)
        caps.setCapability("ssl-protocol", "any")
        caps.setCapability("ignore-ssl-errors", true)
        caps.setCapability("takesScreenshot", true)
        caps.setCapability(
                PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap)
        caps.setCapability(
                PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS,
                arrayOf("--logLevel=2"))
        return caps
    }

    private fun initRemotePhantomJS(): DesiredCapabilities {
        log().info("Init remote PhantomJS Browser")
        val capabilities = DesiredCapabilities.phantomjs()
        capabilities.setJavascriptEnabled(true)
        val cliArgsCap = ArrayList<String>()
        cliArgsCap.add("--web-security=false")
        cliArgsCap.add("--ssl-protocol=any")
        cliArgsCap.add("--ignore-ssl-errors=true")
        cliArgsCap.add("--load-images=no")
        capabilities.setCapability("takesScreenshot", true)
        val cliArgs = arrayOfNulls<String>(cliArgsCap.size)
        for (i in cliArgs.indices) cliArgs[i] = cliArgsCap[i]
        capabilities.setCapability(
                PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgs)
        capabilities.setCapability(
                PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS,
                arrayOf("--logLevel=2"))
        return capabilities
    }


    private fun initRemoteFirefox(): DesiredCapabilities {
        log().info("Init remote Firefox WebBrowser")
        val capabilities = DesiredCapabilities.firefox()
        capabilities.setCapability("web-security", false)
        capabilities.setCapability("ssl-protocol", "any")
        capabilities.setCapability("ignore-ssl-errors", true)
        capabilities.isJavascriptEnabled = true
        val firefoxProfile = FirefoxProfile()
        firefoxProfile.setAlwaysLoadNoFocusLib(true)
        firefoxProfile.setAcceptUntrustedCertificates(true)

        capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile)
        return capabilities
    }

    private fun initRemoteChrome(): DesiredCapabilities {
        log().info("Init remote Chrome WebBrowser")
        val capabilities = DesiredCapabilities.chrome()
        capabilities.isJavascriptEnabled = true
        return capabilities
    }


    fun retrieveBrowserFromRemoteGrid(capabilities: DesiredCapabilities, seleniumHubGridServerAddress: String = this.seleniumHubGridServerAddress): WebDriver? {
        var webDriver: WebDriver? = null
        try {
            webDriver = RemoteWebDriver(URL(seleniumHubGridServerAddress), capabilities)
            Thread.sleep(2000)
        } catch (e: MalformedURLException) {
            log().error("Selenium Hub is not active. Recover with local PhantomJS driver", e)
        } catch (e: InterruptedException) {
            log().error("", e)
        }
        return webDriver
    }


}

