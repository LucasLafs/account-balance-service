package com.challenge.accountbalanceservice.testing.extensions
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.slf4j.LoggerFactory

abstract class AbstractEmbeddedExtension : BeforeAllCallback {
    companion object {
        private val LOG = LoggerFactory.getLogger(AbstractEmbeddedExtension::class.java)
    }

    override fun beforeAll(context: ExtensionContext) {
        // We need to use a unique key here, across all usages of this particular extension.
        if (getInstanceInStore(context) == null) {
            // First test container invocation.
            LOG.info("beforeAll-${this.javaClass.name}-${this.hashCode()}")
            context.root.getStore(ExtensionContext.Namespace.GLOBAL).put(getKeyInstanceInStore(), this)
            setup()
            val currentExtension = this
            Runtime.getRuntime().addShutdownHook(
                object : Thread() {
                    override fun run() {
                        currentExtension.close()
                    }
                }
            )
        }
    }

    fun getInstanceInStore(context: ExtensionContext): Any? {
        val uniqueKey = getKeyInstanceInStore()
        return context.root.getStore(ExtensionContext.Namespace.GLOBAL)[uniqueKey]
    }

    private fun getKeyInstanceInStore(): String {
        return this.javaClass.name
    }

    // Callback that is invoked <em>exactly once</em>
    // before the start of <em>all</em> test containers.
    abstract fun setup()

    // Callback that is invoked <em>exactly once</em>
    // after the end of <em>all</em> test containers.
    abstract fun close()
}
