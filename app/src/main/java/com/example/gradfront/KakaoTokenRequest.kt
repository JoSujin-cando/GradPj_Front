package com.example.gradfront

data class KakaoTokenRequest(val accessToken: String)

// 로그인 응답 데이터 클래스
data class KakaoLoginResponse(
    val user: User
)

// 사용자 정보 데이터 클래스
data class User(
    val id: Long,
    val email: String,
    val nickname: String,
    val role: String
)