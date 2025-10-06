package se.linerotech.module202.project2.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import se.linerotech.module202.project2.api.ApiClient
import se.linerotech.module202.project2.api.GitHubApi
import se.linerotech.module202.project2.api.UserDto

class HomeViewModel : ViewModel() {

    private val api: GitHubApi = ApiClient.retrofit.create(GitHubApi::class.java)
    private val _state = MutableLiveData<HomeUiState>(HomeUiState.Idle)
    val state: LiveData<HomeUiState> = _state

    val sections: List<HomeSection> = listOf(
        HomeSection(
            title = "Popular Languages",
            items = listOf("Kotlin", "Python", "Java", "C++", "JavaScript"),
            type = SectionType.LANGUAGES
        ),
        HomeSection(
            title = "Popular Organizations",
            items = listOf("google", "microsoft", "github", "JetBrains", "facebook"),
            type = SectionType.ORGANIZATIONS
        ),
        HomeSection(
            title = "Popular Users",
            items = listOf("torvalds", "gaearon", "JakeWharton", "octocat"),
            type = SectionType.USERS
        )
    )

    fun validateUser(username: String) {
        _state.value = HomeUiState.Loading
        api.getUser(username).enqueue(object : Callback<UserDto> {
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                if (response.isSuccessful && response.body() != null) {
                    _state.value = HomeUiState.UserExists(response.body()!!)
                } else {
                    _state.value = HomeUiState.Error(
                        if (response.code() == 404) {
                            "GitHub user/organization not found"
                        } else {
                            "Failed to retrieve data (${response.code()})"
                        }
                    )
                }
            }

            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                _state.value = HomeUiState.Error("Network error. Please try again.")
            }
        })
    }
}
