package com.reelcounter.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("reel_counter", MODE_PRIVATE)
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
            .format(java.util.Date())
        if (prefs.getString("day", "") != today) {
            prefs.edit().putString("day", today).putInt("count", 0).apply()
        }
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                ReelCounterScreen(
                    initialLimit = prefs.getInt("limit", 50),
                    onSaveLimit = { prefs.edit().putInt("limit", it).apply() }
                )
            }
        }
    }
}

@Composable
private fun ReelCounterScreen(initialLimit: Int, onSaveLimit: (Int) -> Unit) {
    var limitText by remember { mutableStateOf(initialLimit.toString()) }
    val limit = limitText.toIntOrNull()?.coerceAtLeast(1) ?: initialLimit

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text("REEL COUNTER", color = MaterialTheme.colorScheme.primary,
                fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text("Take back your scroll.", fontSize = 29.sp,
                fontWeight = FontWeight.Bold)
            Text("Your daily short-video dashboard",
                color = MaterialTheme.colorScheme.onSurfaceVariant)

            Card(shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("TODAY'S DETECTED REELS", fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("—", fontSize = 48.sp, fontWeight = FontWeight.Bold)
                    Text("Automatic detection is not connected yet.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Card(shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Daily Reel limit", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        value = limitText,
                        onValueChange = { value -> limitText = value.filter(Char::isDigit).take(5) },
                        label = { Text("Reels per day") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = { limitText.toIntOrNull()?.takeIf { it > 0 }?.let(onSaveLimit) },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Save daily limit") }
                    Text("Daily reset: midnight • Time limit: unlimited",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp)
                }
            }

            Card(shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Platform coverage", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Text("Instagram Reels • YouTube Shorts • Facebook Reels • Snapchat Spotlight",
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Detection and blocking are planned next steps. This first APK is the app foundation, not an active blocker.",
                        color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                }
            }
            Text("Reel Counter • Personal APK • v0.1.0",
                color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}
