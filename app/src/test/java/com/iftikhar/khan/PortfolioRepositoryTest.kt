package com.iftikhar.khan

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests verifying portfolio repository data integrity.
 */
class PortfolioRepositoryTest {

    @Test
    fun `test profile contains valid information`() {
        val profile = PortfolioRepository.profile

        assertTrue("Name should not be blank", profile.name.isNotBlank())
        assertTrue("Role should not be blank", profile.role.isNotBlank())
        assertTrue("About text should not be blank", profile.about.isNotBlank())
        assertTrue("Website URL should start with http or https", profile.websiteUrl.startsWith("http"))
        assertTrue("GitHub URL should start with https", profile.githubUrl.startsWith("https"))
        assertTrue("Email should contain @", profile.contactEmail.contains("@"))
    }

    @Test
    fun `test skills list is non-empty and formatted`() {
        val skills = PortfolioRepository.skills

        assertFalse("Skills list should not be empty", skills.isEmpty())
        assertTrue("Skills count should match repository profile expectation", skills.size >= 5)
        assertTrue("Kotlin should be in technical skills", skills.contains("Kotlin"))
    }

    @Test
    fun `test featured projects have valid package names and categories`() {
        val projects = PortfolioRepository.projects

        assertFalse("Projects list should not be empty", projects.isEmpty())

        projects.forEach { project ->
            assertTrue("Project name should not be blank", project.name.isNotBlank())
            assertTrue("Project description should not be blank", project.description.isNotBlank())
            assertTrue("Project category should not be blank", project.category.isNotBlank())
            assertTrue(
                "Package name should contain valid domain format",
                project.packageName.contains(".")
            )
        }
    }
}
