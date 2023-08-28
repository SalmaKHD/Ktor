package com.salmakhd.dao

import com.salmakhd.model.Articles
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.h2.driver"
        val jdbcURL = "jdbc:h2:file:./build/db"

        fun init() {
            // establish a connection
            val database = Database.connect(jdbcURL, driverClassName)
            // start a transaction
            transaction(database) {
                // create articles table
                SchemaUtils.create(Articles)
            }
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T) : T =
        // run a new transaction in the IO dispatcher
        newSuspendedTransaction(Dispatchers.IO) { block() }
}