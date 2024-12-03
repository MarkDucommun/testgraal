package io.ducommun.testgraal

import io.r2dbc.spi.ConnectionFactory
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.core.env.Environment
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.http.HttpStatus
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

//import org.springframework.boot.autoconfigure.SpringBootApplication
//import org.springframework.boot.runApplication
//import org.springframework.data.annotation.Id
//import org.springframework.data.repository.reactive.ReactiveCrudRepository
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.stereotype.Repository
//import org.springframework.web.bind.annotation.*
//import reactor.core.publisher.Mono
//import reactor.kotlin.core.publisher.toMono
//
//
//@RestController
//@SpringBootApplication
//class TestgraalApplication(
//    private val habitRepository: HabitRepository
//) {
//
//    @GetMapping("/hello")
//    fun hello(): String = "Hello, World!"
//
//    @GetMapping("habits/{id}")
//    fun home(@PathVariable id: Int): Mono<ResponseEntity<String>> =
//        habitRepository.findById(id)
//            .map { ResponseEntity.ok(it.name) }
//
//    @PostMapping("/habits")
//    fun echo(@RequestBody habit: Mono<HabitRegistration>): Mono<ResponseEntity<Unit>> =
//        habit
//            .map { Habit(name = it.name) }
//            .flatMap { habitRepository.save(it).map(Habit::success) }
//            .doOnSuccess { println("Registered Habit: $it") }
//            .doOnError { println("Failed to register Habit: ${it.message}") }
//            .onErrorResume { (it.message ?: "Something went wrong").failure<Habit>().toMono() }
//            .map {
//                when (it) {
//                    is Result.Success -> ResponseEntity(HttpStatus.CREATED)
//                    is Result.Failure -> ResponseEntity(HttpStatus.BAD_REQUEST)
//                }
//            }
//}
//
//
//fun main(args: Array<String>) {
//    runApplication<TestgraalApplication>(*args)
//}

fun main(args: Array<String>) {
    runApplication<FunctionalApplication>(*args) {
        addInitializers(
            beans {
                bean {
                    ConnectionFactoryInitializer().apply {
                        setConnectionFactory(ref<ConnectionFactory>())
                        setDatabasePopulator(ResourceDatabasePopulator(ClassPathResource("schema.sql")))
                    }
                }
                bean { HabitHandler(ref()) }
                bean {
                    router {
                        POST( "/") { ServerResponse.ok().build() }
                        path("/api/root").nest {
                            val handler = ref<HabitHandler>()
                            GET("/hello") { ServerResponse.ok().bodyValue("Hello, World!") }
                            GET("/habits/{id}", handler::getHabitById)
                            POST("/habits", handler::createHabit)
                        }
                    }
                }
            }
        )
    }
}

@EnableAutoConfiguration
@EnableR2dbcRepositories
@RegisterReflectionForBinding(HabitRegistration::class)
class FunctionalApplication

class HabitHandler(private val habitRepository: HabitRepository) {

    fun getHabitById(request: ServerRequest): Mono<ServerResponse> {
        val id = request.pathVariable("id").toInt()
        return habitRepository.findById(id)
            .flatMap { habit -> ServerResponse.ok().bodyValue(habit.name) }
            .switchIfEmpty(ServerResponse.notFound().build())
    }

    fun createHabit(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono<HabitRegistration>()
            .map { Habit(name = it.name) }
            .flatMap { habitRepository.save(it) }
            .doOnSuccess { println("Registered Habit: $it") }
            .doOnError { println("Failed to register Habit: ${it.message}") }
            .flatMap { ServerResponse.status(HttpStatus.CREATED).bodyValue(it) }
            .onErrorResume { ServerResponse.status(HttpStatus.BAD_REQUEST).build() }
}
