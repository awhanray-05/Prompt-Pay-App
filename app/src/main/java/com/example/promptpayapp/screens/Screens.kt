package com.example.promptpayapp.screens

import com.example.promptpayapp.R

sealed class Screens(val route: String) {
    object WelcomeScreen: Screens("welcome_screen")
    object LoginScreen: Screens("login_screen")
    object SignUpScreen: Screens("sign_up_screen")
    object SelectLoginOrSignUpScreen: Screens("select_login_or_sign_up_screen")
}

enum class NavigationItem(
    val title: String,
    val icon: Int
) {
    BankingScreen(
        icon = R.drawable.baseline_banking_24,
        title = "Home"
    ),
    Profile(
        icon = R.drawable.baseline_profile_24,
        title = "Profile"
    )
}

enum class CustomDrawerState {
    Opened,
    Closed
}

fun CustomDrawerState.isOpened(): Boolean {
    return this.name == "Opened"
}

fun CustomDrawerState.opposite(): CustomDrawerState {
    return if(this == CustomDrawerState.Opened) CustomDrawerState.Closed
    else CustomDrawerState.Opened
}