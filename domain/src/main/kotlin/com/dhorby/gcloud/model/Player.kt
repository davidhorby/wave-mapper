package com.dhorby.gcloud.model

data class Player(val pieceLocation: PieceLocation, val sharks: List<PieceLocation>, val pirates:List<PieceLocation>)