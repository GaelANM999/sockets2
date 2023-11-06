plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation(group = "org.json", name = "json", version = "20231013")

    runtimeOnly ("org.postgresql:postgresql:42.3.1")
}

tasks.test {
    useJUnitPlatform()
}