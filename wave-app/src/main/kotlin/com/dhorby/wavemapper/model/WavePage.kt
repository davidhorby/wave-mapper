package com.dhorby.wavemapper.model

import com.dhorby.gcloud.model.Player
import org.http4k.template.ViewModel

data class WavePage(
    val waveData: String,
    val mapsApiKey: String,
    val players: List<Player>,
    val hostname:String = "localhost",
    val port:Int = 8080
): ViewModel
