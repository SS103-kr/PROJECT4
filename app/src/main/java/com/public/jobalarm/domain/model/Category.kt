package com.jobalarm.domain.model

data class Category(
    val code: String,
    val name: String,
    val count: Int
)

enum class JobSort { LATEST, DEADLINE, ORG_NAME }
