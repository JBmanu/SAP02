plugins {
    id("java")
}

group = "org.example"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("io.cucumber:cucumber-java:7.20.1")
    testImplementation("io.cucumber:cucumber-junit:7.20.1")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.20.1")

    implementation("org.springframework:spring-web:6.2.0")
    implementation("com.google.code.gson:gson:2.11.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}