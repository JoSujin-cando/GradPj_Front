package com.example.gradfront.data

data class CancelAvailableAmount(
    val total : Int,
    val tax_free : Int,
    val vat : Int,
    val point : Int,
    val discount : Int,
    val green_deposit : Int
)
