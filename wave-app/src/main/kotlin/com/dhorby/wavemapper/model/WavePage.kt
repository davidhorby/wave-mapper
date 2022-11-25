package com.dhorby.wavemapper.model

import org.http4k.template.ViewModel

data class WavePage(val waveData: String, val mapsApiKey: String): ViewModel
