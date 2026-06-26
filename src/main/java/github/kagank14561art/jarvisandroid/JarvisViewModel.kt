package github.kagank14561art.jarvisandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class JarvisUiState(
    val state: String = "LISTENING",
    val logs: List<String> = emptyList(),
    val batteryLevel: Int = 100,
    val time: String = ""
)

class JarvisViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(JarvisUiState())
    val uiState: StateFlow<JarvisUiState> = _uiState

    // In a real app, you'd get this from secure storage
    private val apiKey = "YOUR_GEMINI_API_KEY"

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    private val chat = generativeModel.startChat()

    fun onUserMessage(message: String) {
        addLog("Siz: $message")
        _uiState.value = _uiState.value.copy(state = "THINKING")
        
        viewModelScope.launch {
            try {
                val response = chat.sendMessage(message)
                response.text?.let { 
                    addLog("JARVIS: $it")
                }
                _uiState.value = _uiState.value.copy(state = "LISTENING")
            } catch (e: Exception) {
                addLog("ERR: ${e.message}")
                _uiState.value = _uiState.value.copy(state = "ERROR")
            }
        }
    }

    private fun addLog(log: String) {
        _uiState.value = _uiState.value.copy(
            logs = _uiState.value.logs + log
        )
    }
}
