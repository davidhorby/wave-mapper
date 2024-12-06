package com.dhorby.wavemapper.adapter

import AddRequestCount
import com.dhorby.gcloud.external.storage.DataStoreClient
import com.dhorby.gcloud.model.GeoLocation
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.wavemapper.fake.FakeDatastore
import org.http4k.events.AutoMarshallingEvents
import org.http4k.events.AutoMarshallingEvents.invoke
import org.http4k.events.Event
import org.http4k.events.EventFilters
import org.http4k.events.then
import org.http4k.format.Jackson

class WaveAdapterTest {

    private val datastore = FakeDatastore().dataStore
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

//    @Test
//    fun `wave page should be present`(){
//        val waveAdapter = WaveAdapter(
//            siteListFunction = WaveServiceFunctions.siteListFunction,
//            dataForSiteFunction = WaveServiceFunctions.dataForSiteFunction,
//            storageAdapter = storageAdapter,
//            googleMapsClient = GoogleMapsClient(ApacheClient())
//        )
//        storageAdapter.add(pieceLocation)
////        val wavePage: ViewModel? = waveAdapter.getWavePage()
////        assertThat(wavePage, present())
//    }
}