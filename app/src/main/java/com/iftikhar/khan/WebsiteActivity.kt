package com.iftikhar.khan

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.iftikhar.khan.databinding.ActivityWebsiteBinding

class WebsiteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebsiteBinding
    private var startUrl = DEFAULT_URL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWebsiteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startUrl = getValidUrl(intent.getStringExtra(EXTRA_URL))
        setupWindowInsets()
        setupToolbar()
        setupWebView()
        setupBrowserControls()
        setupBackNavigation()
        binding.webView.loadUrl(startUrl)
    }
    private fun setupWindowInsets() {
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = true
        controller.isAppearanceLightNavigationBars = true
        val topPadding = resources.getDimensionPixelSize(R.dimen.spacing_tiny)
        val endPadding = resources.getDimensionPixelSize(R.dimen.spacing_small)
        val bottomMarginExtra = resources.getDimensionPixelSize(R.dimen.spacing_medium)
        ViewCompat.setOnApplyWindowInsetsListener(binding.websiteRoot) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.websiteToolbar.setPadding(topPadding, bars.top, endPadding, topPadding)
            val layoutParams = binding.bottomDockCard.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.bottomMargin = bars.bottom + bottomMarginExtra
            binding.bottomDockCard.layoutParams = layoutParams
            insets
        }
    }
    private fun setupToolbar() {
        binding.websiteToolbar.setNavigationOnClickListener {
            finish()
        }
        binding.websiteToolbar.inflateMenu(R.menu.menu_website)
        binding.websiteToolbar.setOnMenuItemClickListener { menuItem: MenuItem ->
            if (menuItem.itemId == R.id.action_open_browser) {
                val currentUrl = binding.webView.url ?: startUrl
                openExternal(currentUrl.toUri())
                true
            } else {
                false
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadsImagesAutomatically = true
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = true
            displayZoomControls = false
            setSupportZoom(true)
            javaScriptCanOpenWindowsAutomatically = false
            setSupportMultipleWindows(false)
            allowFileAccess = false
            allowContentAccess = false
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        }

        binding.webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.webView.visibility = View.VISIBLE
                binding.errorContainer.visibility = View.GONE
                binding.webProgress.progress = 0
                binding.webProgress.visibility = View.VISIBLE

                updateAddress(url)
                updateNavigationButtons()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.webProgress.progress = PROGRESS_COMPLETE
                binding.webProgress.visibility = View.GONE

                updateAddress(url)
                updateNavigationButtons()
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val uri = request?.url ?: return false

                return when (uri.scheme?.lowercase()) {
                    "http", "https" -> false
                    else -> {
                        openExternal(uri)
                        true
                    }
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                if (request?.isForMainFrame == true) {
                    val errorMsg = error?.description?.toString()
                        ?: getString(R.string.network_error_msg)
                    showError(errorMsg)
                }
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                val statusCode = errorResponse?.statusCode ?: 0
                if (request?.isForMainFrame == true && statusCode >= HTTP_SERVER_ERROR_MIN) {
                    showError(getString(R.string.server_error_msg))
                }
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.cancel()
                showError(getString(R.string.ssl_error_msg))
            }
        }

        binding.webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.webProgress.progress = newProgress
                binding.webProgress.visibility =
                    if (newProgress < PROGRESS_COMPLETE) View.VISIBLE else View.GONE
                updateNavigationButtons()
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                if (!title.isNullOrBlank() && !title.startsWith("http")) {
                    binding.websiteToolbar.title = title
                }
            }
        }
    }

    private fun setupBrowserControls() {
        binding.btnWebBack.setOnClickListener {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            }
        }

        binding.btnWebForward.setOnClickListener {
            if (binding.webView.canGoForward()) {
                binding.webView.goForward()
            }
        }

        binding.btnWebRefresh.setOnClickListener {
            val isLoading = binding.webProgress.visibility == View.VISIBLE &&
                    binding.webProgress.progress < PROGRESS_COMPLETE

            if (isLoading) {
                binding.webView.stopLoading()
                binding.webProgress.visibility = View.GONE
            } else {
                binding.webView.reload()
            }
        }

        binding.btnWebShare.setOnClickListener {
            shareWebsite()
        }

        binding.btnRetry.setOnClickListener {
            binding.errorContainer.visibility = View.GONE
            binding.webView.visibility = View.VISIBLE
            binding.webView.loadUrl(binding.webView.url ?: startUrl)
        }

        binding.btnOpenExternal.setOnClickListener {
            val currentUrl = binding.webView.url ?: startUrl
            openExternal(currentUrl.toUri())
        }
    }

    private fun updateNavigationButtons() {
        if (!::binding.isInitialized) return

        val canGoBack = binding.webView.canGoBack()
        val canGoForward = binding.webView.canGoForward()

        binding.btnWebBack.isEnabled = canGoBack
        binding.btnWebBack.isClickable = canGoBack
        binding.btnWebBack.alpha = if (canGoBack) ALPHA_ENABLED else ALPHA_DISABLED

        binding.btnWebForward.isEnabled = canGoForward
        binding.btnWebForward.isClickable = canGoForward
        binding.btnWebForward.alpha = if (canGoForward) ALPHA_ENABLED else ALPHA_DISABLED
    }

    private fun updateAddress(url: String?) {
        if (url.isNullOrBlank()) return

        val uri = url.toUri()
        val host = uri.host
        if (!host.isNullOrBlank()) {
            binding.websiteToolbar.subtitle = host
        }
    }

    private fun showError(message: String) {
        binding.webProgress.visibility = View.GONE
        binding.webView.visibility = View.GONE
        binding.errorContainer.visibility = View.VISIBLE

        binding.errorTitle.text = getString(R.string.error_website_unavailable)
        binding.errorMessage.text = message
    }

    private fun openExternal(uri: Uri) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (e: ActivityNotFoundException) {
            showMessage(getString(R.string.no_browser_found))
        }
    }

    private fun shareWebsite() {
        val currentUrl = binding.webView.url ?: startUrl
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
            putExtra(Intent.EXTRA_TEXT, "Visit my portfolio:\n$currentUrl")
        }

        startActivity(
            Intent.createChooser(
                shareIntent,
                getString(R.string.share_portfolio_title)
            )
        )
    }

    private fun setupBackNavigation() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.webView.canGoBack()) {
                        binding.webView.goBack()
                    } else {
                        finish()
                    }
                }
            }
        )
    }

    private fun getValidUrl(url: String?): String {
        if (url.isNullOrBlank()) return DEFAULT_URL

        val uri = url.toUri()
        val validScheme = uri.scheme == "https" || uri.scheme == "http"

        return if (validScheme && !uri.host.isNullOrBlank()) {
            url
        } else {
            DEFAULT_URL
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        if (::binding.isInitialized) {
            binding.webView.apply {
                stopLoading()
                webChromeClient = null
                webViewClient = WebViewClient()
                destroy()
            }
        }
        super.onDestroy()
    }

    companion object {
        const val EXTRA_URL = "website_url"
        private const val DEFAULT_URL = "https://iftikhar-khan.vercel.app/"
        private const val PROGRESS_COMPLETE = 100
        private const val HTTP_SERVER_ERROR_MIN = 500
        private const val ALPHA_ENABLED = 1.0f
        private const val ALPHA_DISABLED = 0.4f
    }
}
