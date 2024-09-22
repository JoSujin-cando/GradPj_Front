package com.example.gradfront.data

import java.time.LocalDateTime

data class BookingResponse(
    val id: Long,               // 예매 ID
    val userId: Long,           // 유저 ID
    val liveId: Long,           // 공연 ID
    val liveTitle: String,      // 공연 제목
    val bookingDate: String,  // 예매한 날짜
    val numberOfTickets: Int,   // 예매한 티켓 수
    val totalAmount: Int,       // 예매 가격
    val userName: String,       // 유저 이름
    val userEmail: String,       // 유저 이메일
    val status: BookingStatus
)
