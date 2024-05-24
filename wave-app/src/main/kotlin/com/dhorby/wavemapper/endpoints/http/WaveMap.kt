package com.dhorby.wavemapper.endpoints.http

import com.dhorby.gcloud.wavemapper.Constants
import com.dhorby.wavemapper.model.GMap
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.template.HandlebarsTemplates

fun WaveMap(): HttpHandler = {
    val devMode = false
    val renderer = when {
        devMode -> HandlebarsTemplates().HotReload("src/main/resources")
        else -> HandlebarsTemplates().CachingClasspath()
    }
    GMap(Constants.mapsApiKey).let {
        try {
            Response(Status.OK).body(renderer(it))
        } catch (e: Exception) {
            Response(Status.OK).body(e.stackTraceToString())
        }
    }

}