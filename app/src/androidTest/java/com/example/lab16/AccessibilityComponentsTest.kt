package com.example.lab16

import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.lab16.accessibility.AccessibilityState
import com.example.lab16.accessibility.AccessibleButton
import com.example.lab16.accessibility.AccessibleCard
import com.example.lab16.accessibility.AccessibleSwitch
import com.example.lab16.accessibility.AccessibleTextField
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccessibilityComponentsTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun testAccessibleButtonHasCorrectSemantics() {
        composeTestRule.setContent {
            AccessibleButton(
                onClick = { },
                contentDescription = "Test Button",
                icon = null
            ) {
                Text("Press me")
            }
        }
        
        composeTestRule.onNodeWithTag("button_Test_Button")
            .assert(SemanticsMatcher.expectValue(
                SemanticsProperties.ContentDescription, 
                listOf("Test Button")
            ))
            .assert(SemanticsMatcher.expectValue(
                SemanticsProperties.Role,
                Role.Button
            ))
            .assertIsEnabled()
    }
    
    @Test
    fun testAccessibleTextFieldAnnouncesCharacterCount() {
        composeTestRule.setContent {
            var text by remember { mutableStateOf("Hello") }
            AccessibleTextField(
                value = text,
                onValueChange = { text = it },
                label = "Name",
                placeholder = "Enter name"
            )
        }
        
        composeTestRule.onNodeWithTag("textfield_Name")
            .performTextInput(" World")
            
        composeTestRule.onNodeWithTag("textfield_Name")
            .assert(SemanticsMatcher.keyIsDefined(
                SemanticsProperties.StateDescription
            ))
    }
    
    @Test
    fun testAccessibleSwitchHasToggleableRole() {
        composeTestRule.setContent {
            var checked by remember { mutableStateOf(false) }
            AccessibleSwitch(
                checked = checked,
                onCheckedChange = { checked = it },
                label = "Notifications",
                supportingText = "Enable notifications"
            )
        }
        
        composeTestRule.onNodeWithTag("switch_Notifications")
            .assert(SemanticsMatcher.expectValue(
                SemanticsProperties.Role,
                Role.Switch
            ))
            .assert(SemanticsMatcher.expectValue(
                SemanticsProperties.ToggleableState,
                ToggleableState.Off
            ))
            .performClick()
            .assert(SemanticsMatcher.expectValue(
                SemanticsProperties.ToggleableState,
                ToggleableState.On
            ))
    }
    
    @Test
    fun testNewsCardHasFullDescription() {
        composeTestRule.setContent {
            AccessibleCard(
                onClick = { },
                contentDescription = "News: Title. Description. Date: today"
            ) {
                Text("Title")
                Text("Description")
                Text("today")
            }
        }
        
        composeTestRule.onNode(
            hasContentDescription("News: Title. Description. Date: today")
        ).assertExists()
    }
    
    @Test
    fun testMinimumTouchTargetSize() {
        composeTestRule.setContent {
            AccessibleButton(
                onClick = { },
                contentDescription = "Large Button",
                accessibilityState = AccessibilityState(
                    screenReaderEnabled = true,
                    fontScale = 1.0f,
                    highContrastEnabled = false,
                    reduceMotionEnabled = false,
                    minimumTouchTargetSize = 48.dp
                )
            ) {
                Text("Press")
            }
        }
        
        composeTestRule.onNodeWithTag("button_Large_Button")
            .assertWidthIsAtLeast(48.dp)
            .assertHeightIsAtLeast(48.dp)
    }
}
