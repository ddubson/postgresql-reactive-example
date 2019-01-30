package io.ddubson.postgresqlreactiveexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PostgresqlReactiveExampleApplication

fun main(args: Array<String>) {
    runApplication<PostgresqlReactiveExampleApplication>(*args)
}

