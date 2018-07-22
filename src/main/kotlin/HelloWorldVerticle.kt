package de.lathspell.test

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import org.slf4j.LoggerFactory

class HelloWorldVerticle(private val port: Int) : AbstractVerticle() {

    companion object {
        val log = LoggerFactory.getLogger(HelloWorldVerticle::class.java)!!
    }

    override fun start(future: Future<Void>) {
        log.info("start - building router")
        val router = Router.router(vertx)
        router.get("/helloWorld").handler(this::helloWorld)
        router.get("/hello/:name").handler(this::helloName)

        log.info("start - listen")
        vertx.createHttpServer()
                .requestHandler { router.accept(it) }
                .listen(port) { result ->
                    if (result.succeeded()) {
                        future.complete()
                    } else {
                        future.fail(result.cause())
                    }
                }
    }

    override fun stop() {
        log.info("stop called")
    }

    private fun helloWorld(routingContext: RoutingContext) {
        routingContext.response().setStatusCode(200).end("Hello World!")
    }

    private fun helloName(routingContext: RoutingContext) {
        val name = routingContext.request().getParam("name")
        if (name == "Betty") {
            routingContext.response().setStatusCode(400).end("I don't like her!")
        }
        routingContext.response().setStatusCode(200).end("Hello $name!")
    }
}