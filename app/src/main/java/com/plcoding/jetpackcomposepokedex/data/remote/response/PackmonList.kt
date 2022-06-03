package com.plcoding.jetpackcomposepokedex.data.remote.response

data class PackmonList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)