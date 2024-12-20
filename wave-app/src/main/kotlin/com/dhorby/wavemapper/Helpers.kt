package com.dhorby.wavemapper

import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.model.WaveLocation
import org.http4k.core.Body
import org.http4k.format.Jackson.auto


val waveLocationBodyLens = Body.auto<WaveLocation>().toLens()
val waveLocationListBodyLens = Body.auto<List<Location>>().toLens()
