package com.example.promptpayapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.promptpayapp.screens.BankingCommandsScreen
import com.example.promptpayapp.screens.NavigationItem
import com.example.promptpayapp.screens.ProfileScreen
import com.example.promptpayapp.screens.Screens
import com.example.promptpayapp.screens.SignUpOrLoginScreen
import com.example.promptpayapp.screens.WelcomeScreenView
import com.example.promptpayapp.ui.theme.PromptPayAppTheme
import com.example.promptpayapp.viewmodels.AuthViewModel

class MainActivity : ComponentActivity() {

    val voiceToTextParser by lazy {
        VoiceToTextParser(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
            PromptPayAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationGraph (
                        voiceToTextParser = voiceToTextParser,
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationGraph(
    voiceToTextParser: VoiceToTextParser,
    navController: NavHostController,
    modifier: Modifier,
    authViewModel: AuthViewModel
) {
    val currentUser = authViewModel.currentUser.observeAsState()
    Log.d("NavigationGraph", "currentUser: ${currentUser.value}")

    NavHost(
        navController = navController,
        startDestination = Screens.WelcomeScreen.route,
    ) {
        composable(Screens.WelcomeScreen.route) {
            WelcomeScreenView(navController = navController, currentUser = currentUser)
        }
        composable(Screens.SelectLoginOrSignUpScreen.route) {
            SignUpOrLoginScreen(navController = navController, voiceToTextParser = voiceToTextParser)
        }
        composable(Screens.LoginScreen.route) {
            LoginScreen(navController = navController, authViewModel = authViewModel, voiceToTextParser = voiceToTextParser)
        }
        composable(Screens.SignUpScreen.route) {
            SignUpScreen(navController = navController, authViewModel = authViewModel, voiceToTextParser = voiceToTextParser)
        }
        composable(NavigationItem.BankingScreen.title) {
            BankingCommandsScreen(navController = navController, authViewModel = authViewModel, voiceToTextParser = voiceToTextParser)
        }
        composable(NavigationItem.Profile.title) {
            ProfileScreen(navController, authViewModel)
        }
    }
}
