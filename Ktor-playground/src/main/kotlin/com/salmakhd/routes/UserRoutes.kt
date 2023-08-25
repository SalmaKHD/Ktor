package com.example.routes

import io.ktor.server.routing.*

fun Route.userRouting() {
    // a method that specifies everything that falls under
    // the user end point
    route("/user") {
        get { // can be used to list all customers
        }
        get("{id?}") { // can pass an id parameter in the get request to the server

        }
        post {

        }
        delete("{id?}") {

        }
    }
}