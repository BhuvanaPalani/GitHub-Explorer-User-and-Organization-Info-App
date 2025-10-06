package se.linerotech.module202.project2.userFollowers

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
import se.linerotech.module202.project2.databinding.ActivityFollowersBinding

class FollowersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFollowersBinding
    private val api by lazy { ApiClient.retrofit.create(GitHubApi::class.java) }
    private val adapter = FollowersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarFollowers)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarFollowers.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val username = intent.getStringExtra(EXTRA_USERNAME)
        if (username.isNullOrBlank()) {
            Toast.makeText(this, "Missing username", Toast.LENGTH_SHORT).show()
            finish(); return
        }
        supportActionBar?.title = "Followers"

        binding.recyclerFollowers.layoutManager = LinearLayoutManager(this)
        binding.recyclerFollowers.adapter = adapter

        loadFollowers(username)
    }

    private fun loadFollowers(username: String) {
        api.getUserFollowers(username).enqueue(object : Callback<List<FollowerDto>> {
            override fun onResponse(
                call: Call<List<FollowerDto>>,
                response: Response<List<FollowerDto>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(this@FollowersActivity, "HTTP ${response.code()}", Toast.LENGTH_LONG).show()
                    return
                }
                adapter.submitList(response.body().orEmpty())
            }

            override fun onFailure(call: Call<List<FollowerDto>>, t: Throwable) {
                Toast.makeText(this@FollowersActivity, "Error: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {
        private const val EXTRA_USERNAME = "extra_username"
        fun start(ctx: Context, username: String) {
            ctx.startActivity(Intent(ctx, FollowersActivity::class.java).putExtra(EXTRA_USERNAME, username))
        }
    }
}
