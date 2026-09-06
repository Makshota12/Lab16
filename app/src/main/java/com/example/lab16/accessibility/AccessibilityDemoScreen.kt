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
                title = "Новости доступности Android",
                description = "Google представил новые инструменты для создания доступных приложений",
                date = "15 марта 2024",
                icon = Icons.Default.Info
            ),
            NewsItem(
                id = "2",
                title = "Обновление Material Design 3",
                description = "Добавлены улучшенные компоненты для поддержки экранного диктора",
                date = "10 марта 2024",
                icon = Icons.Default.Favorite
            ),
            NewsItem(
                id = "3",
                title = "Jetpack Compose 1.5",
                description = "Версия включает встроенную поддержку семантики и доступности",
                date = "5 марта 2024",
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

        // Секция полей ввода
        SectionHeader(
            title = "Доступные поля ввода",
            subtitle = "Текстовые поля с полной поддержкой экранного диктора",
            icon = Icons.Default.Edit
        )

        AccessibleTextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = "Имя пользователя",
            placeholder = "Введите имя",
            supportingText = "Минимум 3 символа",
            isError = textFieldValue.isNotEmpty() && textFieldValue.length < 3,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )

        AccessibleTextField(
            value = emailValue,
            onValueChange = { emailValue = it },
            label = "Электронная почта",
            placeholder = "example@domain.com",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )

        // Секция переключателей
        SectionHeader(
            title = "Доступные переключатели",
            subtitle = "Увеличенные области нажатия с описанием состояния",
            icon = Icons.Default.Settings
        )

        AccessibleSwitch(
            checked = switchChecked,
            onCheckedChange = { switchChecked = it },
            label = "Уведомления",
            supportingText = "Получать push-уведомления"
        )

        AccessibleSwitch(
            checked = true,
            onCheckedChange = {},
            label = "Тёмная тема",
            supportingText = "Использовать тёмную тему (Отключено)",
            enabled = false
        )

        // Секция карточек новостей
        SectionHeader(
            title = "Доступные карточки новостей",
            subtitle = "Подробные описания для TalkBack",
            icon = Icons.AutoMirrored.Filled.List
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            newsItems.forEach { newsItem ->
                AccessibleCard(
                    onClick = {
                        // Навигация к подробностям
                    },
                    contentDescription = "Новость: ${newsItem.title}. ${newsItem.description}. Дата: ${newsItem.date}",
                    testTag = "news_card_${newsItem.id}"
                ) {
                    NewsCardContent(newsItem)
                }
            }
        }

        // Секция кнопок действий
        SectionHeader(
            title = "Доступные кнопки действий",
            subtitle = "Увеличенные области нажатия с иконками",
            icon = Icons.Default.CheckCircle
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            AccessibleButton(
                onClick = { /* Сохранить */ },
                modifier = Modifier.weight(1f),
                contentDescription = "Сохранить изменения",
                icon = Icons.Default.Check
            ) {
                Text("Сохранить")
            }

            AccessibleButton(
                onClick = { /* Отмена */ },
                modifier = Modifier.weight(1f),
                contentDescription = "Отменить изменения",
                icon = Icons.Default.Close,
                enabled = false
            ) {
                Text("Отмена")
            }
        }

        AccessibleButton(
            onClick = { /* Отправить */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentDescription = "Отправить форму",
            icon = Icons.AutoMirrored.Filled.Send
        ) {
            Text("Отправить форму")
        }

        // Информация о доступности
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
                        contentDescription = "Значок доступности",
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
                        text = "Демонстрация доступности",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Text(
                text = "Это приложение показывает, как создавать красивые интерфейсы, доступные каждому пользователю",
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
                    text = "Статус доступности",
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
                    text = "Контрастность",
                    active = accessibilityState.highContrastEnabled,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatusChip(
                    text = "Уменьш. анимации",
                    active = accessibilityState.reduceMotionEnabled,
                    modifier = Modifier.weight(1f)
                )
                StatusChip(
                    text = "Шрифт: ${"%.1f".format(accessibilityState.fontScale)}x",
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
                    text = "Мин. область нажатия: ${accessibilityState.minimumTouchTargetSize.value}dp",
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
                    contentDescription = "Открыть подробности новости",
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
                    text = "Экранный диктор активен",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Все элементы этой страницы имеют корректные описания для TalkBack",
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