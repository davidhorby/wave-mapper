package com.dhorby.gcloud.data

import com.dhorby.gcloud.model.*
import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation
import java.time.LocalDate

val siteId = "12345"
val siteName = "TestSite"

val sharkId = "Monty"
val sharkSite = "Eastern Atlantic"

object TestData {

    val testSiteLocation = Site(
        id = siteId,
        name = siteName,
        geoLocation = GeoLocation(lat = 12.34, lon = 45.45),
        obsSource = "FM-13 SHIP",
        obsRegion = "West Coast",
        obsLocationType = "Buoy"
    )

    val testSharkLocation = PieceLocation(
        id = "1234",
        name = "Sue",
        geoLocation = GeoLocation(lat = 34.45, lon = 49.01),
        pieceType = PieceType.SHARK
    )

    val testBoatLocation = PieceLocation(
        id = "234ea",
        name = "Albert",
        geoLocation = GeoLocation(lat = 39.45, lon = -5.01),
        pieceType = PieceType.BOAT
    )

    val testPirateLocation = PieceLocation(
        id = "pir123",
        name = "Captain Morgan",
        geoLocation = GeoLocation(lat = 20.45, lon = -15.01),
        pieceType = PieceType.PIRATE
    )

    val validWaveData = WaveDataReading(LocalDate.now(), 2.1F, 23, "SSW")

    val testWaveLocation = WaveLocation(
        id = siteId,
        name = siteName,
        geoLocation = GeoLocation(lat = -13.34, lon = 45.00),
        waveDataReadings = listOf(validWaveData)
    )
    val testWaveLocationWithNoData =
        WaveLocation(
            id = siteId,
            name = siteName,
            geoLocation = GeoLocation(lat = -13.34, lon = 45.00),
            waveDataReadings = emptyList()
        )
}
