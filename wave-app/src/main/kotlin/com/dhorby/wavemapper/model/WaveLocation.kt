package com.dhorby.wavemapper.model

import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation
import org.http4k.core.Body
import org.http4k.format.Jackson.auto


data class WaveLocation(
    val id: String,
    val name: String,
    override val geoLocation: GeoLocation,
    val waveDataReadings: List<WaveDataReading>
):Location

val waveLocationBodyLens = Body.auto<WaveLocation>().toLens()
fun waveLocationListBodyLens() = Body.auto<MutableList<WaveLocation>>().toLens()

