package com.salmakhd.plugins

import com.salmakhd.dao.dao
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.articleRouting() {
    get("/") {
        call.respondText("articles")
    }
    // any route that comes after the articles route
    route("articles") {
        get {
            call.respond(FreeMarkerContent("index.ftl", mapOf(
                "articles" to dao.allArticles()
            )))
        }

        get("new") {

        }

        post {

        }

        get("{id}") {
            val id = call.parameters.getOrFail("id").toInt()
            call.respond(FreeMarkerContent("show.ftl", mapOf("article" to
            dao.article(id = id))))
        }

        get("{id}/edit") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(
                FreeMarkerContent(
                    "edit.ftl", mapOf("aricle" to dao.article(id))
                )
            )
        }

        post("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val formParameters = call.receiveParameters()
            when (formParameters.getOrFail("_action")) {
                "update" -> {
                    val title = formParameters.getOrFail("title")
                    val body = formParameters.getOrFail("body")
                    dao.editArticle(id, title, body)
                    call.respondRedirect("/articles/$id")
                }
                "delete" -> {
                    dao.deleteArticle(id)
                    call.respondRedirect("/articles")
                }
            }
        }

        get("new") {
            call.respond(FreeMarkerContent("new.ftl", model = null))
        }

        post {
            val formParameters = call.receiveParameters()
            val title = formParameters.getOrFail("title")
            val body = formParameters.getOrFail("body")
            val article = dao.addNewArticle(title = title, body = body)
            call.respondRedirect("/articles/${article?.id}")
        }
    }
}