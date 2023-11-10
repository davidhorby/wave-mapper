package com.dhorby.gcloud.model

data class Distance(val name: String, val distance: Int) {
    val alertLevel: String =
        when {
            distance < 1000 -> "red"
            distance in 1001..1999 -> "orange"
            else -> "lightgreen"
        }
}
