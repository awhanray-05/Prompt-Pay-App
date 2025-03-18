/*

package com.example.promptpayapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.promptpayapp.viewmodels.AuthViewModel

@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current

    val textColour = MaterialTheme.colorScheme.onPrimary

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var pursuingDegree by remember { mutableStateOf("") }
    var academicYear by remember { mutableStateOf("") }
    var stream by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
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
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 30.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF66BB6A)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = textColour) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.Cyan,
                    focusedBorderColor = Color(0xFF42A5F5),
                    unfocusedBorderColor = Color.LightGray,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                ),
                placeholder = { Text("123cs0010@iiitk.ac.in", color = Color.Gray) },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Passcode", color = textColour) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            painter = if (passwordVisible) painterResource(R.drawable.baseline_visibility_24) else painterResource(
                                R.drawable.baseline_visibility_off_24
                            ),
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = Color.Yellow
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
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Passcode",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            )
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Name", color = textColour) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.Cyan,
                    focusedBorderColor = Color(0xFF42A5F5),
                    unfocusedBorderColor = Color.LightGray,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                ),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Name",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    // added the signup function
                    if (email.isNotEmpty() && isValidEmail(email.trim()) && password.isNotEmpty() && fullName.isNotEmpty()) {
                        authViewModel.signUp(
                            email.trim(),
                            password.trim(),
                            fullName.trim(),
                            0.00
                        )
                        Toast.makeText(context, "Sign Up successful", Toast.LENGTH_SHORT).show()
                    } else if(email.isNotEmpty() && !isValidEmail(email.trim()) && password.isNotEmpty() && fullName.isNotEmpty()) {
                        Toast.makeText(context, "Invalid Email!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Sign Up failed!", Toast.LENGTH_SHORT).show()
                    }
                    email = ""
                    password = ""
                    fullName = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Sign Up")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Already have an account? Sign in.",
                modifier = Modifier.clickable { onNavigateToLogin() },
                color = Color.Cyan
            )
        }
    }
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


 */



package com.example.promptpayapp

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.promptpayapp.screens.Screens
import com.example.promptpayapp.viewmodels.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun SignUpScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    voiceToTextParser: VoiceToTextParser
) {
    BackHandler(enabled = true) {}

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var selectedField by remember { mutableStateOf("firstName") }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isListening by remember { mutableStateOf(false) }

    val state by voiceToTextParser.state.collectAsState()
    var textToSpeech: TextToSpeech? by remember { mutableStateOf(null) }

    val textColour = MaterialTheme.colorScheme.onPrimary

    LaunchedEffect(Unit) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.US
                textToSpeech?.speak("Please speak your full name", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
        // ✅ Launch coroutine to introduce delay properly
        launch {
            delay(2000)  // ✅ Correctly placed inside coroutine
            isListening = true
            voiceToTextParser.startListening("en-IN")
        }
    }

    LaunchedEffect(state.spokenText) {
        if (isListening && state.spokenText.isNotEmpty()) {
            when (selectedField) {
                "firstName" -> {
                    firstName = state.spokenText.trim().replace("\\s+".toRegex(), "")
                        .replaceFirstChar { it.uppercaseChar() }
                    textToSpeech?.speak("Please speak your email", TextToSpeech.QUEUE_FLUSH, null, null)
                    delay(2000)
                    selectedField = "email"
                    voiceToTextParser.startListening("en-IN")
                }
                "email" -> {
                    email = state.spokenText.lowercase(Locale.ROOT).replace(" ", "")
                    textToSpeech?.speak("Please speak your password", TextToSpeech.QUEUE_FLUSH, null, null)
                    delay(2000)
                    selectedField = "password"
                    voiceToTextParser.startListening("en-IN")
                }
                "password" -> {
                    password = state.spokenText.trim().replace("\\s+".toRegex(), "")
                    isListening = false
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (state.isSpeaking) {
                        voiceToTextParser.stopListening()
                    } else {
                        isListening = true
                        voiceToTextParser.startListening("en-IN")
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Sign Up", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF66BB6A))

            // Full Name Field
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Name", color = textColour) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .onFocusChanged { if (it.isFocused) selectedField = "firstName" }, // ✅ Track focus
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = Color.Cyan,
                    focusedBorderColor = Color(0xFF42A5F5),
                    unfocusedBorderColor = Color.LightGray,
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                ),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Name",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            )

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

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // added the signup function
                    if (email.isNotEmpty() && isValidEmail(email.trim()) && password.isNotEmpty() && firstName.isNotEmpty()) {
                        authViewModel.signUp(
                            email.trim(),
                            password.trim(),
                            firstName.trim(),
                            0.00
                        )
                        Toast.makeText(context, "Sign Up successful", Toast.LENGTH_SHORT).show()
                    } else if(email.isNotEmpty() && !isValidEmail(email.trim()) && password.isNotEmpty() && firstName.isNotEmpty()) {
                        Toast.makeText(context, "Invalid Email!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Sign Up failed!", Toast.LENGTH_SHORT).show()
                    }
                    email = ""
                    password = ""
                    firstName = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Sign Up")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Already have an account? Sign in.",
                modifier = Modifier.clickable { navController.navigate(Screens.LoginScreen.route) },
                color = Color.Cyan
            )
        }
    }
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}