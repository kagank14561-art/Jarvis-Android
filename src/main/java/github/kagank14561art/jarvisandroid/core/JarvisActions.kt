package github.kagank14561art.jarvisandroid.core

import com.google.ai.client.generativeai.type.Schema
import com.google.ai.client.generativeai.type.defineFunction

object JarvisActions {
    val openAppTool = defineFunction(
        name = "open_app",
        description = "Android'da herhangi bir uygulamayı açar.",
        parameters = listOf(Schema.str("app_name", "Uygulama adı (örn. 'Spotify', 'Chrome')"))
    ) { arg ->
        // This will be implemented in the ViewModel or handler
        "App open request for $arg"
    }

    val sysInfoTool = defineFunction(
        name = "sys_info",
        description = "Sistem bilgisi alır: pil durumu, saat.",
        parameters = listOf(Schema.str("query", "battery | time"))
    ) { arg ->
        "System info request for $arg"
    }
}
