package com.salmakhd.model

// Exposed allows us to store data permanently
import org.jetbrains.exposed.sql.*
import java.io.Serializable

data class Article(val id: Int, val title: String, val body: String): Serializable

// a table in a database
object Articles : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val body = varchar("body", 1024)

    override val primaryKey = PrimaryKey(id)
}