package io.ducommun.testgraal

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<TestgraalApplication>().with(TestcontainersConfiguration::class).run(*args)
}
