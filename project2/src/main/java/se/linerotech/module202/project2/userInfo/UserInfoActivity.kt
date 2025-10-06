package se.linerotech.module202.project2.userInfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import coil.transform.CircleCropTransformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import se.linerotech.module202.project2.api.ApiClient
import se.linerotech.module202.project2.api.GitHubApi
import se.linerotech.module202.project2.api.RepoDto
import se.linerotech.module202.project2.api.UserDto
import se.linerotech.module202.project2.databinding.ActivityUserInfoBinding
import se.linerotech.module202.project2.userFollowers.FollowersActivity
import se.linerotech.module202.project2.userFollowing.FollowingActivity
import se.linerotech.module202.project2.userRepo.UserReposActivity

class UserInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserInfoBinding
    private val api by lazy { ApiClient.retrofit.create(GitHubApi::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarUserInfo)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarUserInfo.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val username = intent.getStringExtra("extra_username")
            ?: intent.getStringExtra("username")

        if (username.isNullOrBlank()) {
            Toast.makeText(this, "Missing username", Toast.LENGTH_SHORT).show()
            finish(); return
        }
        supportActionBar?.title = username

        binding.textViewFullName.text = "Loading…"
        binding.textViewCountry.text = ""
        binding.textViewAboutBody.text = ""
        setLangText(null, null, null)

        binding.containerRepos.setOnClickListener {
            val intent = Intent(this, UserReposActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        binding.containerFollowers.setOnClickListener {
            val intent = Intent(this, FollowersActivity::class.java)
            intent.putExtra("extra_username", username)
            startActivity(intent)
        }
        binding.containerFollowing.setOnClickListener {
            val intent = Intent(this, FollowingActivity::class.java)
            intent.putExtra("extra_username", username)
            startActivity(intent)
        }

        loadUser(username)
    }

    private fun loadUser(username: String) {
        api.getUser(username).enqueue(object : Callback<UserDto> {
            override fun onResponse(call: Call<UserDto>, resp: Response<UserDto>) {
                val user = resp.body()
                if (!resp.isSuccessful || user == null) {
                    val msg = if (resp.code() == 404) "User not found" else "HTTP ${resp.code()}"
                    showError(msg); return
                }

                binding.imageViewAvatar.load(user.avatarUrl) {
                    crossfade(true)
                    transformations(CircleCropTransformation())
                }
                binding.textViewFullName.text = user.name ?: "(no name)"
                binding.textViewCountry.text = user.location.orEmpty()

                binding.textViewFollowersValue.text = (user.followers ?: 0).toString()
                binding.textViewFollowingValue.text = (user.following ?: 0).toString()
                binding.textViewReposValue.text = (user.publicRepos ?: 0).toString()

                binding.textViewAboutBody.text = user.bio ?: "Not available"

                val twitter = user.twitterUsername?.trim()?.takeIf { it.isNotEmpty() }
                binding.textViewTwitterValue.text = twitter?.let { "@$it" } ?: "Not available"

                val site = user.blog?.trim()?.takeIf { it.isNotEmpty() }
                binding.textViewWebsiteValue.text = site ?: "Not available"
                binding.textViewWebsiteValue.isEnabled = site != null
                binding.textViewWebsiteValue.setOnClickListener {
                    site?.let { url -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
                }

                binding.textViewHireableValue.text = when (user.hireable) {
                    true -> "✅ Hireable"
                    false -> "❌ Not hireable"
                    null -> "Not available"
                }
                binding.textViewCompanyValue.text = user.company ?: "Not available"

                loadTopLanguages(username)
            }

            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                showError(t.localizedMessage ?: "Network error")
            }
        })
    }

    private fun loadTopLanguages(username: String) {
        api.getUserRepos(username).enqueue(object : Callback<List<RepoDto>> {
            override fun onResponse(
                call: Call<List<RepoDto>>,
                resp: Response<List<RepoDto>>
            ) {
                val repos = resp.body().orEmpty()
                if (!resp.isSuccessful || repos.isEmpty()) {
                    setLangText(null, null, null); return
                }

                val counts = mutableMapOf<String, Int>()
                for (r in repos) {
                    val lang = r.language?.trim()
                    if (!lang.isNullOrEmpty()) {
                        counts[lang] = (counts[lang] ?: 0) + 1
                    }
                }

                val top = counts.entries
                    .sortedByDescending { it.value }
                    .take(3)
                    .map { it.key }

                setLangText(top.getOrNull(0), top.getOrNull(1), top.getOrNull(2))
            }

            override fun onFailure(call: Call<List<RepoDto>>, t: Throwable) {
                setLangText(null, null, null)
            }
        })
    }

    private fun setLangText(l1: String?, l2: String?, l3: String?) {
        binding.textViewLang1.text = l1 ?: ""
        binding.textViewLang2.text = l2 ?: ""
        binding.textViewLang3.text = l3 ?: ""
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        binding.textViewFullName.text = "Not available"
        binding.textViewCountry.text = ""
        binding.textViewAboutBody.text = message
        binding.textViewFollowersValue.text = "0"
        binding.textViewFollowingValue.text = "0"
        binding.textViewReposValue.text = "0"
        setLangText(null, null, null)
    }
}
