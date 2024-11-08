package com.dhorby.wavemapper.fake

import com.dhorby.wavemapper.port.WavePort
import org.http4k.template.ViewModel

class FakeWaveAdapter: WavePort {
    override fun getWavePage(): ViewModel? {
        TODO("Not yet implemented")
    }
}