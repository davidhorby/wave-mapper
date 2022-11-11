package com.dhorby.wavemapper.model

sealed interface Location {
    val id: String
    val name: String
}
