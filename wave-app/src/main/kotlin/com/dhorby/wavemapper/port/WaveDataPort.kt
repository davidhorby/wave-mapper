package com.dhorby.wavemapper.port

import com.dhorby.gcloud.model.Location

interface WaveDataPort {
    fun getAllWaveData(): List<Location>
}