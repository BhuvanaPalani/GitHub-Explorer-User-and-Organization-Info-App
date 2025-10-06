package se.linerotech.module202.project2.api

import com.squareup.moshi.Json

data class FollowerDto(
    @Json(name = "login") val login: String,
    @Json(name = "avatar_url") val avatarUrl: String?
)
