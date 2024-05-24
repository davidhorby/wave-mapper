package com.dhorby.wavemapper.endpoints.http

import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.wavemapper.DataForSiteFunction
import com.dhorby.gcloud.wavemapper.SiteListFunction
import com.dhorby.gcloud.wavemapper.getAllWaveData
import com.dhorby.wavemapper.waveLocationListBodyLens
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with

fun WaveData(siteListFunction: SiteListFunction, dataForSiteFunction: DataForSiteFunction): HttpHandler = {
    val allWaveData: MutableList<Location> = getAllWaveData(siteListFunction, dataForSiteFunction)
    Response(Status.OK).with(waveLocationListBodyLens of allWaveData)
}