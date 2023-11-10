package com.dhorby.gcloud.model

enum class CardinalPoint {
    NORTH, SOUTH, WEST, EAST
}
data class Bearing(val primaryCardinalPoint:CardinalPoint, val angle:Int, val secondaryCardinalPoint:CardinalPoint,)