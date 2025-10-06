package se.linerotech.module202.project2.userInfo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val userName: String,
    val fullName: String,
    val country: String,
    val avatarUrl: String,
    val followersCount: Int,
    val followingCount: Int,
    val repositoriesCount: Int,
    val about: String,
    val twitterHandle: String?,
    val websiteUrl: String?,
    val isHireable: Boolean?,
    val companyName: String?,
    val topLanguages: List<String>
) : Parcelable
