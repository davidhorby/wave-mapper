package com.dhorby.gcloud.external.junit

import com.google.cloud.datastore.Datastore
import com.google.cloud.datastore.testing.LocalDatastoreHelper
import org.junit.jupiter.api.extension.*

class DataStoreExtension : BeforeTestExecutionCallback, BeforeAllCallback, AfterAllCallback, AfterEachCallback, ParameterResolver {

    private val localDatastoreHelper = LocalDatastoreHelper
        .newBuilder()
        .setConsistency(1.0) // If the consistency is not 1.0, the data may not be there yet
        .setStoreOnDisk(false)
        .build()

    override fun beforeTestExecution(context: ExtensionContext) = store(context).put("datastore", localDatastoreHelper.options.service)

    override fun beforeAll(extensionContext: ExtensionContext) {
        localDatastoreHelper.start()
    }

    override fun afterAll(extensionContext: ExtensionContext) {
        localDatastoreHelper.stop()
    }

    override fun afterEach(extensionContext: ExtensionContext) {
        localDatastoreHelper.reset()
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean =
        parameterContext.parameter.type == Datastore::class.java

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any? =
        if (supportsParameter(parameterContext, extensionContext)) store(extensionContext)["datastore"] else null


    private fun store(extensionContext: ExtensionContext) = with(extensionContext) {
        getStore(ExtensionContext.Namespace.create(requiredTestClass.name, requiredTestMethod.name))
    }

}
