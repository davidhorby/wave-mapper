package com.dhorby.wavemapper.model

import org.http4k.core.Body
import org.http4k.format.Jackson.auto


data class WaveLocation(
    override val id: String,
    override val name: String,
    override val geoLocation: GeoLocation,
    val waveDataReadings: List<WaveDataReading>
):Location

val waveLocationBodyLens = Body.auto<WaveLocation>().toLens()
fun waveLocationListBodyLens() = Body.auto<MutableList<Location>>().toLens()

