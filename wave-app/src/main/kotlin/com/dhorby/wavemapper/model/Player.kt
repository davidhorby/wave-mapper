package com.dhorby.wavemapper.model

data class Player(
    val pieceLocation: PieceLocation,
    val distanceToFinish: Int,
    val distanceFromPirates: List<Distance> = emptyList(),
    val distanceFromSharks: List<Distance> = emptyList(),
    val moneyChest: MoneyChest = MoneyChest(),
)
