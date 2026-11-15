package com.iftikhar.khan

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.iftikhar.khan.databinding.ActivityMainBinding
import com.iftikhar.khan.databinding.ItemProjectBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val profile = PortfolioRepository.profile
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()
        loadProfile()
        loadSkills()
        loadProjects()
        setupClickListeners()
    }
    private fun setupWindowInsets() {
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = true
        controller.isAppearanceLightNavigationBars = true
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(view.paddingLeft, systemBars.top, view.paddingRight, systemBars.bottom)
            insets
        }
    }
    private fun loadProfile() {
        binding.profileName.text = profile.name
        binding.profileRole.text = profile.role
        binding.aboutText.text = profile.about
        binding.yearsValue.text = profile.experienceYears
        binding.appsValue.text = profile.publishedAppsCount
        binding.skillsValue.text = getString(
            R.string.stat_skills_value_format,
            PortfolioRepository.skills.size
        )
    }
    private fun loadSkills() {
        val separator = getString(R.string.skills_bullet_separator)
        binding.skillsText.text = PortfolioRepository.skills.joinToString(separator)
    }
    private fun loadProjects() {
        binding.projectsContainer.removeAllViews()
        PortfolioRepository.projects.forEach { project ->
            val projectBinding = ItemProjectBinding.inflate(
                layoutInflater,
                binding.projectsContainer,
                false
            )
            projectBinding.projectTitle.text = project.name
            projectBinding.projectCategory.text = project.category
            projectBinding.projectDescription.text = project.description
            projectBinding.root.setOnClickListener {
                val playUrl = "https://play.google.com/store/apps/details?id=${project.packageName}"
                openUrl(playUrl)
            }
            binding.projectsContainer.addView(projectBinding.root)
        }
    }
    private fun setupClickListeners() {
        binding.btnWebsite.setOnClickListener {
            val intent = Intent(this, WebsiteActivity::class.java).apply {
                putExtra(WebsiteActivity.EXTRA_URL, profile.websiteUrl)
            }
            startActivity(intent)
        }
        binding.btnGithub.setOnClickListener {
            if (profile.githubUrl.contains("https://github.com/iftikharkhan-android-app-developer")) {
                showMessage(getString(R.string.msg_configure_github))
            } else {
                openUrl(profile.githubUrl)
            }
        }
        binding.btnContact.setOnClickListener {
            if (profile.contactEmail.contains("aacontcat03@gmail.com")) {
                showMessage(getString(R.string.msg_configure_email))
                return@setOnClickListener
            }
            try {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:${profile.contactEmail}".toUri()
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        getString(R.string.msg_email_subject)
                    )
                }
                startActivity(emailIntent)
            } catch (_: ActivityNotFoundException) {
                showMessage(getString(R.string.msg_no_email_app))
            }
        }
        binding.btnShare.setOnClickListener {
            val shareText = buildString {
                appendLine(profile.name)
                appendLine(profile.role)
                appendLine()
                appendLine(profile.about)
                appendLine()
                appendLine("Portfolio:")
                appendLine(profile.websiteUrl)
                appendLine()
                appendLine("GitHub:")
                append(profile.githubUrl)
            }
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            startActivity(
                Intent.createChooser(
                    shareIntent,
                    getString(R.string.msg_share_chooser_title)
                )
            )
        }
    }
    private fun openUrl(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
        } catch (_: ActivityNotFoundException) {
            showMessage(getString(R.string.msg_unable_to_open_url))
        }
    }
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
