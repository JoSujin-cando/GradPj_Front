package com.example.gradfront.data

data class LivePerformanceResponse(
    val id: Long,
    val title: String,
    val bandLineup: String,
    val date: String, // 또는 LocalDate로 파싱할 수 있습니다.
    val club_id: String,
    val genre: String,
    val advancePrice: Int,
    val doorPrice: Int,
    val notice: String,
    val timetable: String,
    val image: String
)

