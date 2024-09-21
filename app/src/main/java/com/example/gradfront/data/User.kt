package com.example.gradfront.data

// 사용자 정보 데이터 클래스
data class User(
    val id: Long,
    val kakaoId: Long,
    val username: String,
    val role: String
)
