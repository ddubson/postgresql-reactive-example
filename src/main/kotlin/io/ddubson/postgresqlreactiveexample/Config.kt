package io.ddubson.postgresqlreactiveexample

import io.r2dbc.client.R2dbc
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Bean
    fun postgreReactiveClient(configuration: PostgresqlConnectionConfiguration): R2dbc =
            R2dbc(PostgresqlConnectionFactory(configuration))

    @Bean
    fun postgreConfig() = PostgresqlConnectionConfiguration.builder()
            .host("localhost")
            .port(5432)
            .database("postgres")
            .username("ddubson")
            .password("")
            .build()
}