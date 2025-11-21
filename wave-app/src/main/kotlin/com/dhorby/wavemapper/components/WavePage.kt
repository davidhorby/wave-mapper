package com.dhorby.wavemapper.components

import model.Player
import org.http4k.template.ViewModel

data class WavePage(
    val waveData: String,
    val mapsApiKey: String,
    val players: List<Player>,
    val hostname: String = "localhost",
    val port: Int = 8080,
    val template: String,
) : ViewModel {
    override fun template(): String = template
}
