import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "me.cobeine"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
    implementation("com.github.Cobeine:SQLava:1.5.6-SNAPSHOT")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("com.github.Mqzn:Lotus:1.1.7")
}
tasks.shadowJar {
    archiveFileName.set("SimpleRegions-${project.version}.jar")
    destinationDirectory.set(file("D:\\minecraft-dev\\servers\\paper-1.20\\plugins"))

    exclude("org/checkerframework/")
    exclude("META-INF/**")
}
apply(plugin = "java")
apply(plugin = "com.github.johnrengelman.shadow")