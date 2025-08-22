package com.challenge.accountbalanceservice.testing.extensions.postgres

import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.SQLException

class PostgresDatabaseCleaner(private val connectionProvider: () -> Connection) {
    private var tablesForClearing: List<TableRef>? = null

    private val tablesToExclude = mutableSetOf<String>()

    fun excludeTable(vararg tableName: String): PostgresDatabaseCleaner {
        tablesToExclude += tableName
        return this
    }

    fun reset() {
        if (notPrepared) {
            prepare()
        }
        executeReset()
    }

    private val notPrepared get() = tablesForClearing == null

    private fun prepare() {
        connectionProvider().use { connection ->
            val metaData = connection.metaData
            val tableRefs = metaData.getTables(connection.catalog, null, null, arrayOf("TABLE")).use { tables ->
                iterator(tables::next) { tables.getString("TABLE_NAME") }
                    .asSequence()
                    .filterNot(tablesToExclude::contains)
                    .map(PostgresDatabaseCleaner::TableRef)
                    .toList()
            }

            tablesForClearing = tableRefs

            LOG.info("Prepared clean db command: {}", tablesForClearing)
        }
    }

    private fun executeReset() {
        try {
            connectionProvider().use { connection ->
                tablesForClearing?.forEach { ref ->
                    connection.prepareStatement("alter table ${ref.name} disable trigger all").execute()
                    connection.prepareStatement("DELETE FROM ${ref.name}").execute()
                    connection.prepareStatement("alter table ${ref.name} enable trigger all").execute()
                }
            }
        } catch (e: SQLException) {
            LOG.error("Failed to remove rows because {}.")
            throw e
        }
    }

    data class TableRef(val name: String)

    companion object {
        private val LOG = LoggerFactory.getLogger(PostgresDatabaseCleaner::class.java)!!
    }

    private inline fun <T> iterator(
        crossinline next: () -> Boolean,
        crossinline value: () -> T
    ): AbstractIterator<out T> =
        object : AbstractIterator<T>() {
            override fun computeNext() {
                if (next()) {
                    setNext(value())
                } else {
                    done()
                }
            }
        }
}
