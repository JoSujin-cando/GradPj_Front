package com.example.gradfront.data

import java.sql.Time

data class LiveData(
    val id: Long,
    val title: String,
    val bandLineup: String,
    val date: String,
    val clubId: Long,
    val startTime: Time,
    val genre: String,
    val advancePrice: Int,
    val doorPrice: Int,
    val notice: String,
    val timetable: String,
    val image: String // 이미지 경로
)

