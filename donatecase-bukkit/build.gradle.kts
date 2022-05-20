import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

defaultTasks("clean", "build")

repositories {
    maven ("https://repo.codemc.org/repository/maven-public")
    //Vault
    maven ("https://jitpack.io")
    //PaperMC
    maven ("https://papermc.io/repo/repository/maven-public/")
    mavenCentral()
}

configurations {
    compileClasspath.get().extendsFrom(create("shadeOnly"))
}

dependencies {
    //MariaDB for DataBase
    implementation("org.mariadb.jdbc:mariadb-java-client:3.0.4")
    //HikariCP
    implementation("com.zaxxer:HikariCP:5.0.1")
    //Vault
    implementation("com.github.MilkBowl:VaultApi:1.7")
    implementation("org.jetbrains:annotations:23.0.0")
    //Paper 1.13
    compileOnly("com.destroystokyo.paper:paper-api:1.13.2-R0.1-SNAPSHOT")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = rootProject.group as String?
            artifactId = "wgrp"
            version = rootProject.version as String?

            from(components["java"])
        }
    }

    repositories {
        val mavenUrl: String? by project
        val mavenSnapshotUrl: String? by project

        (if(rootProject.version.toString().endsWith("SNAPSHOT")) mavenSnapshotUrl else mavenUrl)?.let { url ->
            maven(url) {
                val mavenUsername: String? by project
                val mavenPassword: String? by project
                if(mavenUsername != null && mavenPassword != null) {
                    credentials {
                        username = mavenUsername
                        password = mavenPassword
                    }
                }
            }
        }
    }
}

tasks {
    runServer {
        minecraftVersion("1.16.5")
    }
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.named<Copy>("processResources") {
    val internalVersion = project.version
    inputs.property("internalVersion", internalVersion)
    filesMatching("plugin.yml") {
        expand("internalVersion" to internalVersion)
    }
}

tasks.named<Jar>("jar") {
    val projectVersion = project.version
    inputs.property("projectVersion", projectVersion)
    manifest {
        attributes("Implementation-Version" to projectVersion)
    }
}

tasks.named<ShadowJar>("shadowJar") {
    configurations = listOf(project.configurations["shadeOnly"], project.configurations["runtimeClasspath"])
    archiveFileName.set("${project.name}-${project.version}-SNAPSHOT.${archiveExtension.getOrElse("jar")}")

    dependencies {
        include(dependency(":wgrp-api"))
    }
}

tasks.withType<Jar>() {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    configurations["compileClasspath"].forEach { file: File ->
        if(file.name.contains("HikariCP"))
            from(zipTree(file.absoluteFile))
    }
    configurations["compileClasspath"].forEach { file: File ->
        if(file.name.contains("mariadb-java-client"))
            from(zipTree(file.absoluteFile))
    }
}

tasks.named("assemble").configure {
    dependsOn("shadowJar")
}
