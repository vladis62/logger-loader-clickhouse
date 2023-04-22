import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation(group = "ru.yandex.clickhouse", name = "clickhouse-jdbc", version = "0.3.1")
	implementation(group = "org.springframework.kafka", name = "spring-kafka")
	implementation(group = "com.zaxxer", name = "HikariCP")
	implementation(group = "com.fasterxml.jackson.core", name = "jackson-core")
	implementation(group = "com.fasterxml.jackson.dataformat", name = "jackson-dataformat-cbor")
	implementation(group = "org.apache.commons", name = "commons-lang3")
	implementation(group = "org.apache.commons", name = "commons-compress", version = "1.21")
	implementation(group = "commons-io", name = "commons-io", version = "2.11.0")
	implementation(group = "com.google.guava", name = "guava", version = "30.1.1-jre")
	implementation(group = "org.springframework.boot", name = "spring-boot-starter-web")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
