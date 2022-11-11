package com.dhorby.wavemapper.env

import com.dhorby.wavemapper.AppFunctions
import com.dhorby.wavemapper.DataForSiteFunction
import com.dhorby.wavemapper.SiteListFunction

open class FunctionalTestEnv:AppFunctions {
    override val siteListFunction: SiteListFunction = { listOf(TestData.testSiteLocation) }
    override val dataForSiteFunction: DataForSiteFunction = { TestData.testLocation }

    internal val siteListFunctionFake:SiteListFunction = {
        listOf(TestData.testSiteLocation)
    }

    internal val dataForSiteFunctionFake:DataForSiteFunction = {
        TestData.testLocation
    }

}
