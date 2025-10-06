package se.linerotech.module202.project2.api

import com.squareup.moshi.Json

data class RepoDto(
    val name: String?,
    val description: String?,
    val language: String?,
    @Json(name = "updated_at") val updatedAt: String?,
    @Json(name = "html_url") val htmlUrl: String?,
    val license: LicenseDto?,
    @Json(name = "languages_url") val languagesUrl: String? // keep if you had it
)

data class LicenseDto(
    val key: String?,
    val name: String?
)
