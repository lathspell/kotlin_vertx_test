package de.lathspell.test

import io.vertx.core.Vertx
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory

@RunWith(VertxUnitRunner::class)
class HelloWorldTest {

    companion object {
        val log = LoggerFactory.getLogger(HelloWorldTest::class.java)!!
    }

    private lateinit var vertx: Vertx

    private val port = 8080

    @Before
    fun setup(testContext: TestContext) {
        log.info("Starting Setup")
        vertx = Vertx.vertx()
        vertx.deployVerticle(HelloWorldVerticle(port), testContext.asyncAssertSuccess())
        log.info("Finishing Setup")
    }

    @After
    fun tearDown(testContext: TestContext) {
        log.info("Starting Teardown")
        vertx.close(testContext.asyncAssertSuccess())
        log.info("Finishing Teardown")
    }

    @Test
    fun testHelloWorld(testContext: TestContext) {
        log.info("Starting Test")
        val async = testContext.async()

        log.info("Creating HTTP Client for port $port")
        vertx.createHttpClient()
                .getNow(port, "localhost", "/helloWorld") { response ->
                    response.handler { responseBody ->
                        log.info("Response received!")
                        testContext.assertEquals("Hello World!", responseBody.toString())
                        async.complete()
                    }
                }
    }

    @Test
    fun testHelloName(testContext: TestContext) {
        val async = testContext.async()
        vertx.createHttpClient()
                .getNow(port, "localhost", "/hello/Tim") { response ->
                    response.handler { responseBody ->
                        testContext.assertEquals("Hello Tim!", responseBody.toString())
                        async.complete()
                    }
                }
    }

    @Test
    fun testHelloNameError(testContext: TestContext) {
        val async = testContext.async()
        vertx.createHttpClient()
                .getNow(port, "localhost", "/hello/Betty") { response ->
                    response.handler { responseBody ->
                        testContext.assertEquals(400, response.statusCode())
                        testContext.assertEquals("I don't like her!", responseBody.toString())
                        async.complete()
                    }
                }
    }
}