import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureSerialization() {
    // this allows the server to read the HTTP header and see if it
    // can serve this type of content
    install(ContentNegotiation) {
        json()
    }
}