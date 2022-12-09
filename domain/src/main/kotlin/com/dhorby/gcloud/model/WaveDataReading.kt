package com.dhorby.gcloud.model

import java.time.LocalDate

data class WaveDataReading(val date: LocalDate, val waveHeight: Float, val windSpeed:Int, val windDirection:String)
