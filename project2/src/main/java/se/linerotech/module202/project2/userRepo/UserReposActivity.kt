package se.linerotech.module202.project2.userRepo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import se.linerotech.module202.project2.R
import se.linerotech.module202.project2.api.ApiClient
import se.linerotech.module202.project2.api.GitHubApi
import se.linerotech.module202.project2.api.RepoDto

class UserReposActivity : AppCompatActivity() {

    private val api by lazy { ApiClient.retrofit.create(GitHubApi::class.java) }
    private lateinit var adapter: RepoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_repos)

        val username = intent.getStringExtra("username")
        if (username.isNullOrBlank()) {
            finish(); return
        }

        val toolbar =
            findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbarRepos)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        supportActionBar?.title = "Repositories"

        val recycler = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerRepos)
        val progress = findViewById<View>(R.id.progressRepos)
        val textError = findViewById<TextView>(R.id.textErrorRepos)

        adapter = RepoListAdapter(onClick = { repo ->
            repo.htmlUrl?.let { url ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        })
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        progress.visibility = View.VISIBLE
        textError.visibility = View.GONE

        api.getUserRepos(username).enqueue(object : Callback<List<RepoDto>> {
            override fun onResponse(call: Call<List<RepoDto>>, resp: Response<List<RepoDto>>) {
                progress.visibility = View.GONE
                val list = resp.body().orEmpty()
                if (!resp.isSuccessful || list.isEmpty()) {
                    textError.text = "No repositories found."
                    textError.visibility = View.VISIBLE
                } else {
                    adapter.submit(list)
                }
            }

            override fun onFailure(call: Call<List<RepoDto>>, t: Throwable) {
                progress.visibility = View.GONE
                textError.text = "Network error: ${t.localizedMessage ?: "try again"}"
                textError.visibility = View.VISIBLE
            }
        })
    }
}
