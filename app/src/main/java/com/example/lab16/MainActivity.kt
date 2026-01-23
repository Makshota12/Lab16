package com.example.lab16

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.lab16.accessibility.AccessibilityDemoScreen
import com.example.lab16.ui.theme.Lab16Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab16Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Using a Box or just passing padding to the screen
                    androidx.compose.foundation.layout.Box(modifier = Modifier.padding(innerPadding)) {
                        AccessibilityDemoScreen()
                    }
                }
            }
        }
    }
}
