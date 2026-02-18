package com.gonx.criptotest

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform