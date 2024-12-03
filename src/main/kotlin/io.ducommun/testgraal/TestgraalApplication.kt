package io.ducommun.testgraal

import io.r2dbc.spi.ConnectionFactory
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
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

fun main(args: Array<String>) {
    runApplication<FunctionalApplication>(*args) {
        setDefaultProperties(mapOf(serverPort))
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
                        POST("/") { ServerResponse.ok().build() }
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

private val serverPort: Pair<String, Any> get() = "server.port" to customHandlerPort

private val customHandlerPort get() = System.getenv("FUNCTIONS_CUSTOMHANDLER_PORT")?.toIntOrNull() ?: 8080
