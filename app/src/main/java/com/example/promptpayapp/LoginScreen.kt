package com.example.promptpayapp

import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.promptpayapp.data.ResultClass
import com.example.promptpayapp.screens.NavigationItem
import com.example.promptpayapp.screens.Screens
import com.example.promptpayapp.viewmodels.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    voiceToTextParser: VoiceToTextParser
) {
    BackHandler(enabled = true) {
        // Prevent navigating back
    }

    var selectedField by remember { mutableStateOf("") }  // Track selected field
    val focusManager = LocalFocusManager.current  // ✅ Use FocusManager

    val authResult by authViewModel.authResult.observeAsState()
    val currentUser by authViewModel.currentUser.observeAsState()
    val textColour = MaterialTheme.colorScheme.onPrimary

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isListeningForEmail by remember { mutableStateOf(false) }
    var isListeningForPassword by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val state by voiceToTextParser.state.collectAsState()
    val context = LocalContext.current
    var textToSpeech: TextToSpeech? by remember { mutableStateOf(null) }

    // Initialize Text-to-Speech
    LaunchedEffect(Unit) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.US
                textToSpeech?.speak(
                    "Please speak your email ID",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null
                )
            }
        }

        // ✅ Launch coroutine to introduce delay properly
        launch {
            delay(2000)  // ✅ Correctly placed inside coroutine
            isListeningForEmail = true
            voiceToTextParser.startListening("en-IN")
        }
    }

    // Capture email from speech
    LaunchedEffect(state.spokenText) {
        if (isListeningForEmail && state.spokenText.isNotEmpty()) {
            email = state.spokenText.lowercase(Locale.ROOT).replace(" ", "")
            isListeningForEmail = false

            // Ask for password next
            textToSpeech?.speak("Please speak your password", TextToSpeech.QUEUE_FLUSH, null, null)
            delay(2000)
            isListeningForPassword = true
            voiceToTextParser.startListening("en-IN")
        } else if (isListeningForPassword && state.spokenText.isNotEmpty()) {
            password = state.spokenText.trim().replace("\\s+".toRegex(), "")
            isListeningForPassword = false
        }
    }

    Scaffold(
        floatingActionButton = {
            // ✅ FloatingActionButton properly detects selected field
            FloatingActionButton(
                onClick = {
                    if (state.isSpeaking) {
                        voiceToTextParser.stopListening()
                    } else {
                        when (selectedField) {
                            "email" -> {
                                isListeningForEmail = true
                                voiceToTextParser.startListening("en-IN")
                            }
                            "password" -> {
                                isListeningForPassword = true
                                voiceToTextParser.startListening("en-IN")
                            }
                            else -> {
                                Toast.makeText(context, "Select a field first", Toast.LENGTH_SHORT).show()
                            }
                        }
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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF66BB6A)
                )

                // ✅ Update selectedField when focus changes
                val focusRequesterModifier = Modifier.onFocusChanged { focusState ->
                    selectedField = when {
                        focusState.isFocused -> "email"
                        else -> selectedField
                    }
                }

                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = textColour) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .onFocusChanged { if (it.isFocused) selectedField = "email" }, // ✅ Track focus
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.Cyan,
                        focusedBorderColor = Color(0xFF42A5F5),
                        unfocusedBorderColor = Color.LightGray,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                )

                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = textColour) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .onFocusChanged { if (it.isFocused) selectedField = "password" }, // ✅ Track focus
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible }
                        ) {
                            Icon(
                                painter = if (passwordVisible) painterResource(R.drawable.baseline_visibility_24) else painterResource(
                                    R.drawable.baseline_visibility_off_24
                                ),
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.Cyan,
                        focusedBorderColor = Color(0xFF42A5F5),
                        unfocusedBorderColor = Color.LightGray,
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Login Button
                Button(
                    onClick = {
                        authViewModel.login(email, password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Login")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sign Up Navigation
                Text(
                    "Don't have an account? Sign up.",
                    color = Color.Cyan,
                    modifier = Modifier.clickable { navController.navigate(Screens.SignUpScreen.route) }
                )
            }
        }
    }

    // Observe login result and load user
    LaunchedEffect(authResult) {
        if (authResult is ResultClass.Success) {
            authViewModel.loadCurrentUser()
        } else if (authResult is ResultClass.Error) {
            Toast.makeText(
                context,
                "Login failed: ${(authResult as ResultClass.Error).exception.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Observe currentUser and proceed
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            Log.d("Login", "User Loaded: ${currentUser?.firstName}")
            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
            navController.navigate(NavigationItem.BankingScreen.title)
        }
    }
}
