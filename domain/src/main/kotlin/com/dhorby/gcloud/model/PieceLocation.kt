package com.dhorby.gcloud.model

import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation


data class PieceLocation(
    override val id: String,
    override val name: String,
    override val pieceType: PieceType,
    override val geoLocation: GeoLocation
):Piece, Location


