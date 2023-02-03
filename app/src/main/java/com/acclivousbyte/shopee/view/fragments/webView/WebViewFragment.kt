package com.acclivousbyte.shopee.view.fragments.webView

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.acclivousbyte.shopee.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {

    private lateinit var binding: FragmentWebViewBinding
    private val args: WebViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebViewBinding.inflate(inflater, container, false)

        loadWebUrl()

        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebUrl() {
        binding.fragmentWebViewWebView.run {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.fragmentWebViewProgressBar.visibility = View.GONE
                }
            }
            loadUrl(args.adsUrl)
        }
    }


}