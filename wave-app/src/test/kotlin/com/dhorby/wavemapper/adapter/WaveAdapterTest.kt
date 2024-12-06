package com.dhorby.wavemapper.adapter

import AddRequestCount
import com.dhorby.gcloud.external.junit.DataStoreExtension
import com.dhorby.gcloud.external.storage.DataStoreClient
import com.dhorby.gcloud.model.GeoLocation
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.wavemapper.WaveServiceFunctions
import com.dhorby.wavemapper.external.google.GoogleMapsClient
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import org.http4k.client.ApacheClient
import org.http4k.events.AutoMarshallingEvents
import org.http4k.events.AutoMarshallingEvents.invoke
import org.http4k.events.Event
import org.http4k.events.EventFilters
import org.http4k.events.then
import org.http4k.format.Jackson
import org.http4k.template.ViewModel
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class WaveAdapterTest {

    companion object {
        @JvmStatic
        @RegisterExtension
        val server = DataStoreExtension()
            .builder()
            .build()
    }

    private val datastore = server.localDatastoreHelper.options.service

    private val events: (Event) -> Unit =
        EventFilters.AddTimestamp()
            .then(EventFilters.AddEventName())
            .then(EventFilters.AddZipkinTraces())
            .then(AddRequestCount())
            .then(AutoMarshallingEvents(Jackson))
    val storageAdapter = StorageAdapter(DataStoreClient(events, datastore))

    val pieceLocation = PieceLocation(
        id = "test",
        name = "test piece",
        geoLocation = GeoLocation(-0.34, 45.03),
        pieceType = PieceType.BOAT
    )

    @Test
    fun `wave page should be present`(){
        val waveAdapter = WaveAdapter(
            siteListFunction = WaveServiceFunctions.siteListFunction,
            dataForSiteFunction = WaveServiceFunctions.dataForSiteFunction,
            storageAdapter = storageAdapter,
            googleMapsClient = GoogleMapsClient(ApacheClient())
        )
        storageAdapter.add(pieceLocation)
        val wavePage: ViewModel? = waveAdapter.getWavePage()
        assertThat(wavePage, present())
    }
}