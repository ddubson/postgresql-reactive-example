package io.ddubson.postgresqlreactiveexample.repository

import io.r2dbc.client.R2dbc
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class ExampleDataRepository(private val postgreReactiveClient: R2dbc): Repository<Integer> {
    override fun getSomeData(): Flux<Integer> {
        return postgreReactiveClient
                /*.inTransaction { handle ->
                    handle.execute("INSERT INTO nyc_311_complaints (test) VALUES ($1)", 100)
                }*/
                .inTransaction { handle ->
                    handle
                            .select("SELECT column1 FROM test_schema.testc")
                            .mapResult { result ->
                                result.map<Integer> { row, rowMetadata ->
                                    row.get("column1", Integer::class.java)
                                }
                            }
                }
    }
}