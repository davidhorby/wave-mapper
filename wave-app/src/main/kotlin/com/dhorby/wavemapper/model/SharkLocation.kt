package com.dhorby.wavemapper.model

import com.dhorby.gcloud.model.Piece
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation
import java.time.LocalDate

enum class SharkType {
    HAMMERHEAD, GREAT_WHITE, BASKING, TIGER, WHALE
}

data class SharkLocation(
    override val id: String,
    override val name:String,
    override val geoLocation: GeoLocation,
    val date: LocalDate,
    val size: Float,
    val species: SharkType,
    override val pieceType: PieceType = PieceType.SHARK
): Piece, Location
