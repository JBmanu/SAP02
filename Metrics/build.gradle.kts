import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass.set("Launcher")
}

dependencies {
    implementation("io.prometheus:simpleclient_bom:0.16.0")
    implementation("io.prometheus:simpleclient_httpserver:0.16.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<ShadowJar>() {

}

tasks {
    shadowJar {
        archiveBaseName = "metrics"
        archiveClassifier = ""
        archiveVersion = ""
    }
}