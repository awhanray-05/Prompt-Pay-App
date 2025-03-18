package com.example.promptpayapp.screens

import android.Manifest
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.promptpayapp.VoiceToTextParser
import java.util.Locale

@Composable
fun SignUpOrLoginScreen(
    navController: NavController,
    voiceToTextParser: VoiceToTextParser
) {
    val state by voiceToTextParser.state.collectAsState()
    val context = LocalContext.current
    var textToSpeech: TextToSpeech? by remember { mutableStateOf(null) }
    var canRecord by remember { mutableStateOf(false) }

    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> canRecord = isGranted }
    )

    LaunchedEffect(Unit) {
        recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.US
                textToSpeech?.speak(
                    "Please say Login or Sign Up",
                    TextToSpeech.QUEUE_FLUSH,
                    null, null
                )
            }
        }
        voiceToTextParser.startListening("en-US")
    }

    LaunchedEffect(state.spokenText) {
        when (state.spokenText.lowercase(Locale.ROOT)) {
            "login" -> navController.navigate(Screens.LoginScreen.route)
            "sign up", "signup" -> navController.navigate(Screens.SignUpScreen.route)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (state.isSpeaking) {
                        voiceToTextParser.stopListening()
                    } else {
                        voiceToTextParser.startListening("en-US")
                    }
                }
            ) {
                AnimatedContent(targetState = state.isSpeaking) { isSpeaking ->
                    if (isSpeaking) {
                        Icon(imageVector = Icons.Rounded.Stop, contentDescription = "Stop Icon")
                    } else {
                        Icon(imageVector = Icons.Rounded.Mic, contentDescription = "Mic Icon")
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (canRecord) {
                AnimatedContent(targetState = state.isSpeaking) { isSpeaking ->
                    if (isSpeaking) {
                        Text(text = "Listening...")
                    } else {
                        Text(text = state.spokenText.ifEmpty { "Say 'Login' or 'Sign Up'" })
                    }
                }
                Spacer(modifier = Modifier.padding(16.dp))
            }
        }
    }
}
