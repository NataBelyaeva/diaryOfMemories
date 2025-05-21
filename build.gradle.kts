// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.9.3" apply false
}

// Чтение ключа из local.properties
val mapkitApiKey by lazy {
    val properties = java.util.Properties().apply {
        rootProject.file("local.properties").inputStream().use { load(it) }
    }
    properties.getProperty("MAPKIT_API_KEY") ?: throw GradleException(
        "Добавьте MAPKIT_API_KEY в local.properties"
    )
}

// Передача ключа во все модули
allprojects {
    extra["mapkitApiKey"] = mapkitApiKey
}