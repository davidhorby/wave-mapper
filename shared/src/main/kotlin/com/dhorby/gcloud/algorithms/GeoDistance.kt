package com.dhorby.gcloud.algorithms

import model.Bearing
import model.GeoLocation
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

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

    fun Int.kmPerHourToMPerSecond(): Int = this * 1000 / (60 * 60)
    fun Bearing.toRadians():Double = this.degrees * Math.PI / 180

    fun calculateNewCoordinates(currentLocation: GeoLocation, bearing: Bearing, speedKMPerHour: Int): GeoLocation {

        val bearingRadians = bearing.toRadians()

        val metresPerSecond = speedKMPerHour.kmPerHourToMPerSecond()

        val deltaLat = metresPerSecond * cos(bearingRadians)
        val deltaLon = metresPerSecond * sin(bearingRadians)

        return currentLocation.let {
            GeoLocation(it.lat - deltaLat, it.lon - deltaLon)
        }
    }
}
