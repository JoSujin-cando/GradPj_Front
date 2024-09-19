package com.example.gradfront.data

data class BookingRequest(
    val userEmail: String,
    val liveId: Int,
    val numberOfTickets: Int
)