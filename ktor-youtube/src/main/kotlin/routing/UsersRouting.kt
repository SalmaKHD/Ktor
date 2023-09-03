package routing

import com.salmakhd.entities.NoteEntity
import com.salmakhd.model.Note
import com.salmakhd.model.NoteRequest
import com.salmakhd.model.NoteResponse
import db.DatabaseConnection
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Application.notesRoutes() {
    val db = DatabaseConnection.database

    routing {
        get("/notes") {
            val notes = db.from(NoteEntity).select()
                .map {
                    val id = it[NoteEntity.id]
                    val note = it[NoteEntity.note]
                    Note(id ?: -1, note ?: "")
                }
            call.respond(notes)
            call.respondText("Returning all notes")
        }

        post("/notes") {
            val request = call.receive<NoteRequest>()
            val result = db.insert(NoteEntity) {
                set(it.note, request.note)
            }

            if(result==1) {
                call.respondText("Insertion was successful")
                call.respond(HttpStatusCode.OK, NoteResponse(
                    success = true,
                    data = "Values have been inserted successfully."
                ))
            } else {
                call.respond(HttpStatusCode.BadRequest, NoteResponse(
                    success = false,
                    data = "Failed to insert data."
                )
                )
            }

        }

        get("/notes/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1
            val note = db.from(NoteEntity)
                .select()
                .where {NoteEntity.id eq id }
                .map {
                    val id = it[NoteEntity.id]!!
                    val note = it[NoteEntity.note]!!
                    Note(id = id, note = note)
                }.firstOrNull()

            if(note == null) {
                call.respond(HttpStatusCode.NotFound, NoteResponse(
                    success = false,
                    data = "The note you're looking for could not be found"
                ))
            } else {
                call.respond(HttpStatusCode.OK, NoteResponse(
                    success = true,
                    data = note
                ))
            }
        }

        post("/notes/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1
            val updatedNote = call.receive<NoteRequest>()

            val numberOfRowsAffected = db.update(NoteEntity) {
                set(it.note, updatedNote.note)
                where {
                    it.id eq id
                }
            }

            if(numberOfRowsAffected == 1) {
                call.respond(
                    HttpStatusCode.OK, NoteResponse(
                        success = true,
                        data = "note updated successdfully"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Updating note failed."
                    )
                )
            }
        }

        delete("/notes/{id}") {
            val id = call.parameters["id"]?.toInt() ?: -1

            val numberOfAffectedRows = db.delete(NoteEntity) {
                it.id eq id
            }

            if(numberOfAffectedRows == 1) {
                call.respond(
                    HttpStatusCode.OK, NoteResponse(
                        success = true,
                        data = "note deleted successfully"
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Deleting note failed."
                    )
                )
            }
        }
    }
}