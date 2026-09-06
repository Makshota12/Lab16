package com.example.lab16.accessibility

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.*
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Accessible button with enhanced semantics and touch target size
 */
@Composable
fun AccessibleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accessibilityState: AccessibilityState = rememberAccessibilityState(),
    contentDescription: String,
    icon: ImageVector? = null,
    content: @Composable RowScope.() -> Unit
) {
    // Calculate minimum touch target size
    val minSize = accessibilityState.minimumTouchTargetSize
    
    // Create description considering state
    val fullDescription = remember(contentDescription, enabled) {
        if (enabled) {
            contentDescription
        } else {
            "$contentDescription. Disabled"
        }
    }
    
    Button(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minWidth = minSize, minHeight = minSize)
            .semantics {
                // Main semantic properties
                this.contentDescription = fullDescription
                this.role = Role.Button
                if (!enabled) this.disabled()
                
                // Additional info for screen readers
                if (icon != null) {
                    this.customActions = listOf(
                        CustomAccessibilityAction(
                            "Press button $contentDescription",
                            { onClick(); true }
                        )
                    )
                }
                
                // Indicate this is a traversal group
                this.isTraversalGroup = true
            }
            .testTag("button_${contentDescription.replace(" ", "_")}"),
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = if (accessibilityState.highContrastEnabled) {
            // Enhanced colors for high contrast
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                contentColor = Color.White,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        } else {
            ButtonDefaults.buttonColors()
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null, // Already described in parent
                    modifier = Modifier.size(20.dp)
                )
            }
            content()
        }
    }
}

/**
 * Accessible text field with full screen reader support
 */
@Composable
fun AccessibleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String = "",
    isError: Boolean = false,
    supportingText: String? = null,
    accessibilityState: AccessibilityState = rememberAccessibilityState(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    val focusManager = LocalFocusManager.current
    
    // Adapt text size - capture typography outside remember
    val typography = MaterialTheme.typography
    val textStyle = remember(accessibilityState.fontScale, typography) {
        TextStyle(
            fontSize = typography.bodyLarge.fontSize * accessibilityState.fontScale
        )
    }
    
    // Create semantic description
    val semanticsDescription = remember(label, placeholder, isError, supportingText) {
        buildString {
            append("Input field: $label")
            if (placeholder.isNotEmpty()) {
                append(". Example: $placeholder")
            }
            if (isError) {
                append(". Has error")
            }
            supportingText?.let {
                append(". $it")
            }
        }
    }
    
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                this.contentDescription = semanticsDescription
                // Removed invalid Role.TextField
                this.editableText = androidx.compose.ui.text.AnnotatedString(value)
                this.setText { text ->
                    onValueChange(text.text)
                    true
                }
                
                // For screen readers: read current value
                this.stateDescription = "Entered ${value.length} characters"
                
                // Indicate this is a traversal group
                this.isTraversalGroup = true
            }
            .testTag("textfield_${label.replace(" ", "_")}"),
        label = {
            Text(
                text = label,
                style = textStyle
            )
        },
        placeholder = {
            if (placeholder.isNotEmpty()) {
                Text(
                    text = placeholder,
                    style = textStyle.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        },
        isError = isError,
        supportingText = {
            supportingText?.let {
                Text(
                    text = it,
                    style = textStyle.copy(
                        fontSize = textStyle.fontSize * 0.9f
                    ),
                    color = if (isError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        },
        singleLine = singleLine,
        maxLines = maxLines,
        textStyle = textStyle,
        colors = if (accessibilityState.highContrastEnabled) {
            // High contrast colors
            TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
                errorCursorColor = MaterialTheme.colorScheme.error,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                errorIndicatorColor = MaterialTheme.colorScheme.error
            )
        } else {
            TextFieldDefaults.colors()
        },
        keyboardOptions = keyboardOptions.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                keyboardActions.onDone?.invoke(this)
            },
            onGo = keyboardActions.onGo,
            onNext = keyboardActions.onNext,
            onPrevious = keyboardActions.onPrevious,
            onSearch = keyboardActions.onSearch,
            onSend = keyboardActions.onSend
        ),
        shape = RoundedCornerShape(14.dp)
    )
}

/**
 * Accessible switch with improved touch target
 */
@Composable
fun AccessibleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accessibilityState: AccessibilityState = rememberAccessibilityState(),
    label: String,
    supportingText: String? = null
) {
    val minSize = accessibilityState.minimumTouchTargetSize
    
    // Description for screen reader
    val stateDescription = remember(checked) {
        if (checked) "On" else "Off"
    }
    
    val fullDescription = remember(label, stateDescription) {
        "$label. Current state: $stateDescription"
    }
    
    Row(
        modifier = modifier
            .toggleable(
                value = checked,
                enabled = enabled,
                onValueChange = onCheckedChange,
                role = Role.Switch,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .defaultMinSize(minHeight = minSize)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (enabled) 0.6f else 0.3f)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .semantics(mergeDescendants = true) {
                this.contentDescription = fullDescription
                this.role = Role.Switch
                if (!enabled) this.disabled()
                this.toggleableState = ToggleableState(checked)
                this.stateDescription = stateDescription
            }
            .testTag("switch_${label.replace(" ", "_")}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                }
            )
            
            supportingText?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize * 
                                 accessibilityState.fontScale * 0.9f
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Switch(
            checked = checked,
            onCheckedChange = null, // Controlled by Row.toggleable
            enabled = enabled,
            modifier = Modifier
                .size(minSize) // Not exactly correct to size Switch itself, but for touch target it's okay if parent handles it
                .padding(12.dp),
            colors = if (accessibilityState.highContrastEnabled) {
                SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = MaterialTheme.colorScheme.outline
                )
            } else {
                SwitchDefaults.colors()
            }
        )
    }
}

/**
 * Accessible card with correct semantics
 */
@Composable
fun AccessibleCard(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    accessibilityState: AccessibilityState = rememberAccessibilityState(),
    contentDescription: String,
    testTag: String = "",
    content: @Composable ColumnScope.() -> Unit
) {
    val minSize = accessibilityState.minimumTouchTargetSize
    val isClickable = onClick != null
    
    val semanticsModifier = if (isClickable) {
        modifier
            .clickable(
                onClick = onClick!!,
                enabled = true,
                role = Role.Button
            )
            .semantics {
                this.contentDescription = contentDescription
                this.role = Role.Button
                this.isTraversalGroup = true
            }
    } else {
        modifier.semantics {
            this.contentDescription = contentDescription
            this.role = Role.Image // Using Image or similar generic role if not clickable, or just none if it's a container
            this.isTraversalGroup = true
        }
    }
    
    Card(
        modifier = semanticsModifier
            .then(if (isClickable) Modifier.defaultMinSize(
                minWidth = minSize,
                minHeight = minSize
            ) else Modifier)
            .testTag(if (testTag.isNotEmpty()) testTag else "card_${contentDescription.hashCode()}"),
        colors = CardDefaults.cardColors(
            containerColor = if (accessibilityState.highContrastEnabled) {
                MaterialTheme.colorScheme.surfaceContainerHigh
            } else {
                MaterialTheme.colorScheme.surfaceContainerLow
            }
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (accessibilityState.highContrastEnabled) 4.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            content()
        }
    }
}

/**
 * Accessible icon with description
 */
@Composable
fun AccessibleIcon(
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    size: Dp = 24.dp
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier
            .size(size)
            .semantics {
                this.contentDescription = contentDescription
                this.role = Role.Image
            }
            .testTag("icon_${contentDescription.replace(" ", "_")}"),
        tint = tint
    )
}
