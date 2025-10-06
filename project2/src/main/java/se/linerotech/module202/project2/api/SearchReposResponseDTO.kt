package se.linerotech.module202.project2.api

import com.squareup.moshi.Json

data class SearchReposResponseDTO(
    @Json(name = "total_count") val totalCount: Int?,
    val items: List<SearchRepoItemDto> = emptyList()
)

// each repository in that search result
data class SearchRepoItemDto(
    @Json(name = "full_name") val fullName: String?,
    val name: String?,
    val description: String?,
    @Json(name = "stargazers_count") val stars: Int?,
    @Json(name = "forks_count") val forks: Int?,
    val language: String?,
    @Json(name = "html_url") val htmlUrl: String?,
    val owner: OwnerDto?
) {
    data class OwnerDto(
        val login: String?,
        @Json(name = "avatar_url") val avatarUrl: String?
    )
}
