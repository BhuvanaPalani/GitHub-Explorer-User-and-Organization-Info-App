// se/linerotech/module202/project2/userFollowing/FollowingActivity.kt
package se.linerotech.module202.project2.userFollowing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import se.linerotech.module202.project2.api.ApiClient
import se.linerotech.module202.project2.api.FollowerDto
import se.linerotech.module202.project2.api.GitHubApi
import se.linerotech.module202.project2.databinding.ActivityFollowingBinding
import se.linerotech.module202.project2.userFollowers.FollowersAdapter

class FollowingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFollowingBinding
    private val api by lazy { ApiClient.retrofit.create(GitHubApi::class.java) }
    private val adapter = FollowersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarFollowing)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarFollowing.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        supportActionBar?.title = "Following"

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username.isNullOrBlank()) {
            Toast.makeText(this, "Missing username", Toast.LENGTH_SHORT).show()
            finish(); return
        }

        binding.recyclerFollowing.layoutManager = LinearLayoutManager(this)
        binding.recyclerFollowing.adapter = adapter

        loadFollowing(username)
    }

    private fun loadFollowing(username: String) {
        api.getUserFollowing(username).enqueue(object : Callback<List<FollowerDto>> {
            override fun onResponse(
                call: Call<List<FollowerDto>>,
                response: Response<List<FollowerDto>>
            ) {
                adapter.submitList(response.body().orEmpty())
            }

            override fun onFailure(call: Call<List<FollowerDto>>, t: Throwable) {
                Toast.makeText(this@FollowingActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {
        private const val EXTRA_USERNAME = "extra_username"

        fun start(ctx: Context, username: String) {
            ctx.startActivity(
                Intent(ctx, FollowingActivity::class.java)
                    .putExtra(EXTRA_USERNAME, username)
            )
        }
    }
}
