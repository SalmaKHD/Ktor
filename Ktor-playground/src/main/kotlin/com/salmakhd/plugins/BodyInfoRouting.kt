package com.salmakhd.plugins

import com.salmakhd.model.bodyInfoStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.bodyInfoRouting() {
    get("/bodyInfo/{id?}/stepStride") {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad Request", status =HttpStatusCode.BadRequest)
        val userBodyInfo = bodyInfoStorage.filter { it.id == id} ?: return@get call.respondText(
            "Not found",
            status = HttpStatusCode.NotFound
        )
        call.respond(userBodyInfo)
    }
}