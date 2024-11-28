package io.ducommun.testgraal

sealed class Result<String, T> {
    data class Success<T>(val value: T) : Result<String, T>()
    data class Failure<T>(val error: String) : Result<String, T>()
}

fun <T> T.success(): Result<String, T> = Result.Success(this)
fun <T> String.failure(): Result<String, T> = Result.Failure(this)
