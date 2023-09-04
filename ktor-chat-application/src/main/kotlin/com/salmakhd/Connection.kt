package com.salmakhd

import io.ktor.server.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class Connection(val session: DefaultWebSocketServerSession) {
    companion object {
        val lastId = AtomicInteger(0)

    }
    val name = "user${lastId.getAndIncrement()}"

}