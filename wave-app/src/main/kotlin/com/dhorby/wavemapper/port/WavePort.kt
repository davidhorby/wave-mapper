package com.dhorby.wavemapper.port

import org.http4k.template.ViewModel

interface WavePort {
    fun getWavePage(): ViewModel?
}