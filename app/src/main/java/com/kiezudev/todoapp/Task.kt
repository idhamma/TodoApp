package com.kiezudev.todoapp

data class Task(
    val id: String? = null, // Menambahkan ID
    val name: String = "",
    val isCompleted: Boolean = false
) {
    // Konstruktor tanpa argumen diperlukan untuk Firebase
    constructor() : this(null, "",false)
}
