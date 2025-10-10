package com.dhorby.wavemapper.endpoints.http

import com.dhorby.gcloud.model.Location
import com.dhorby.wavemapper.port.WaveDataPort
import com.dhorby.wavemapper.waveLocationListBodyLens
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with

fun waveData(waveDataAdapter: WaveDataPort): HttpHandler =
    {
        val allWaveData: List<Location> = waveDataAdapter.getAllWaveData()
        Response(Status.OK).with(waveLocationListBodyLens of allWaveData)
    }
