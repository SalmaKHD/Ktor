package com.salmakhd

import com.salmakhd.dao.DatabaseFactory
import com.salmakhd.plugins.*
import configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    /* TODO: FIX DEPENDENCIES */
    // How to specify the entry point to the server code?
    // do programatically like done here or use a "application.conf" file
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

// this is specified as the entry point in "application.conf"
fun Application.module() {
    // for allowing the API to work nicely with browser clients
//    install(CORS) {
//        anyHost()
//    }

    // execute database configurations
    DatabaseFactory.init(environment.config)

    // defines routing for User data
    configureRouting()
    configureSerialization()
    module1()
}

// create a module
fun Application.module1() {
    // routing defines endpoints and what happens when they are triggered
    routing {
        get("/module1") {
            call.respondText("Hello from module 1!")
        }
    }
}

