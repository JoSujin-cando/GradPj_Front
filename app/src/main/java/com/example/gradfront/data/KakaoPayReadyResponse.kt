package com.example.gradfront.data

data class KakaoPayReadyResponse(
    val tid: String,
    val next_redirect_mobile_url: String?,
    val next_redirect_pc_url: String?,
    val android_app_scheme: String,
    val created_at: String
)
