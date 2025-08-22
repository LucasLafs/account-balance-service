package com.challenge.accountbalanceservice.testing.factory

import java.io.File

internal object JsonFactory {

    fun jsonMessageRead(path: String) = readText("messages/$path.json")

    private fun readText(fullPath: String) = File(ClassLoader.getSystemResource(fullPath).file).readText()
}
