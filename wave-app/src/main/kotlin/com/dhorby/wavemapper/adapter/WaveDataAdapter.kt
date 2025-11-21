package com.dhorby.wavemapper.adapter

import com.dhorby.wavemapper.model.Location
import com.dhorby.wavemapper.model.Site
import com.dhorby.wavemapper.model.WaveLocation
import com.dhorby.wavemapper.port.WaveDataPort
import com.dhorby.wavemapper.wavemapper.DataForSiteFunction
import com.dhorby.wavemapper.wavemapper.SiteListFunction

class WaveDataAdapter(
    val siteListFunction: SiteListFunction,
    val dataForSiteFunction: DataForSiteFunction,
) : WaveDataPort {
    override fun getAllWaveData(): List<Location> {
        val mapNotNull: List<WaveLocation> =
            siteListFunction().mapNotNull { site: Site ->
                dataForSiteFunction(site.id)
            }
        return mapNotNull
            .filter { location ->
                location.id.isNotEmpty()
            }.toMutableList()
    }
}
