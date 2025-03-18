package com.example.promptpayapp.screens

/*

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
import com.example.promptpayapp.VoiceToTextParser

@Composable
fun BankingCommandsScreen(
    voiceToTextParser: VoiceToTextParser,
) {
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
//                Text(text = state.command.ifEmpty { "No Commands made yet" })
            val result = checkCommands(spokenText = state.spokenText)
            if(result != null) {
                Text(text = result) }
            }
        }
    }
}

fun checkCommands(spokenText: String): String? { // Should return a string such as "Your requested transaction has been processed.\nCurrent Balance: ${balance}"
    if (
        spokenText.contains("fetch", true) ||
        spokenText.contains("show", true) ||
        spokenText.contains("check", true) ||
        spokenText.contains("balance", true)
    ) {
        // Fetch balance logic
        return "Current Balance: "
    } else if (
        spokenText.contains("pay", true) ||
        spokenText.contains("send", true) ||
        spokenText.contains("transfer", true) ||
        spokenText.contains("debit", true)
    ) {
        // Debit transaction logic
        return "The Debit transaction has been processed.\nCurrent Balance: "
    } else if(
        spokenText.contains("credit", true) ||
        spokenText.contains("deposit", true) ||
        spokenText.contains("add", true)
    ) {
        // Credit transaction logic
        return "The Credit transaction has been processed.\nCurrent Balance: "
    } else return null
}


 */


import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.promptpayapp.VoiceToTextParser
import com.example.promptpayapp.componentsOrUtils.CustomDrawer
import com.example.promptpayapp.componentsOrUtils.coloredShadow
import com.example.promptpayapp.viewmodels.AuthViewModel
import kotlin.math.roundToInt

@Composable
fun BankingCommandsScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    voiceToTextParser: VoiceToTextParser
) {
    // Drawer Functionalities
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }
    var selectedNavigationItem by remember { mutableStateOf(NavigationItem.BankingScreen) }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density

    val screenWidth = remember {
        derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
    }
    val offsetValue by remember { derivedStateOf { (screenWidth.value / 4.5).dp } }
    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpened()) offsetValue else 0.dp,
        label = "Animated Offset"
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f,
        label = "Animated Scale"
    )

    BackHandler(enabled = drawerState.isOpened()) {
        drawerState = CustomDrawerState.Closed
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        CustomDrawer(
            selectedNavigationItem = selectedNavigationItem,
            onNavigationItemClick = {
                selectedNavigationItem = it
                navController.navigate(it.title)
            },
            onCloseClick = { drawerState = CustomDrawerState.Closed }
        )
        MainContent(
            navController = navController,
            modifier = Modifier
                .offset(x = animatedOffset)
                .scale(scale = animatedScale)
                .coloredShadow(
                    color = Color.Black,
                    alpha = 0.1f,
                    shadowRadius = 50.dp
                ),
            drawerState = drawerState,
            onDrawerClick = { drawerState = it },
            authViewModel = authViewModel,
            voiceToTextParser = voiceToTextParser
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun MainContent(
    navController: NavController,
    modifier: Modifier = Modifier,
    drawerState: CustomDrawerState,
    onDrawerClick: (CustomDrawerState) -> Unit,
    authViewModel: AuthViewModel,
    voiceToTextParser: VoiceToTextParser
) {

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


    val isSheetFullScreen by remember { mutableStateOf(false) }
    val sheetModifier = if(isSheetFullScreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth()
    val modalSheetState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val roundCornerRadius = if(isSheetFullScreen) 0.dp else 12.dp
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = roundCornerRadius, topEnd = roundCornerRadius),
        sheetContent ={ MoreBottomSheet(modifier = sheetModifier) },
        sheetElevation = 20.dp
    ) {
        Scaffold(
            modifier = modifier
                .clickable(enabled = drawerState == CustomDrawerState.Opened) {
                    onDrawerClick(CustomDrawerState.Closed)
                },
            topBar = {
                TopAppBar(
                    title = { Text(text = "Home", modifier = Modifier.padding(top = 4.dp)) },
                    navigationIcon = {
                        IconButton(onClick = { onDrawerClick(drawerState.opposite()) }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu Icon"
                            )
                        }
                    },
                    modifier = Modifier.shadow(10.dp),
                    actions = {
                        IconButton(
                            onClick = { /* Open up the Notifications Drawer in ModalBottomSheetLayout */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                )
            },
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
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
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
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Hi, ${authViewModel.currentUser.value?.firstName ?: "Guest"} 👋",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 30.sp
                    )
                    Text(
                        "Greetings of the day!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(70.dp))

                    Text(
                        "Perform your Bank Transactions! 🎯",
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        if (canRecord) {
                            AnimatedContent(targetState = state.isSpeaking) { isSpeaking ->
                                if (isSpeaking) {
                                    Text(
                                        text = "Listening...",
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                } else {
                                    Text(
                                        text = if(state.spokenText.isEmpty()) "Click on the mic to record Commands.." else "You spoke: ${state.spokenText}",
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.padding(16.dp))
                            val result = checkCommands(spokenText = state.spokenText)
                            if (result != null) {
                                Text(
                                    text = result,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}

fun checkCommands(spokenText: String): String? { // Should return a string such as "Your requested transaction has been processed.\nCurrent Balance: ${balance}"
    if (
        spokenText.contains("fetch", true) ||
        spokenText.contains("show", true) ||
        spokenText.contains("check", true) ||
        spokenText.contains("balance", true)
    ) {
        // Fetch balance logic
        return "Current Balance: "
    } else if (
        spokenText.contains("pay", true) ||
        spokenText.contains("send", true) ||
        spokenText.contains("transfer", true) ||
        spokenText.contains("debit", true)
    ) {
        // Debit transaction logic
        return "The Debit transaction has been processed.\nCurrent Balance: "
    } else if(
        spokenText.contains("credit", true) ||
        spokenText.contains("deposit", true) ||
        spokenText.contains("add", true)
    ) {
        // Credit transaction logic
        return "The Credit transaction has been processed.\nCurrent Balance: "
    } else return null
}

@Composable
fun MoreBottomSheet(modifier: Modifier) {

}