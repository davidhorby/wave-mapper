package com.dhorby.wavemapper.port

import com.dhorby.wavemapper.model.Location

interface WaveDataPort {
    fun getAllWaveData(): List<Location>
}
