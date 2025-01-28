package com.example.promptpayapp

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainView(voiceToTextParser: VoiceToTextParser, modifier: Modifier = Modifier) {
    var canRecord by remember {
        mutableStateOf(false)
    }

    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            canRecord = isGranted
        }
    )

    LaunchedEffect(key1 = recordAudioLauncher) {
        recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    val state by voiceToTextParser.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if(state.isSpeaking) {
                        voiceToTextParser.stopListening()
                    } else {
                        voiceToTextParser.startListening("en-IN")
                    }
                }
            ) {
                AnimatedContent(targetState = state.isSpeaking) { isSpeaking ->
                    if(isSpeaking) {
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
            if(canRecord) {
                AnimatedContent(targetState = state.isSpeaking) {isSpeaking ->
                    if(isSpeaking) {
                        Text(text = "Listening...")
                    } else {
                        Text(text = state.spokenText.ifEmpty { "Click on the mic to record Audio" })
                    }
                }
                Spacer(modifier = Modifier.padding(16.dp))
                Text(text = state.command.ifEmpty { "No Commands made yet" })
            }
        }

    }
}