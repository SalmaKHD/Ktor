package com.salmakhd

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import io.ktor.http.*
import kotlin.test.*

fun testModule1() = testApplication {
    application {
        module1()
    }
}

class UserRouteTest {

    @Test
    fun testGetOrder() = testApplication {
        val response = client.get("/100")
        // does the response match what is expected?
        assertEquals(
            """{"name":"سلما","number":"09128584364"}""",
            response.bodyAsText()
        )
        assertEquals(HttpStatusCode.OK, response.status)
    }
}