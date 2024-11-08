package com.dhorby.wavemapper.port

import com.dhorby.gcloud.model.Location
import org.http4k.template.ViewModel

interface WavePort {
    fun getWavePage(): ViewModel?
    fun getWaveData():MutableList<Location>
    fun getLocationData(lat:Float, lon:Float): String
    fun addPiece(parametersMap: Map<String, List<String?>>): Boolean
}