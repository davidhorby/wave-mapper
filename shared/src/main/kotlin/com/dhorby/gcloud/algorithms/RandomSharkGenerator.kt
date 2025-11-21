package com.dhorby.gcloud.algorithms

import model.GeoLocation
import model.PieceLocation
import model.PieceType

object RandomSharkGenerator {

    fun createFrenzyOfSharks(groupName:String, size:Int, location: GeoLocation): List<Pair<String, PieceLocation>> {
        val sharkList = 0..size
        var latitude = location.lat
        var longitude = location.lon
        var counter = 0
        val sharks = sharkList.map {
            counter++
            if (counter <= (size/2)) {
                latitude += 0.5
            } else {
                latitude -= 0.5
            }
            longitude += 0.3
            val id = "$groupName-$it"
            id to PieceLocation(
                id = id,
                name = Names.sharkName[it] ?: "Shark_$it",
                geoLocation = location.copy(lat  = latitude, lon = longitude),
                pieceType = PieceType.SHARK
            )
        }
        return sharks
    }
}