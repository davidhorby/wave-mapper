package com.dhorby.gcloud.algorithms

import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation

object RandomSharkGenerator {

    fun createFrenzyOfSharks(groupName:String, size:Int, location:GeoLocation): List<Pair<String, PieceLocation>> {
        val sharkList = 0..size
        var latitude = location.lat
        var longitude = location.lon
        val sharks = sharkList.map {
            latitude += 0.5
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