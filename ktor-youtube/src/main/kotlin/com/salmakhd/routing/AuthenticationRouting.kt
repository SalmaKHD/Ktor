package com.salmakhd.routing

import com.salmakhd.model.NoteResponse
import com.salmakhd.model.User
import com.salmakhd.model.UserCredentials
import com.salmakhd.model.UserEntity
import com.salmakhd.utils.TokenManger
import com.typesafe.config.ConfigFactory
import db.DatabaseConnection
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt

fun Application.authenticationRoutes() {
    val db = DatabaseConnection.database
    val tokenManger = TokenManger(HoconApplicationConfig((ConfigFactory.load())))

    routing {
        post("/register") {
            val userCredentials = call.receive<UserCredentials>()

            if(!userCredentials.isValidCredentials()) {
                call.respond(HttpStatusCode.BadRequest,
                    NoteResponse(
                        success =false,
                        data = "Password should be at least 3 characters long, and username must be at least 6 characters long."
                    )
                )
                return@post
            }

            val userName =userCredentials.username.toLowerCase()
            val password  = userCredentials.hashedPassword()

            // check if username already exists
            val user = db.from(UserEntity)
                .select()
                .where { UserEntity.userName eq userName }
                .map {
                    it[UserEntity.userName]
                }.firstOrNull()

            if(user != null) {
                call.respond(HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "username already exists, please try a dfferent one."
                    ))
                return@post
            }
            val numberOfRowsAffected = db.insert(UserEntity) {
                set(it.userName, userName)
                set(it.password, password)
            }

            call.respondText("User has been Registered.")
        }

        post("login") {
            val userCredentials = call.receive<UserCredentials>()

            if(!userCredentials.isValidCredentials()) {
                call.respond(HttpStatusCode.BadRequest,
                    NoteResponse(
                        success =false,
                        data = "Password should be at least 3 characters long, and username must be at least 6 characters long."
                    ))
                return@post
            }

            val userName =userCredentials.username.toLowerCase()
            val password  = userCredentials.password

            // check if user exists
            val user = db.from(UserEntity)
                .select()
                .where { UserEntity.userName eq userName}
                .map {
                    val id = it[UserEntity.id]!!
                    val userName = it[UserEntity.userName]!!
                    val password = it[UserEntity.password]!!
                    User(id = id, userName = userName, password=password)
                }.firstOrNull()

            if(user == null) {
                call.respond(HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Invalid username or password."
                    ))
                return@post
            }

            val doesPasswordMatch =  BCrypt.checkpw(password, user?.password)
            if(!doesPasswordMatch) {
                call.respond(HttpStatusCode.BadRequest,
                    NoteResponse(
                        success = false,
                        data = "Invalid username or password."
                    ))
                return@post
            }

            val token = tokenManger.generateJWTToken(user)
            call.respond(
                HttpStatusCode.OK,
                NoteResponse(success = true, data = token)
            )
        }

        authenticate {
            get("/me") {
                val principle = call.principle<JWTPrincipal>()
                val username =  principle!!.payload.getClaim("username").toString()
                val userId = principle!!.payload.getClaim("userId").toString()
                call.respondText("Hello $username with id: $userId")
            }
        }
    }
}