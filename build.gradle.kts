plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.graalvm.buildtools.native") version "0.10.3"
    id("com.microsoft.azure.azurefunctions") version "1.8.0"
}

group = "io.ducommun"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
//    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-reactor-netty")
    implementation("io.netty:netty-common")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
//    implementation("org.springframework.cloud:spring-cloud-function-adapter-azure-web:4.1.3")
//    implementation("org.springframework.amqp:spring-rabbit-stream")
//    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
//    runtimeOnly("org.postgresql:postgresql")
//    runtimeOnly("org.postgresql:r2dbc-postgresql")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.r2dbc:r2dbc-h2")
//    testImplementation("org.springframework.boot:spring-boot-starter-test")
//    testImplementation("org.springframework.boot:spring-boot-testcontainers")
//    testImplementation("io.projectreactor:reactor-test")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
//    testImplementation("org.testcontainers:junit-jupiter")
//    testImplementation("org.testcontainers:postgresql")
//    testImplementation("org.testcontainers:r2dbc")
//    testImplementation("org.testcontainers:rabbitmq")
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

graalvmNative {
    metadataRepository {
        this.enabled = true
    }
    binaries.all {
        resources.autodetect()
//        buildArgs.add("--emit build-report")

//        configurationFileDirectories.from(file("src/main/resources/META-INF/native-image"))
    }
}

azurefunctions {
    subscription = "8ed56184-2c26-4958-8cee-3c8437a115c8"
    resourceGroup = "test-azure-function"
    appName = "testgraal"
    pricingTier = "Consumption'"
    region = "eastus"
    setRuntime(closureOf<com.microsoft.azure.gradle.configuration.GradleRuntimeConfig> {
        os("docker")
        image("testgraal:latest")
    })
//    setAppSettings(closureOf<MutableMap<String, String>> {
//        put("key", "value")
//    })
    setAuth(closureOf<com.microsoft.azure.gradle.auth.GradleAuthConfig> {
        type = "azure_cli"
    })
//    // enable local debug
//    // localDebug = "transport=dt_socket,server=y,suspend=n,address=5005"
    setDeployment(closureOf<com.microsoft.azure.plugin.functions.gradle.configuration.deploy.Deployment> {
        type = "run_from_blob"
    })
}
