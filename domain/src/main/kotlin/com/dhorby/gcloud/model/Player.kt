package com.dhorby.gcloud.model

data class Player(
    val pieceLocation: PieceLocation,
    val distanceToFinish: Int,
    val distanceFromPirates: List<PirateDistance> = emptyList(),
    val distanceFromSharks: List<SharkDistance> = emptyList()
)