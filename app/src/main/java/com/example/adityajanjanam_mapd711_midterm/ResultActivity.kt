package com.example.adityajanjanam_mapd711_midterm

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var resultTextView: TextView
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Initialize UI elements
        resultTextView = findViewById(R.id.textViewResult)
        webView = findViewById(R.id.webView)

        // Set WebView Client to handle loading of URLs
        webView.webViewClient = WebViewClient()

        // Retrieve shared preferences data safely
        val sharedPref = getSharedPreferences("BloodSugarConverter", MODE_PRIVATE)
        val name = sharedPref.getString("name", getString(R.string.not_specified)) ?: getString(R.string.not_specified)
        val age = sharedPref.getInt("age", 0)
        val gender = sharedPref.getString("gender", getString(R.string.not_specified)) ?: getString(R.string.not_specified)
        val bloodSugar = sharedPref.getFloat("bloodSugar", 0.0f)
        val exercise = sharedPref.getString("exercise", getString(R.string.none)) ?: getString(R.string.none)
        val loadWebsite = sharedPref.getBoolean("loadWebsite", false)

        // Populate result text
        resultTextView.text = getString(R.string.result_greeting, name) + "\n" +
                getString(R.string.result_gender, gender) + "\n" +
                getString(R.string.result_age, age) + "\n" +
                getString(R.string.result_blood_sugar, bloodSugar) + "\n" +
                getString(R.string.result_exercise, exercise)

        // Load website if checkbox was checked
        if (loadWebsite) {
            loadDiabetesActionWebsite()
        } else {
            webView.loadUrl("") // Clear the WebView
        }
    }

    private fun loadDiabetesActionWebsite() {
        val url = getString(R.string.url_diabetes_action)
        if (url.isNotBlank()) {
            webView.loadUrl(url)
        } else {
            // Handle the case where the URL is not valid
            resultTextView.append("\n" + getString(R.string.url_not_valid))
        }
    }
}
