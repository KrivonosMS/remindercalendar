import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions
import ru.otus.otuskotlin.remindercalendar.ktor.jsonConfig
import ru.otus.otuskotlin.remindercalendar.ktor.module
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Debug
import ru.otus.otuskotlin.remindercalendar.transport.model.common.Message
import ru.otus.otuskotlin.remindercalendar.transport.model.common.StubCase
import ru.otus.otuskotlin.remindercalendar.transport.model.event.RequestEventRead
import ru.otus.otuskotlin.remindercalendar.transport.model.event.ResponseEventRead
import kotlin.test.Test
import kotlin.test.fail

class AuthJwtTest {
    companion object {
        const val SECRET = "marketplace-secret"
        const val AUDIENCE = "test-mp-audience"
        const val REALM = "test-mp-realm"
        const val DOMAIN = "http://localhost/"
        const val USER_ID = "test-user-id"
        const val USER_FNAME = "Ivan"
        const val USER_MNAME = "Ivanovich"
        const val USER_LNAME = "Ivanov"
        val TOKEN = JWT.create()
            .withSubject("Authentication")
            .withIssuer(DOMAIN)
            .withAudience(AUDIENCE)
            .withClaim("id", USER_ID)
            .withClaim("fname", USER_FNAME)
            .withClaim("mname", USER_MNAME)
            .withClaim("lname", USER_LNAME)
            .withArrayClaim("groups", arrayOf("USER", "ADMIN_MP", "MODERATOR_MP"))
            .sign(Algorithm.HMAC256(SECRET))
            .toString()
    }

    @Test
    fun jwtTest() {
        withTestApplication({
            (environment.config as MapApplicationConfig).apply {
                put("remindercalendar.auth.jwt.secret", SECRET)
                put("remindercalendar.auth.jwt.audience", AUDIENCE)
                put("remindercalendar.auth.jwt.domain", DOMAIN)
                put("remindercalendar.auth.jwt.realm", REALM)
            }
            module(testing = true)
        }) {
            handleRequest(HttpMethod.Post, "/event/read") {
                val body = RequestEventRead(
                    requestId = "1",
                    eventId = "event-id",
                    debug = Debug(stubCase = StubCase.SUCCESS),
                )
                val bodyString = jsonConfig.encodeToString(Message.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
                addHeader("Authorization", "Bearer $TOKEN")
            }.apply {
                Assertions.assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
                Assertions.assertThat(response.contentType())
                    .isEqualTo(ContentType.Application.Json.withCharset(Charsets.UTF_8))
                val jsonString = response.content ?: fail("empty content")
                jsonConfig.decodeFromString(Message.serializer(), jsonString) as ResponseEventRead
            }

        }
    }
}
