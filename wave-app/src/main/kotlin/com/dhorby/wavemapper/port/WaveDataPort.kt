package com.dhorby.wavemapper.port

import model.Location

interface WaveDataPort {
    fun getAllWaveData(): List<Location>
}
