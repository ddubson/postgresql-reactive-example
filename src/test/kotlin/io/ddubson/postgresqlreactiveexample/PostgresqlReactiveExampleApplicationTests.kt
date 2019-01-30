package io.ddubson.postgresqlreactiveexample

import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostgresqlReactiveExampleApplicationTests {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun index_fetchesSampleResults_fromDb() {
        webTestClient.get().uri("/")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectHeader()
                .contentType("text/event-stream;charset=UTF-8")
                .expectStatus().isOk
    }
}

