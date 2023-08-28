package com.salmakhd.plugins

import com.salmakhd.routes.userRouting
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
    static("/static") {
        resources("files")
    }
        // add a new routing
      //  userRouting()
        // add another routing
      //  bodyInfoRouting()
        articleRouting()
    }
}
