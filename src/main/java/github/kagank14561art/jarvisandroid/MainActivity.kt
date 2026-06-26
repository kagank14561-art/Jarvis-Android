package github.kagank14561art.jarvisandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import github.kagank14561art.jarvisandroid.ui.JarvisHud
import github.kagank14561art.jarvisandroid.ui.theme.JarvisAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JarvisAndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF020C0C)
                ) {
                    JarvisScreen()
                }
            }
        }
    }
}

@Composable
fun JarvisScreen(viewModel: JarvisViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        JarvisHud(
            state = uiState.state,
            logs = uiState.logs,
            batteryLevel = uiState.batteryLevel
        )

        // Input bar at the bottom
        Row(
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth()
                .imePadding(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Jarvis'e bir şey söyle...") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF041212),
                    focusedContainerColor = Color(0xFF081818),
                    unfocusedTextColor = Color(0xFF7DFFF6),
                    focusedTextColor = Color(0xFF7DFFF6)
                )
            )
            Button(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.onUserMessage(inputText)
                        inputText = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6600))
            ) {
                Text("GÖNDER")
            }
        }
    }
}
