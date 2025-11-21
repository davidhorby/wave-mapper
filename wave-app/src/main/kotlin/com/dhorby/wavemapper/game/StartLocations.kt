package com.dhorby.wavemapper.game

import com.dhorby.wavemapper.model.GeoLocation
import com.dhorby.wavemapper.model.PieceLocation
import com.dhorby.wavemapper.model.PieceType

val startLocation =
    PieceLocation(
        id = "start",
        name = "Newport",
        geoLocation = GeoLocation(lat = 41.29, lon = -71.19),
        pieceType = PieceType.START,
    )
val finishLocation =
    PieceLocation(
        id = "finish",
        name = "Lisbon",
        geoLocation = GeoLocation(lat = 38.41, lon = -9.09),
        pieceType = PieceType.START,
    )
val testSharkLocation =
    PieceLocation(
        id = "1234",
        name = "Sue",
        geoLocation = GeoLocation(lat = 34.45, lon = -49.01),
        pieceType = PieceType.SHARK,
    )

val testBoatLocation =
    PieceLocation(
        id = "234ea",
        name = "Albert",
        geoLocation = GeoLocation(lat = 39.45, lon = -5.01),
        pieceType = PieceType.BOAT,
    )

val testPirateLocation =
    PieceLocation(
        id = "pir123",
        name = "Captain Morgan",
        geoLocation = GeoLocation(lat = 20.45, lon = -15.01),
        pieceType = PieceType.PIRATE,
    )
