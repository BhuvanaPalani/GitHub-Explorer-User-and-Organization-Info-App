package se.linerotech.module202.project2.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "login") val login: String,
    @Json(name = "name") val name: String?,
    @Json(name = "location") val location: String?,
    @Json(name = "avatar_url") val avatarUrl: String?,
    @Json(name = "bio") val bio: String?,
    @Json(name = "public_repos") val publicRepos: Int?,
    @Json(name = "followers") val followers: Int?,
    @Json(name = "following") val following: Int?,
    @Json(name = "twitter_username") val twitterUsername: String?,
    @Json(name = "blog") val blog: String?,
    @Json(name = "hireable") val hireable: Boolean?,
    @Json(name = "company") val company: String?
)
