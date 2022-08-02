package net.listadoko.myfirstkmm2.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

// TODO: fix
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BottomNavigation()
        }
    }

    sealed class Item(var title: String, var icon: ImageVector) {
        object Calculator: Item("Calculator", Icons.Filled.Add)
        object Repo: Item("Repo", Icons.Filled.List)
    }

    @Composable
    private fun BottomNavigation() {
        val selectedItem = remember { mutableStateOf(0)}
        val items = listOf(Item.Calculator, Item.Repo)

        BottomNavigation() {
            items.forEachIndexed { index, item ->
                BottomNavigationItem(
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(item.title) },
                    selected = selectedItem.value == index,
                    onClick = { selectedItem.value = index }
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun BottomNavigationPreview() {
        BottomNavigation()
    }
}
