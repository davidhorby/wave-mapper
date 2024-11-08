package com.dhorby.wavemapper.adapter

import com.dhorby.gcloud.config.Settings
import com.dhorby.gcloud.wavemapper.Constants.mapsApiKey
import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormat
import com.dhorby.wavemapper.handlers.WaveHandlers
import com.dhorby.wavemapper.model.WavePage
import com.dhorby.wavemapper.port.StoragePort
import com.dhorby.wavemapper.port.WavePort
import org.http4k.template.ViewModel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class WaveAdapter(
    val storageAdapter: StoragePort
): WavePort {
    val LOG: Logger = LoggerFactory.getLogger(WaveHandlers::class.java)

    override fun getWavePage(): ViewModel? {
        val theData: String = storageAdapter.getLocationData().toGoogleMapFormat()
        val viewModel: ViewModel? =
            mapsApiKey.let { mapsApiKey ->
                try {
                    WavePage(
                        waveData = theData,
                        mapsApiKey = mapsApiKey,
                        players = storageAdapter.getDistances(),
                        hostname = Settings.HOST,
                        port = Settings.PORT
                    )
                } catch (e: Exception) {
                    LOG.error("Failed to get wave data", e)
                    null
                }
            }
        return viewModel
    }
}