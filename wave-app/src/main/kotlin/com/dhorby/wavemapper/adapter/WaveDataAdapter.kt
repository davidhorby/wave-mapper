package com.dhorby.wavemapper.adapter

import com.dhorby.gcloud.wavemapper.DataForSiteFunction
import com.dhorby.gcloud.wavemapper.SiteListFunction
import com.dhorby.wavemapper.port.WaveDataPort
import model.Location
import model.WaveLocation

class WaveDataAdapter(
    val siteListFunction: SiteListFunction,
    val dataForSiteFunction: DataForSiteFunction,
) : WaveDataPort {
    override fun getAllWaveData(): List<Location> {
        val mapNotNull: List<WaveLocation> =
            siteListFunction().mapNotNull { site ->
                dataForSiteFunction(site.id)
            }
        return mapNotNull
            .filter { location ->
                location.id.isNotEmpty()
            }.toMutableList()
    }
}
