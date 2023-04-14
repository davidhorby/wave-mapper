package com.dhorby.gcloud

import DataStoreClient.clearDatastore
import com.google.cloud.functions.HttpFunction
import com.google.cloud.functions.HttpRequest
import com.google.cloud.functions.HttpResponse

class ResetFirestoreFunc : HttpFunction {

    @Throws(Exception::class)
    override fun service(request: HttpRequest, response: HttpResponse) {
        clearDatastore("PieceLocation")
    }

}



