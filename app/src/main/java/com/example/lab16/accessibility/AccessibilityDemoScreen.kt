package com.example.lab16.accessibility

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AccessibilityDemoScreen() {
    val accessibilityState = rememberAccessibilityState()

    var textFieldValue by remember { mutableStateOf("") }
    var emailValue by remember { mutableStateOf("") }
    var switchChecked by remember { mutableStateOf(false) }

    val newsItems = remember {
        listOf(
            NewsItem(
                id = "1",
                title = "Android Accessibility News",
                description = "Google introduced new tools for creating accessible apps",
                date = "March 15, 2024",
                icon = Icons.Default.Info
            ),
            NewsItem(
                id = "2",
                title = "Material Design 3 Update",
                description = "Added improved components for screen reader support",
                date = "March 10, 2024",
                icon = Icons.Default.Favorite
            ),
            NewsItem(
                id = "3",
                title = "Jetpack Compose 1.5",
                description = "Version includes built-in semantics and accessibility support",
                date = "March 5, 2024",
                icon = Icons.Default.Build
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        DemoHeader()

        AccessibilityStatusCard(accessibilityState)

        // Input fields section
        SectionHeader(
            title = "Accessible Input Fields",
            subtitle = "Text fields with full screen reader support",
            icon = Icons.Default.Edit
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
            value = emailValue,
            onValueChange = { emailValue = it },
            label = "Email",
            placeholder = "example@domain.com",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )

        // Switches section
        SectionHeader(
            title = "Accessible Switches",
            subtitle = "Large touch targets with state descriptions",
            icon = Icons.Default.Settings
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
        SectionHeader(
            title = "Accessible News Cards",
            subtitle = "Rich descriptions for TalkBack",
            icon = Icons.AutoMirrored.Filled.List
        )

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
                    NewsCardContent(newsItem)
                }
            }
        }

        // Buttons section
        SectionHeader(
            title = "Accessible Action Buttons",
            subtitle = "Enhanced touch targets with icons",
            icon = Icons.Default.CheckCircle
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            AccessibleButton(
                onClick = { /* Save */ },
                modifier = Modifier.weight(1f),
                contentDescription = "Save changes",
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentDescription = "Submit form",
            icon = Icons.AutoMirrored.Filled.Send
        ) {
            Text("Submit Form")
        }

        // Info about accessibility
        if (accessibilityState.screenReaderEnabled) {
            TalkBackBanner()
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun DemoHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)
                    )
                )
            )
            .padding(horizontal = 20.dp, vertical = 28.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.25f),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Accessibility icon",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(12.dp)
                            .size(28.dp)
                    )
                }

                Column {
                    Text(
                        text = "Lab16",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Accessibility Demo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Text(
                text = "This app demonstrates how to build beautiful interfaces that are accessible to everyone",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.95f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(10.dp)
                    .size(20.dp)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun AccessibilityStatusCard(accessibilityState: AccessibilityState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Accessibility Status",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusChip(
                    text = "TalkBack",
                    active = accessibilityState.screenReaderEnabled,
                    modifier = Modifier.weight(1f)
                )
                StatusChip(
                    text = "Contrast",
                    active = accessibilityState.highContrastEnabled,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusChip(
                    text = "Red. Motion",
                    active = accessibilityState.reduceMotionEnabled,
                    modifier = Modifier.weight(1f)
                )
                StatusChip(
                    text = "Font Scale: ${"%.1f".format(accessibilityState.fontScale)}x",
                    active = accessibilityState.fontScale != 1.0f,
                    modifier = Modifier.weight(1f)
                )
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Min Touch Target: ${accessibilityState.minimumTouchTargetSize.value}dp",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun StatusChip(
    text: String,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (active) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        },
        shape = RoundedCornerShape(10.dp),
        border = if (!active) {
            androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        } else {
            null
        },
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (active) {
                            MaterialTheme.colorScheme.tertiary
                        } else {
                            MaterialTheme.colorScheme.outline
                        }
                    )
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = if (active) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
private fun NewsCardContent(newsItem: NewsItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                imageVector = newsItem.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(12.dp)
                    .size(24.dp)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = newsItem.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = newsItem.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = newsItem.date,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )

                AccessibleIcon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Open news details",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun TalkBackBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Screen Reader Active",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "All elements on this page have proper descriptions for TalkBack",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.85f)
                )
            }
        }
    }
}

data class NewsItem(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val icon: ImageVector
)
