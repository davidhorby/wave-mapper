package com.dhorby.gcloud.model

data class Player(
    val pieceLocation: PieceLocation,
    val distanceToFinish: Int,
    val distanceFromPirates: List<Distance> = emptyList(),
    val distanceFromSharks: List<Distance> = emptyList()
)