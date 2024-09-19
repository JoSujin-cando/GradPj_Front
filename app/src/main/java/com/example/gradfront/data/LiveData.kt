package com.example.gradfront.data

import java.time.LocalDate

data class LiveData(
    val id: Long,
    val title: String,
    val bandLineup: String,
    val date: LocalDate,
    val club_id: String,
    val genre: String,
    val advancePrice: Int,
    val doorPrice: Int,
    val notice: String,
    val timetable: String,
    val image: String // 이미지 경로
)

