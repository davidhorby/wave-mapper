package com.dhorby.wavemapper.env

import com.dhorby.wavemapper.model.DatePeriod
import com.dhorby.wavemapper.model.Location
import com.dhorby.wavemapper.model.Site
import java.time.LocalDate

val siteId = "12345"
val siteName = "TestSite"

internal object TestData {

    val testSiteLocation = Site(
        id = siteId,
        name = siteName,
        latitude = 12.34F,
        longitude = 45.45F,
        obsSource = "FM-13 SHIP",
        obsRegion = "West Coast",
        obsLocationType = "Buoy"
    )

    val validWaveData = DatePeriod(LocalDate.now(), 2.1F, 23, "SSW")


    val testLocation = Location(id= siteId, name=siteName, lat = -13.34F, lon = 45.00F, datePeriods = listOf(validWaveData))
    val testLocationWithNoData = Location(id= siteId, name=siteName, lat = -13.34F, lon = 45.00F, datePeriods = emptyList())
}
