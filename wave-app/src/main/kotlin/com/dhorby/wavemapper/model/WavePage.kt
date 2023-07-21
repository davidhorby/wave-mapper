package com.dhorby.wavemapper.model

import com.dhorby.gcloud.model.Player
import org.http4k.template.ViewModel

data class WavePage(val waveData: String, val mapsApiKey: String, val stats: List<Player>): ViewModel
