package com.dhorby.wavemapper.handlers

import com.dhorby.wavemapper.siteListFunction
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.Request
import org.http4k.core.Response
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class WaveHandlersTest {

    @Test
    fun getWavePage() {
        val wavePage: (Request) -> Response = WaveHandlers(siteListFunction = siteListFunction,  "", "").getWaveData()
    }

    @Test
    fun getWaveData() {
    }
}
