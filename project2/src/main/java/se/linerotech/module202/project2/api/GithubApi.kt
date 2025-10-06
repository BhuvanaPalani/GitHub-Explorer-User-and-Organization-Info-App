package se.linerotech.module202.project2.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("users/{username}")
    fun getUser(@Path("username") username: String): Call<UserDto>

    @GET("users/{user}/repos")
    fun getUserRepos(
        @Path("user") user: String,
        @Query("per_page") perPage: Int = 100,
        @Query("type") type: String = "owner",
        @Query("sort") sort: String = "updated"
    ): Call<List<RepoDto>>

    @GET("search/repositories")
    fun searchReposByLanguage(
        @Query("q") q: String,
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = 50
    ): Call<SearchReposResponseDTO>

    @GET("users/{username}/followers")
    fun getUserFollowers(@Path("username") username: String): Call<List<FollowerDto>>

    @GET("users/{username}/following")
    fun getUserFollowing(@Path("username") username: String): Call<List<FollowerDto>>
}
