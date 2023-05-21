package com.example.myapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.myapp.databinding.ActivitySearchBinding
import java.io.IOException

class SearchActivity : AppCompatActivity() {
    val binding by lazy { ActivitySearchBinding.inflate(layoutInflater)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.webView.settings.javaScriptEnabled = true // 자바 스크립트 구문을 허용
        //자바스크립트 인터페이스 클래스 만들기
        binding.webView.addJavascriptInterface(BridgeInterface(), "Android")
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                //안드로이드 -> 자바스크립트 함수 호출
                binding.webView.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }
        //최초 웹뷰 로드
        binding.webView.loadUrl("https://st-iise-capstonedesign2023.web.app/")
    }

    private inner class BridgeInterface {
        @JavascriptInterface
        fun processDATA(data: String) {
            // 카카오 주소 검색 API의 결과 값이 브릿지 통로를 통해 전달 받음 (JavaScript에서)
            val intent = Intent()
            intent.putExtra("data", data)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }


}