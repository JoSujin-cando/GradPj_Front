package com.example.gradfront.data

data class BookingRequest(
    val userId: Long,
    val liveId: Int,
    val numberOfTickets: Int
)