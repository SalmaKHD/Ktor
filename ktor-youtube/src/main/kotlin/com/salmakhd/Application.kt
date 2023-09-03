package com.salmakhd

import com.salmakhd.entities.NoteEntity
import com.salmakhd.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.Routing.Plugin.install
import kotlinx.serialization.Serializable
import java.io.Serial
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.coroutines.selects.select
import org.ktorm.database.Database
import org.ktorm.dsl.*
import java.io.File

fun main() {
    // no need for a configuration file if this line is present
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        // before any endpoint call, a will be activated
        // alternative approach: call module directly here: module()
        .start(wait = true) // starts the server
}

fun Application.module() {
    // install a feature, allows converting Kotlin objects into json format and vice versa
    install(ContentNegotiation) {
        json()
    }
    configureRouting()

    // insert a value into the database
    /*
    database.insert(NoteEntity) {
        // insert a value into the note table
        set(it.note, "Study Ktor")
    }
     */
    /*

    val notes = database
        .from(NoteEntity)
        .select()

    for(row in notes) {
        println(row[NoteEntity.note])
    }

    // update a row
    database.update(NoteEntity) { // what entity (table) do you want to update?
        set(it.note, "Work out")
            where {
                it.id eq 1
        }
    }

    // delete a row
    database.delete(NoteEntity) {
        it.id eq 1
    }

     */
}


fun Route.homeRoute() {
    get("/") { // define home route
        // all requests can be manipulated using the call object provided by kotlin

        // call.request: contains info about
        println("URI: ${call.request.uri}") // '/'

        println("Headers: ${call.request.headers.names()}")
        println("User-agent: ${call.request.headers["User-Agent"]}")

        // query parameters: /?name=john&email=john@gmail.com -> name and email
        println("Query parameters: ${call.request.queryParameters.names()}")
        println("Name: ${call.request.queryParameters["name"]}")
        /*
            OUTPUT:
            URI: /?name=john&email=john@gmail.com
            Headers: [User-Agent, Accept, Postman-Token, Host, Accept-Encoding, Connection]
            User-agent: PostmanRuntime/7.32.3
            Query parameters: [name, email]
            Name: john
             */

        // we can send responses to requests using the .respond() method, status code denotes request result status
        // call.respondText("Something went wrong", status = HttpStatusCode.NotFound)

        // send Json data from server to client
        val responseObject = UserResponse(name = "Salma", email = "khodaeisalma@gmail.com")
        call.respond(message = responseObject)

    }

    get("/iphones/{page}") {
        val pageNumber = call.parameters["page"]

        /*
        request:
        http://127.0.0.1:8080/iphones/2 ==> / -> DENOTES A PATH, ? -? DENOTES A Query PARAMETER
         */
        call.respondText("You are on page number: $pageNumber")
        /*
        OUTPUT:
        Matched routes:
         "" -> "iphones" -> "{page}" -> "(method:GET)"
        Route resolve result:
        SUCCESS; Parameters [page=[2]] @ /iphones/{page}/(method:GET)
         */
    }

    get("/headers") {
        // append headers to the response -> they can be custom also
        call.response.headers.append("server-name", "ktor-server")
        call.respondText("Headers attached.")
    }

    get("/fileDownload") {
        val file = File("files/salma1.jpg")
        // enable the user to download the file or show it in the browser
        call.response.header(
            HttpHeaders.ContentDisposition, // contentDisposition header -> tells the browser whether we want to
            ContentDisposition.Attachment.withParameter(
                ContentDisposition.Parameters.FileName, "downloadable_image.jpg" // file name that will appear on client's machine
            ).toString() // a class that contains most popular headers
        )

        call.respondFile(file)
    }

    // a post end point
    post("/login") {
        // receive body from the request
        val userInfo = call.receive<UserInfo>() // will deserialize the object in the body of the request
        println("User info is: $userInfo")
        call.respondText("Everything is working")
    }
}

@Serializable
data class UserInfo(
    val email: String,
    val password: String
)

@Serializable
data class UserResponse(
    val name: String,
    val email: String
)