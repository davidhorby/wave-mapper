package com.dhorby.wavemapper.model

import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.model.Piece
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation
import java.time.LocalDate

enum class BoatType {
    SAIL, TANKER, DINGY
}

data class BoatLocation(
    override val id: String,
    override val name:String,
    override val geoLocation: GeoLocation,
    val date: LocalDate,
    val size: Float,
    val boatType: BoatType,
    override val pieceType: PieceType = PieceType.BOAT
):Piece, Location
