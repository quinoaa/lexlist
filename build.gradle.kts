plugins {
    id("java")
}

group = "space.quinoaa"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

sourceSets {
    main {
        resources {
            srcDirs("src/resources")
            srcDirs("front/build")
        }
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("io.javalin:javalin:6.7.0")
    implementation("org.xerial:sqlite-jdbc:3.50.3.0")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    implementation("org.slf4j:slf4j-simple:2.0.17")
    // https://mvnrepository.com/artifact/info.picocli/picocli
    implementation("info.picocli:picocli:4.7.7")
    implementation("org.jline:jline:3.30.0")
    implementation("de.mkammerer:argon2-jvm:2.12")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
}

tasks.test {
    useJUnitPlatform()
}