package com.example.promptpayapp.data

// User class that will serve as a template for each registered user information.
data class User (
    val firstName: String = "",
    val email: String = "",
    val balance: Double = 0.00,
)