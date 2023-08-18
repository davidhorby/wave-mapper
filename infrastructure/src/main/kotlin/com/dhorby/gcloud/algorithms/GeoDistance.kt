package com.dhorby.gcloud.algorithms

import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation
import kotlin.math.abs
import kotlin.math.roundToInt

object GeoDistance {

    fun distanceKm(
        start: GeoLocation,
        finish: GeoLocation
    ): Int {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.

        // Haversine formula
        val dLon = Math.toRadians(finish.lon) - Math.toRadians(start.lon)
        val dLat = Math.toRadians(finish.lat) - Math.toRadians(start.lat)
        val a = (Math.pow(Math.sin(dLat / 2), 2.0)
                + (Math.cos(start.lat) * Math.cos(finish.lat)
                * Math.pow(Math.sin(dLon / 2), 2.0)))
        val c = 2 * Math.asin(Math.sqrt(abs(a)))

        // Radius of earth in kilometers. Use 3956
        // for miles.
        val r = 6371.0

        // calculate the result
        val distance = (c * r).roundToInt()
        return distance

    }
}
