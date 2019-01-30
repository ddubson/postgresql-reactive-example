package io.ddubson.postgresqlreactiveexample

import io.ddubson.postgresqlreactiveexample.repository.Repository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class ReactiveController(val dataRepository: Repository<Integer>) {
    @GetMapping("/", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun index(): Flux<Integer> {
        return dataRepository.getSomeData()
    }
}