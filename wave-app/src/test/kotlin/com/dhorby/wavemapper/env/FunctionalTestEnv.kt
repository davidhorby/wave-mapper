package com.dhorby.wavemapper.env

import com.dhorby.gcloud.data.TestData
import com.dhorby.wavemapper.wavemapper.AppFunctions
import com.dhorby.wavemapper.wavemapper.DataForSiteFunction
import com.dhorby.wavemapper.wavemapper.SiteListFunction

open class FunctionalTestEnv : AppFunctions {
    override val siteListFunction: SiteListFunction = { mutableListOf(TestData.testSiteLocation) }
    override val dataForSiteFunction: DataForSiteFunction = { TestData.testWaveLocation }

    internal val siteListFunctionFake: SiteListFunction = {
        mutableListOf(TestData.testSiteLocation)
    }

    internal val dataForSiteFunctionFake: DataForSiteFunction = {
        TestData.testWaveLocation
    }

    internal val dataForSiteWithNoDataFunctionFake: DataForSiteFunction = {
        TestData.testWaveLocationWithNoData
    }
}
