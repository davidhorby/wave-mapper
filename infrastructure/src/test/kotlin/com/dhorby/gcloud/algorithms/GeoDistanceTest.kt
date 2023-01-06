package com.dhorby.gcloud.algorithms

import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation
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
}