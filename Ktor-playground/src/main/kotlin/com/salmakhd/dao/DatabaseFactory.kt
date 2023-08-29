package com.salmakhd.dao

import com.salmakhd.model.Articles
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*
import io.ktor.server.config.*
import java.io.*

object DatabaseFactory {

    // for enabling database connection pooling
    private fun createHikariDataSource(
        url: String,
        driver: String
    ) = HikariDataSource(HikariConfig().apply {
        driverClassName = driver
        jdbcUrl = url
        maximumPoolSize = 3 // max size connection pool can reach
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    })

    fun init(config: ApplicationConfig) {
        // load database configurations
        val driverClassName = config.property("storage.driverClassName").getString()
        val jdbcURL = config.property("storage.jdbcURL").getString() +
                (config.propertyOrNull("storage.dbFilePath")?.getString()?.let {
                    File(it).canonicalFile.absolutePath
                } ?: "")

        // establish a connection
        val database = Database.connect(createHikariDataSource(url = jdbcURL, driver = driverClassName))

        // start a transaction
        // not efficient, as a new JDBC connection will be established for each transaction
        transaction(database) {
            // create articles table
            SchemaUtils.create(Articles)
        }
    }


    suspend fun <T> dbQuery(block: suspend () -> T) : T =
        // run a new transaction in the IO dispatcher
        newSuspendedTransaction(Dispatchers.IO) { block() }
}