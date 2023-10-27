package com.dhorby.gcloud.model

data class Player(val pieceLocation: PieceLocation, val distanceToFinish: Int, val distanceFromPirates:Map<PieceLocation, Int> = emptyMap())