package io.ddubson.postgresqlreactiveexample

import io.r2dbc.client.R2dbc
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux


@SpringBootApplication
class PostgresqlReactiveExampleApplication

@Configuration
class Config {
    @Bean
    fun routes(dataHandler: DataHandler): RouterFunction<ServerResponse> = router {
        accept(MediaType.TEXT_EVENT_STREAM).nest {
            GET("/") {
                ok().body(dataHandler.getSomeData())
            }
        }
    }

    @Bean
    fun postgreReactiveClient(configuration: PostgresqlConnectionConfiguration): R2dbc =
            R2dbc(PostgresqlConnectionFactory(configuration))

    @Bean
    fun postgreConfig() = PostgresqlConnectionConfiguration.builder()
            .host("localhost")
            .port(5432)
            .database("ddubson")
            .username("ddubson")
            .password("")
            .build()
}

@Component
class DataHandler(private val postgreReactiveClient: R2dbc) {
    fun getSomeData(): Flux<String> {
        return postgreReactiveClient
                /*.inTransaction { handle ->
                    handle.execute("INSERT INTO nyc_311_complaints (test) VALUES ($1)", 100)
                }*/
                .inTransaction { handle ->
                    handle
                            .select("SELECT service_request_id FROM nyc_311_complaints")
                            .mapResult { result ->
                                result.map { row, rowMetadata ->
                                    row.get("service_request_id", String::class.java) ?: "Not found"
                                }
                            }
                }
    }
}

fun main(args: Array<String>) {
    runApplication<PostgresqlReactiveExampleApplication>(*args)
}

