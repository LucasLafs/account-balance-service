package com.challenge.accountbalanceservice

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.junit.jupiter.api.Test

class PackagePatternTest {

    private val basePackage = "com.challenge.accountbalanceservice"
    private val importedClassesApplication = ClassFileImporter().importPackages("$basePackage.application")
    private val importedClassesDomain = ClassFileImporter().importPackages("$basePackage.domain")
    private val importedClassesResources = ClassFileImporter().importPackages("$basePackage.resources")

    @Test
    fun `classes that ends with Controller should be in a controllers package inside web`() =
        ArchRuleDefinition.classes().that().haveNameMatching(".*Controller").should()
            .resideInAPackage("..web..").check(importedClassesApplication)

    @Test
    fun `classes that ends with Config should be in a controllers package inside web`() =
        ArchRuleDefinition.classes().that().haveNameMatching(".*Configuration").should()
            .resideInAPackage("..config..").check(importedClassesApplication)

    @Test
    fun `classes that ends with Service should be in a services package`() =
        ArchRuleDefinition.classes().that().haveNameMatching(".*Service").should()
            .resideInAPackage("..services..").check(importedClassesDomain)

    @Test
    fun `classes that ends with Exception should be in a exceptions package`() =
        ArchRuleDefinition.classes().that().haveNameMatching(".*Exception").should()
            .resideInAPackage("..exceptions..")
            .allowEmptyShould(true)
            .let {
                it.check(importedClassesApplication)
                it.check(importedClassesDomain)
                it.check(importedClassesResources)
            }

    @Test
    fun `classes that ends with Gateway should be in a gateway package`() =
        ArchRuleDefinition.classes().that().haveNameMatching(".*Gateway").should()
            .resideInAPackage("..gateways..").check(importedClassesDomain)

    @Test
    fun `classes that ends with Repository or Jpa cannot exists in domain package`() {
        ArchRuleDefinition.classes().that().haveSimpleNameEndingWith("Repository").or()
            .haveSimpleNameEndingWith("Jpa").should().resideOutsideOfPackage("..domain..")
    }
}
