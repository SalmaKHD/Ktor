package com.salmakhd.routes

import com.salmakhd.model.User
import com.salmakhd.model.userStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting() {
    // a method that specifies everything that falls under
    // the user end point
    route("/user") {
        // what halogens when a get request comes in?
        get { // can be used to list all customers
            if(userStorage.isNotEmpty()) {
                call.respond(userStorage)
            }
            else {
                call.respondText("No Customers found", status = HttpStatusCode.OK)
            }
        }
        get("{id?}") { // can pass an id parameter in the get request to the server
            val id = call.parameters["id"] ?: return@get call.respondText(
                "missing id",
                status = HttpStatusCode.BadRequest
            )

            val user = userStorage.find { it.id == id } ?: return@get call.respondText(
                "No Customer with id $id",
                status = HttpStatusCode.NotFound
            )
            call.respond(user)
        }
        // corresponding HTTP request:
        /*
        curl -X POST --location "http://127.0.0.1:8080/user" \
    -H "Content-Type: application/json" \
    -d "{
          \"id\": \"Salma9128584364\",
          \"name\": \"سلما\",
          \"number\": \"09128584364\",
        }"
         */
        post {
            // allow adding a new user
            val user = call.receive<User>()
            userStorage.add(user)
            call.respondText("User stored correctly", status = HttpStatusCode.Created)
        }

        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if(userStorage.removeIf { it.id == id}) {
                call.respondText("User removed successfully", status = HttpStatusCode.Accepted)
            }    else {
                call.respondText(
                    "Not Found", status = HttpStatusCode.NotFound
                )
            }
        }
    }
}