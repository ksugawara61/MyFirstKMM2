package net.listadoko.myfirstkmm2.feature.todoList

data class TodoItem(
    val id: Long = 0L,
    val order: Long = 0L,
    val text: String = "",
    val isDone: Boolean = false
)
