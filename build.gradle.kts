buildscript {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        // pin 1.2.41 since the latest version has a bug
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.41")
    }
}

val wrapper by tasks.creating(Wrapper::class) {
    gradleVersion = "4.9"
}

repositories {
    mavenLocal()
    google()
    mavenCentral()
    jcenter()
}

plugins {
    kotlin("jvm") version "1.2.0"
    java
    idea
    `maven-publish`
    `kotlin-dsl`
}

group = "apachebeam-kotlin"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    compile(kotlin("stdlib"))
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.2.41")
    compile("org.jetbrains.kotlin:kotlin-reflect:1.2.41")
    compile("ch.qos.logback:logback-classic:1.2.3")
    compile("org.apache.beam:beam-sdks-java-core:2.6.0")
    compile("org.apache.beam:beam-runners-direct-java:2.6.0")
    compile("org.apache.beam:beam-runners-google-cloud-dataflow-java:2.6.0")

    testCompile("org.jetbrains.kotlin:kotlin-test-junit:1.2.41")
    testCompile("org.hamcrest:hamcrest-all:1.3")
}

val jobClass: Any? by project
val options: Any? by project
val runJob by tasks.creating(JavaExec::class) {
    val opts = options?.let { (it as String).split(Regex("(\n|\\s)")) }?.filter{ it.isNotBlank() }?.map { it.trim() } ?: listOf()
    args = opts
    main = "$jobClass"
    classpath = java.sourceSets["main"].runtimeClasspath

    doFirst {
        println("* main job class  : $jobClass")
        println("* pipeline options: \n${opts.joinToString("\n")}\n")
    }
}

runJob.dependsOn("compileKotlin")
