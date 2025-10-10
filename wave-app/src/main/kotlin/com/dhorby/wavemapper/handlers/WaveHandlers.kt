package com.dhorby.wavemapper.handlers

import com.dhorby.gcloud.model.Location
import com.dhorby.wavemapper.port.WavePort
import com.dhorby.wavemapper.waveLocationListBodyLens
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.body.form
import org.http4k.core.toParametersMap
import org.http4k.core.with
import org.http4k.lens.Query
import org.http4k.lens.float
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel

class WaveHandlers(
    val wavePort: WavePort,
) {
    val latQuery = Query.float().required("lat")
    val lonQuery = Query.float().required("lon")

    private val devMode = false

    val renderer =
        when {
            devMode -> HandlebarsTemplates().HotReload("src/main/resources")
            else -> HandlebarsTemplates().CachingClasspath()
        }

    fun getWavePage(): HttpHandler =
        {
            val waveViewModel: ViewModel? = wavePort.getWavePage()
            waveViewModel?.let {
                try {
                    Response(OK).body(renderer(waveViewModel))
                } catch (e: Exception) {
                    Response(OK).body(e.stackTraceToString())
                }
            } ?: Response(Status.INTERNAL_SERVER_ERROR)
        }

    fun getWaveData(): HttpHandler =
        {
            val allWaveData: MutableList<Location> = wavePort.getWaveData()
            Response(OK).with(waveLocationListBodyLens of allWaveData)
        }

    fun getLocationData(): HttpHandler =
        {
            val lat: Float = latQuery(it)
            val lon = lonQuery(it)
            val bodyString = wavePort.getLocationData(lat, lon)
            Response(OK).body(bodyString)
        }

    fun addPiece(): HttpHandler =
        { request ->
            val parametersMap: Map<String, List<String?>> = request.form().toParametersMap()
            wavePort.addPiece(parametersMap)
            Response(FOUND).header("Location", "/")
        }

    fun start(): HttpHandler =
        {
            wavePort.startRace()
            Response(FOUND).header("Location", "/")
        }

    fun move(): HttpHandler =
        {
            wavePort.move()
            Response(FOUND).header("Location", "/")
        }

    fun clear(): HttpHandler =
        {
            wavePort.clear()
            Response(FOUND).header("Location", "/")
        }
}
