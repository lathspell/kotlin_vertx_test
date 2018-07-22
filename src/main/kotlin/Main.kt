package de.lathspell.test

import io.vertx.core.Vertx
import io.vertx.kotlin.core.VertxOptions

class Main {
    fun start() {
        println("starting")
        var vertx = Vertx.vertx(VertxOptions(warningExceptionTime = 500, workerPoolSize = 40))
        vertx.deployVerticle(HelloWorldVerticle(8080))
    }
}

fun main(args: Array<String>) {
    Main().start()
}