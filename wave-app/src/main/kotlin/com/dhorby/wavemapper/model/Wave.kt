package com.dhorby.wavemapper.model

import org.http4k.template.ViewModel

data class Wave(val location:String, val height:Long):ViewModel
