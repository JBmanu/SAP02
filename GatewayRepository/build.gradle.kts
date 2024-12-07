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
    // Javalin per costruire l'API Gateway
    implementation("io.javalin:javalin:6.3.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    // OkHttp per fare richieste HTTP come proxy
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")

    // Kotlin standard library
    implementation(kotlin("stdlib"))

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
        archiveBaseName = "EBikeRepository"
        archiveClassifier = ""
        archiveVersion = ""
    }
}