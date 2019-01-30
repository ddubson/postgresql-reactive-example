package io.ddubson.postgresqlreactiveexample.repository

import reactor.core.publisher.Flux

interface Repository<T> {
    fun getSomeData(): Flux<T>
}