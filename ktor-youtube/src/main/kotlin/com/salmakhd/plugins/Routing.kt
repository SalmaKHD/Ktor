package com.salmakhd.plugins

import com.salmakhd.routing.authenticationRoutes
import io.ktor.server.application.*
import routing.notesRoutes

// define configureRouting module
fun Application.configureRouting() {
    notesRoutes()
    authenticationRoutes()
}
