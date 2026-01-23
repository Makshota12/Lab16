package com.example.lab16.accessibility

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AccessibilityDemoScreen() {
    val accessibilityState = rememberAccessibilityState()
    
    var textFieldValue by remember { mutableStateOf("") }
    var switchChecked by remember { mutableStateOf(false) }
    
    val newsItems = remember {
        listOf(
            NewsItem(
                id = "1",
                title = "Android Accessibility News",
                description = "Google introduced new tools for creating accessible apps",
                date = "March 15, 2024"
            ),
            NewsItem(
                id = "2",
                title = "Material Design 3 Update",
                description = "Added improved components for screen reader support",
                date = "March 10, 2024"
            ),
            NewsItem(
                id = "3",
                title = "Jetpack Compose 1.5",
                description = "Version includes built-in semantics and accessibility support",
                date = "March 5, 2024"
            )
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Status card
        AccessibilityStatusCard(accessibilityState)
        
        // Input fields section
        Text(
            text = "Accessible Input Fields",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        AccessibleTextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = "Username",
            placeholder = "Enter your name",
            supportingText = "Minimum 3 characters",
            isError = textFieldValue.isNotEmpty() && textFieldValue.length < 3,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        
        AccessibleTextField(
            value = "",
            onValueChange = {},
            label = "Email",
            placeholder = "example@domain.com",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )
        
        // Switches section
        Text(
            text = "Accessible Switches",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        AccessibleSwitch(
            checked = switchChecked,
            onCheckedChange = { switchChecked = it },
            label = "Notifications",
            supportingText = "Receive push notifications"
        )
        
        AccessibleSwitch(
            checked = true,
            onCheckedChange = {},
            label = "Dark Mode",
            supportingText = "Use dark theme (Disabled)",
            enabled = false
        )
        
        // News cards section
        Text(
            text = "Accessible News Cards",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        // We use Column instead of LazyColumn here because we are already in a verticalScroll
        // nesting scrollable containers is not ideal but for this demo with few items it's fine 
        // if we disable scrolling or just map them.
        // Better to just loop them in the Column since the outer column is scrollable.
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            newsItems.forEach { newsItem ->
                AccessibleCard(
                    onClick = {
                        // Navigation to details
                    },
                    contentDescription = "News: ${newsItem.title}. ${newsItem.description}. Date: ${newsItem.date}",
                    testTag = "news_card_${newsItem.id}"
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = newsItem.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Text(
                            text = newsItem.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = newsItem.date,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            
                            AccessibleIcon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Open news details",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
        
        // Buttons section
        Text(
            text = "Accessible Action Buttons",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AccessibleButton(
                onClick = { /* Save */ },
                modifier = Modifier.weight(1f),
                contentDescription = "Save changes",
                // Used Check instead of Save to avoid missing icon issues
                icon = Icons.Default.Check 
            ) {
                Text("Save")
            }
            
            AccessibleButton(
                onClick = { /* Cancel */ },
                modifier = Modifier.weight(1f),
                contentDescription = "Cancel changes",
                icon = Icons.Default.Close,
                enabled = false
            ) {
                Text("Cancel")
            }
        }
        
        AccessibleButton(
            onClick = { /* Send */ },
            modifier = Modifier.fillMaxWidth(),
            contentDescription = "Submit form",
            icon = Icons.Default.Send
        ) {
            Text("Submit Form")
        }
        
        // Info about accessibility
        if (accessibilityState.screenReaderEnabled) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Screen Reader Active",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "All elements on this page have proper descriptions for TalkBack",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun AccessibilityStatusCard(accessibilityState: AccessibilityState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant // Fallback for containerHigh if not available
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Accessibility Status",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusChip(
                    text = "TalkBack",
                    active = accessibilityState.screenReaderEnabled
                )
                StatusChip(
                    text = "Contrast",
                    active = accessibilityState.highContrastEnabled
                )
            }
             Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusChip(
                    text = "Red. Motion",
                    active = accessibilityState.reduceMotionEnabled
                )
                 StatusChip(
                    text = "Font Scale: ${"%.1f".format(accessibilityState.fontScale)}x",
                    active = accessibilityState.fontScale != 1.0f
                )
            }
            
            Text(
                text = "Min Touch Target: ${accessibilityState.minimumTouchTargetSize.value}dp",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StatusChip(text: String, active: Boolean) {
    Surface(
        color = if (active) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        },
        shape = MaterialTheme.shapes.small,
        border = if (!active) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null
    ) {
        Text(
            text = if (active) "$text ✓" else text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelSmall,
            color = if (active) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

data class NewsItem(
    val id: String,
    val title: String,
    val description: String,
    val date: String
)
