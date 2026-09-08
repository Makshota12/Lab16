package com.example.lab16.accessibility

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class AccessibilityManager(private val context: Context) {
    
    fun isScreenReaderEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val accessibilityManager = context
                .getSystemService(Context.ACCESSIBILITY_SERVICE) 
                    as android.view.accessibility.AccessibilityManager
            accessibilityManager.isTouchExplorationEnabled
        } else {
            val services = context.contentResolver.query(
                android.provider.Settings.Secure.CONTENT_URI,
                null,
                "${android.provider.Settings.Secure.NAME}=?",
                arrayOf("enabled_accessibility_services"),
                null
            )
            val hasServices = services?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val value = cursor.getString(0)
                    value?.isNotEmpty() == true
                } else {
                    false
                }
            } ?: false
            services?.close()
            hasServices
        }
    }
    
    fun getFontScale(): Float {
        return context.resources.configuration.fontScale
    }
    
    fun isHighContrastEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contrast = android.provider.Settings.Secure.getFloat(
                context.contentResolver,
                "accessibility_high_text_contrast_enabled",
                0f
            )
            contrast > 0f
        } else {
            false
        }
    }
    
    fun isReduceMotionEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val animationScale = android.provider.Settings.Global.getFloat(
                context.contentResolver,
                android.provider.Settings.Global.ANIMATOR_DURATION_SCALE,
                1f
            )
            animationScale == 0f
        } else {
            false
        }
    }
    
    fun isStickyKeysEnabled(): Boolean {
        return android.provider.Settings.Secure.getInt(
            context.contentResolver,
            "accessibility_sticky_keys",
            0
        ) == 1
    }
    
    fun getMinimumTouchTargetSize(): Dp {
        return if (isScreenReaderEnabled()) {
            48.dp
        } else {
            44.dp
        }
    }
    
    fun getAdaptiveTextSize(baseSize: TextUnit): TextUnit {
        val fontScale = getFontScale()
        return (baseSize.value * fontScale).sp
    }
    
    fun createAccessibleDescription(
        contentDescription: String,
        stateDescription: String? = null,
        roleDescription: String? = null
    ): String {
        return buildString {
            append(contentDescription)
            stateDescription?.let {
                append(". Current state: $it")
            }
            roleDescription?.let {
                append(". Element type: $it")
            }
        }
    }
}

@Stable
data class AccessibilityState(
    val screenReaderEnabled: Boolean,
    val fontScale: Float,
    val highContrastEnabled: Boolean,
    val reduceMotionEnabled: Boolean,
    val minimumTouchTargetSize: Dp
)

@Composable
fun rememberAccessibilityState(): AccessibilityState {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    
    val accessibilityManager = remember(context) { AccessibilityManager(context) }
    
    var screenReaderEnabled by remember { mutableStateOf(false) }
    var fontScale by remember { mutableStateOf(1f) }
    var highContrastEnabled by remember { mutableStateOf(false) }
    var reduceMotionEnabled by remember { mutableStateOf(false) }
    
    androidx.compose.runtime.LaunchedEffect(configuration) {
        screenReaderEnabled = accessibilityManager.isScreenReaderEnabled()
        fontScale = accessibilityManager.getFontScale()
        highContrastEnabled = accessibilityManager.isHighContrastEnabled()
        reduceMotionEnabled = accessibilityManager.isReduceMotionEnabled()
    }
    
    androidx.compose.runtime.LaunchedEffect(Unit) {
        screenReaderEnabled = accessibilityManager.isScreenReaderEnabled()
        fontScale = accessibilityManager.getFontScale()
        highContrastEnabled = accessibilityManager.isHighContrastEnabled()
        reduceMotionEnabled = accessibilityManager.isReduceMotionEnabled()
    }
    
    return remember(
        screenReaderEnabled,
        fontScale,
        highContrastEnabled,
        reduceMotionEnabled,
        density
    ) {
        AccessibilityState(
            screenReaderEnabled = screenReaderEnabled,
            fontScale = fontScale,
            highContrastEnabled = highContrastEnabled,
            reduceMotionEnabled = reduceMotionEnabled,
            minimumTouchTargetSize = accessibilityManager.getMinimumTouchTargetSize()
        )
    }
}
