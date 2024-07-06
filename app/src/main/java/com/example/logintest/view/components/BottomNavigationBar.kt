package com.example.logintest.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.Navigator
import com.example.logintest.view.ScreenCalendar
import com.example.logintest.view.ScreenMap

data class TabItem<T>(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
    val screen: T,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        TabItem<ScreenMap>(
            "Map",
            Icons.Outlined.PinDrop,
            Icons.Filled.PinDrop,
            false,
            screen = ScreenMap
        ),
        TabItem<ScreenCalendar>(
            "Events",
            Icons.Outlined.CalendarToday,
            Icons.Filled.CalendarToday,
            false,
            screen = ScreenCalendar
        ),
    )

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        contentColor = MaterialTheme.colorScheme.primary,
//        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                        Icon(
                            imageVector = if (index == selectedItemIndex) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.title,
//                            tint = MaterialTheme.colorScheme.primary
                        )
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = Color.Gray,
                    selectedIconColor = Color.White,
                    indicatorColor = MaterialTheme.colorScheme.primary
                ),
                label = {
                    Text(item.title)
                },
                alwaysShowLabel = false,
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    navController.navigate(item.screen)
                },
            )
        }
    }

}
