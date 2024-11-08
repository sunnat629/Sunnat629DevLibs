package dev.sunnat629.libs

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform