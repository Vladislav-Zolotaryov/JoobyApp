import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("application")
  id("org.jetbrains.kotlin.jvm") version "1.8.20"
  id("io.jooby.run") version "3.0.0.M7"
  id("com.google.osdetector") version "1.7.3"
  id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  group = "app"
  version = "1.0.0"
}

application {
  mainClass.set("app.AppKt")
}

repositories {
  mavenLocal()
  mavenCentral()
}

dependencies {
  implementation(platform("io.jooby:jooby-bom:3.0.0.M7"))
  implementation("io.jooby:jooby-netty")
  implementation("io.jooby:jooby-jackson")
  implementation("io.jooby:jooby-hikari")
  implementation("io.jooby:jooby-jdbi")

  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

  implementation("org.postgresql:postgresql:42.6.0")

  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("ch.qos.logback:logback-classic")

  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("org.junit.jupiter:junit-jupiter-engine")
  testImplementation("io.jooby:jooby-test")
  testImplementation("com.squareup.okhttp3:okhttp")
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile>().all {
  kotlinOptions.javaParameters = true
}

tasks.shadowJar {
  mergeServiceFiles()
}
