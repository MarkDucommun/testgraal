package io.ducommun.testgraal

import org.springframework.data.annotation.Id

data class Habit(
    @Id
    val id: Int? = null,
    val name: String
)
