package com.dhorby.gcloud.data

import com.dhorby.gcloud.model.*
import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation
import com.dhorby.wavemapper.model.*
import java.time.LocalDate

val siteId = "12345"
val siteName = "TestSite"

val sharkId = "Monty"
val sharkSite = "Eastern Atlantic"

object TestData {

    val testSiteLocation = Site(
        id = siteId,
        name = siteName,
        geoLocation = GeoLocation(lat = 12.34F, lon = 45.45F),
        obsSource = "FM-13 SHIP",
        obsRegion = "West Coast",
        obsLocationType = "Buoy"
    )

    val testSharkLocation = PieceLocation(
        id = "1234",
        name = "Sue",
        geoLocation = GeoLocation(lat = 34.45F, lon = 49.01F),
        pieceType = PieceType.SHARK
    )

    val testBoatLocation = PieceLocation(
        id = "234ea",
        name = "Albert",
        geoLocation = GeoLocation(lat = 39.45F, lon = -5.01F),
        pieceType = PieceType.BOAT
    )

    val validWaveData = WaveDataReading(LocalDate.now(), 2.1F, 23, "SSW")

    val testWaveLocation = WaveLocation(
        id = siteId,
        name = siteName,
        geoLocation = GeoLocation(lat = -13.34F, lon = 45.00F),
        waveDataReadings = listOf(validWaveData)
    )
    val testWaveLocationWithNoData =
        WaveLocation(
            id = siteId,
            name = siteName,
            geoLocation = GeoLocation(lat = -13.34F, lon = 45.00F),
            waveDataReadings = emptyList()
        )
}
