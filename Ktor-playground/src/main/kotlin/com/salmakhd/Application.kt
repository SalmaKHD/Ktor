package com.salmakhd

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
    configureRouting()
    configureSerialization()
    module1()
}

// create a module
fun Application.module1() {
    routing {
        get("/module1") {
            call.respondText("Hello from module 1!")
        }
    }
}

