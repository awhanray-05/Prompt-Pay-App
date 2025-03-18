package com.example.promptpayapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.promptpayapp.data.User
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreenView(
    navController: NavController,
    currentUser: State<User?>
) {
    LaunchedEffect(Unit) {
        delay(3000)  // Wait for 3 seconds
        if(currentUser.value != null) { navController.navigate(NavigationItem.BankingScreen.title) }
        else { navController.navigate(Screens.SelectLoginOrSignUpScreen.route) }
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Url("https://lottie.host/3e2e2903-0699-46a0-86e4-e1bfc5a05b2b/B0jtciB46g.lottie")
    )
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.size(500.dp)
            )
        }
    }
}