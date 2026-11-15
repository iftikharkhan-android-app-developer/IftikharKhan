package com.iftikhar.khan

/**
 * Data model representing the developer's profile details.
 */
data class PortfolioProfile(
    val name: String,
    val role: String,
    val about: String,
    val experienceYears: String,
    val publishedAppsCount: String,
    val websiteUrl: String,
    val githubUrl: String,
    val contactEmail: String
)

/**
 * Data model representing a published or showcase Android app project.
 */
data class PortfolioProject(
    val name: String,
    val description: String,
    val category: String,
    val packageName: String
)

/**
 * Repository supplying portfolio data for the application.
 */
object PortfolioRepository {

    val profile = PortfolioProfile(
        name = "Iftikhar Khan",
        role = "Android Application Engineer",
        about = "Android developer with 2+ years of hands-on experience. " +
                "I build modern native Android applications using Kotlin, Java, " +
                "Firebase, REST APIs and modern UI/UX.",
        experienceYears = "2+",
        publishedAppsCount = "2",
        websiteUrl = "https://iftikhar-khan.vercel.app/",
        githubUrl = "https://github.com/iftikharkhan-android-app-developer",
        contactEmail = "aacontact03@gmail.com"
    )

    val skills = listOf(
        "Kotlin",
        "Java",
        "XML",
        "Firebase",
        "REST APIs",
        "Jetpack Compose",
        "Room Database",
        "OpenCV",
        "ML Kit"
    )

    val projects = listOf(
        PortfolioProject(
            name = "ArCaif AI Photo Editor",
            description = "AI photo editing, background removal, " +
                    "image resizing, object eraser and creative tools.",
            category = "Photography",
            packageName = "com.ai.arcaif.image.edit"
        ),
        PortfolioProject(
            name = "Recover Deleted Messages - Box",
            description = "Notification history, captured message recovery " +
                    "and supported media status saving.",
            category = "Productivity",
            packageName = "com.allmedia.status.recover"
        )
    )
}
