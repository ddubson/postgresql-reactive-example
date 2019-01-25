package io.ddubson.postgresqlreactiveexample

import io.r2dbc.client.R2dbc
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Flux
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory


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
    fun postgreReactiveClient(configuration: PostgresqlConnectionConfiguration): R2dbc = R2dbc(PostgresqlConnectionFactory(configuration))

    @Bean
    fun postgreConfig() = PostgresqlConnectionConfiguration.builder()
            .host("localhost")
            .port(5432)
            .database("postgres")
            .username("ddubson")
            .password("")
            .build()
}

@Component
class DataHandler(private val postgreReactiveClient: R2dbc) {
    fun getSomeData(): Flux<Int> {
        return postgreReactiveClient
                .inTransaction { handle ->
                    handle.execute("INSERT INTO testtable (test) VALUES ($1)", 100)
                }
                .thenMany(postgreReactiveClient.inTransaction { handle ->
                    handle
                            .select("SELECT test FROM testtable")
                            .mapResult { result ->
                                result.map { row, rowMetadata ->
                                    row.get("test", Int::class.java) ?: 1
                                }
                            }
                })
    }
}

fun main(args: Array<String>) {
    runApplication<PostgresqlReactiveExampleApplication>(*args)
}

