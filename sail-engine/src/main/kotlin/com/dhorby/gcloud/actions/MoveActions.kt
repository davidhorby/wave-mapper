package com.dhorby.gcloud.actions

import com.dhorby.gcloud.model.PieceLocation
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


fun loadPieceLocation(json: String): PieceLocation {
    return Json.decodeFromString<PieceLocation>(json)
}