package com.challenge.accountbalanceservice

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import org.junit.jupiter.api.Test

class LayeredPatternTest {

    companion object {
        const val WEB = "Web"
        const val APP = "App"
        const val CONFIG = "Config"
        const val DOMAIN = "Domain"
        const val RESOURCE = "Resource"
    }

    private val classes = ClassFileImporter()
        .withImportOption { location ->
            location.contains("com/challenge/accountbalanceservice")
        }
        .importPackages("com.challenge.accountbalanceservice")

    @Test
    fun `layered architecture test`() {
        layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer(APP).definedBy("..application..")
            .layer(WEB).definedBy("..application.web..")
            .layer(CONFIG).definedBy("..application.config..")
            .layer(DOMAIN).definedBy("..domain..")
            .layer(RESOURCE).definedBy("..resources..")
            .whereLayer(WEB).mayOnlyBeAccessedByLayers(APP)
            .whereLayer(CONFIG).mayOnlyBeAccessedByLayers(APP, WEB, DOMAIN, RESOURCE)
            .whereLayer(DOMAIN).mayOnlyBeAccessedByLayers(APP, RESOURCE)
            .whereLayer(RESOURCE).mayOnlyBeAccessedByLayers(DOMAIN)
            .check(classes)
    }
}
