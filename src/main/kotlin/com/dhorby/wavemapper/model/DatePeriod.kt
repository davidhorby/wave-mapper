package com.dhorby.wavemapper.model

import java.time.LocalDate

data class DatePeriod(val date: LocalDate, val waveHeight: Float, val windSpeed:Int, val windDirection:String)
