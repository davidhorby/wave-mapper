package com.dhorby.wavemapper

import com.dhorby.gcloud.model.WaveLocation
import org.http4k.core.Body
import org.http4k.format.Jackson.auto


val waveLocationBodyLens = Body.auto<WaveLocation>().toLens()
fun waveLocationListBodyLens() = Body.auto<MutableList<WaveLocation>>().toLens()
