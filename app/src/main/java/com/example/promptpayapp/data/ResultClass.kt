package com.example.promptpayapp.data

// Result class that will hold the data state for each process.
sealed class ResultClass<out T> {
    data class Success<out T>(val data: T) : ResultClass<T>()
    data class Error(val exception: Exception) :ResultClass<Nothing>()
}