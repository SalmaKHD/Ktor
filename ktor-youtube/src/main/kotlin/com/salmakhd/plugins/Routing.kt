package com.salmakhd.plugins

import com.salmakhd.homeRoute
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// define configureRouting module
fun Application.configureRouting() {
    routing {
        homeRoute()
    }
}
