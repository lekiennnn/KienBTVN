package com.example.kienldmbtvn.data

object AiArtServiceEntry {
    internal var API_KEY = "sk-ePKj7HupzKwrm0BBDpKgbcptFg6zhJL7Fx0ZpfOMzhTa0w2efS"
    internal var APP_NAME = "Tecktrek"
    internal var BUNDLE_ID = "for.techtrek"
    internal var ART_SERVICE_URL = "https://api-img-gen-wrapper.apero.vn"
    internal var ART_STYLE_URL = "https://api-style-manager.apero.vn"
    private var timeDiff: Long = 0L
    internal val timeStamp: Long get() = System.currentTimeMillis() + timeDiff

    fun setTimeStamp(serverTimestamp: Long?) {
        val clientTimestamp = System.currentTimeMillis()
        timeDiff = (serverTimestamp ?: 0) - clientTimestamp
    }

    fun init(apiKey: String, appName: String, bundleId: String) {
        API_KEY = apiKey
        APP_NAME = appName
        BUNDLE_ID = bundleId
    }
}