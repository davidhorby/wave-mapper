package com.dhorby.wavemapper.algorithms

import com.dhorby.wavemapper.algorithms.GeoDistance.kmPerHourToMPerSecond
import com.dhorby.wavemapper.algorithms.GeoDistance.toRadians
import com.dhorby.wavemapper.model.Bearing
import com.dhorby.wavemapper.model.GeoLocation
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.jupiter.api.Test

class GeoDistanceTest {
    @Test
    fun distanceKm() {
        val start = GeoLocation(50.68, -34.10)
        val finish = GeoLocation(49.17, -5.05)
        val distanceKm = GeoDistance.distanceKm(start, finish)
        assertThat(distanceKm, equalTo(2084))
    }

    @Test
    fun `should convert km per hour to metres per second`() {
        assertThat(22.kmPerHourToMPerSecond(), equalTo(6))
        assertThat(8.kmPerHourToMPerSecond(), equalTo(2))
    }

    @Test
    fun `should convert degress to radians`() {
        val bearing = Bearing(45.0)
        val radians = bearing.toRadians()
        val expectedRadians = Math.toRadians(bearing.degrees)
        assertThat(radians, equalTo(expectedRadians))
    }
}
