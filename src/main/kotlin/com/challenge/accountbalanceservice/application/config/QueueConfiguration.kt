package com.challenge.accountbalanceservice.application.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(
    QueueProperties::class
)
class QueueConfiguration

@ConfigurationProperties(prefix = "app.queue")
data class QueueProperties(
    val financialTransactionProcessed: QueueDefinition
)

data class QueueDefinition(
    val name: String,
    val dlq: QueueDlqDefinition
)

data class QueueDlqDefinition(
    val name: String,
    val reprocess: Boolean = false
)
