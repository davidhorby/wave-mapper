package com.dhorby.wavemapper

import com.dhorby.wavemapper.model.Location
import com.dhorby.wavemapper.model.Site

val siteId = "12345"
val siteName = "TestSite"

object TestData {

    val testSiteLocation = Site(
        id = siteId,
        name = siteName,
        latitude = 12.34F,
        longitude = 45.45F,
        obsSource = "FM-13 SHIP",
        obsRegion = "West Coast",
        obsLocationType = "Buoy"
    )

    val testLocation = Location(id= siteId, name=siteName, lat = -13.34F, lon = 45.00F, datePeriods = emptyList())
}
