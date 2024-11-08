package dev.sunnat629.libs

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Sunnat629DevLibs",
    ) {
        App()
    }
}