plugins {
    kotlin("plugin.jpa") version "1.9.23"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"

    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"

    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
    id("org.openapi.generator") version "7.12.0"
    id("info.solidsoft.pitest") version "1.15.0"

    jacoco
}

group = "com.challenge"
version = "0.0.1-SNAPSHOT"
extra["springCloudVersion"] = "2024.0.1"

val basePackage = "com.challenge.accountbalanceservice"
val basePath = "com/challenge/accountbalanceservice"
val restAssuredVersion = "5.1.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.test {
    dependsOn("detekt")
    finalizedBy("jacocoTestCoverageVerification", "archTest", "integrationTest")
}

tasks.withType<Test> {
    jvmArgs("--add-opens", "java.base/java.time=ALL-UNNAMED")
}

// Open Api Gen
tasks.loadKtlintReporters { dependsOn("openApiGenerate") }
tasks.compileKotlin { dependsOn("openApiGenerate") }
openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$rootDir/src/main/resources/static/api-docs.yaml")
    outputDir.set("${layout.buildDirectory.get()}/generated/openapi")
    modelNameSuffix.set("Dto")
    configOptions.set(
        mapOf(
            "dateLibrary" to "java8",
            "gradleBuildFile" to "false",
            "basePackage" to "$basePackage.application.web.api",
            "apiPackage" to "$basePackage.application.web.api",
            "modelPackage" to "$basePackage.application.web.dto",
            "interfaceOnly" to "true",
            "hideGenerationTimestamp" to "true",
            "openApiNullable" to "false",
            "useTags" to "true",
            "useSpringBoot3" to "true",
            "delegatePattern" to "true"
        )
    )
}
sourceSets { getByName("main") { java { srcDir("${layout.buildDirectory.get()}/generated/openapi/src/main/java") } } }

// Detekt
tasks.detekt { dependsOn("ktlintCheck") }
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html {
            required.set(true)
            outputLocation.set(file("build/reports/detekt/detekt.html"))
        }
    }
}

detekt {
    source.setFrom(files("src/main"))
    config.setFrom(files("detekt-config.yml"))
    buildUponDefaultConfig = false
    autoCorrect = true
}

// Integration Test
sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
    }
}
val integrationTestTask = tasks.register("integrationTest", Test::class) {
    description = "Runs the component tests."
    group = "verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
}
val integrationTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
    extendsFrom(configurations.testImplementation.get())
}
configurations["integrationTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

// Arch Test
sourceSets {
    create("archTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val archTest = tasks.register("archTest", Test::class) {
    description = "Runs the architecture tests."
    group = "verification"

    testClassesDirs = sourceSets["archTest"].output.classesDirs
    classpath = sourceSets["archTest"].runtimeClasspath
}

val archTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
    extendsFrom(configurations.testImplementation.get())
}

configurations["archTestRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

// Jacoco
jacoco { toolVersion = "0.8.11" }
val excludePackages: Iterable<String> = listOf(
    "**/$basePath/AccountBalanceApplication*",
    "**/$basePath/application/config/**",
    "**/$basePath/application/web/api/**",
    "**/$basePath/application/web/dto/**",
    "**/$basePath/application/exception/**",
    "**/$basePath/application/web/extensions/**",
    "**/$basePath/domain/gateways/**",
    "**/$basePath/domain/entities/**",
    "**/$basePath/domain/exceptions/**",
    "**/$basePath/resources/repositories/tables/**"
)
extra["excludePackages"] = excludePackages

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude(excludePackages)
        }
    )
}
tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.jacocoTestReport)
    violationRules {
        rule {
            limit {
                minimum = 0.8.toBigDecimal()
                counter = "LINE"
            }
        }
    }
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude(excludePackages)
        }
    )
}

dependencies {
    // Kotlin adapters
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Web
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-jetty")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")

    // OpenFeign
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")

    // OpenApi
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.6.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    compileOnly("io.swagger:swagger-annotations:1.6.14")

    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.9.10")
    implementation("org.flywaydb:flyway-core:9.0.4")
    runtimeOnly("org.postgresql:postgresql")

    // Observability
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // Resilience
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")

    // SQS
    implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs:3.1.0")

    // Cache
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")

    // Unit Test
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Unit Test utils
    testImplementation("io.mockk:mockk:1.12.4")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("net.bytebuddy:byte-buddy:1.14.18")
    testImplementation("net.bytebuddy:byte-buddy-agent:1.14.18")

    // Arch Test
    archTestImplementation("com.tngtech.archunit:archunit-junit5-api:1.2.1")
    archTestImplementation("com.tngtech.archunit:archunit-junit5-engine:1.2.1")

    // Integration Test
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    // Integration Test utils
    integrationTestImplementation("org.awaitility:awaitility-kotlin:4.2.0")

    // SQS Integration Test
    integrationTestImplementation("org.elasticmq:elasticmq-rest-sqs_2.13:1.6.11")
    integrationTestImplementation("software.amazon.awssdk:sqs")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-activemq")

    // HTTP Integration Test
    integrationTestImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")
    integrationTestImplementation("io.rest-assured:rest-assured:$restAssuredVersion")
    integrationTestImplementation("io.rest-assured:json-path:$restAssuredVersion")
    integrationTestImplementation("io.rest-assured:xml-path:$restAssuredVersion")
    integrationTestImplementation("io.rest-assured:kotlin-extensions:$restAssuredVersion")

    // Database Integration Test
    integrationTestImplementation("io.zonky.test:embedded-postgres:2.1.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

pitest {
    junit5PluginVersion.set("1.1.2")
    avoidCallsTo.set(setOf("kotlin.jvm.internal"))
    mutators.set(setOf("Stronger"))
    threads.set(Runtime.getRuntime().availableProcessors())
    outputFormats.set(setOf("XML", "HTML"))
}

springBoot { buildInfo() }
