package se.linerotech.module202.project2.language

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import se.linerotech.module202.project2.R
import se.linerotech.module202.project2.api.ApiClient
import se.linerotech.module202.project2.api.GitHubApi
import se.linerotech.module202.project2.api.SearchRepoItemDto
import se.linerotech.module202.project2.api.SearchReposResponseDTO

class LanguageReposActivity : AppCompatActivity() {

    private val api by lazy { ApiClient.retrofit.create(GitHubApi::class.java) }
    private lateinit var adapter: RepoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_repos)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbarLanguage)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        val language = intent.getStringExtra("language")
        if (language.isNullOrBlank()) {
            Toast.makeText(this, "Missing language", Toast.LENGTH_SHORT).show()
            finish(); return
        }
        supportActionBar?.title = "$language repositories"

        val recycler = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler)
        val progress = findViewById<View>(R.id.progress)
        val textError = findViewById<TextView>(R.id.textError)

        adapter = RepoAdapter(onClick = { item ->
            item.htmlUrl?.let { url ->
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        })
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        progress.visibility = View.VISIBLE
        textError.visibility = View.GONE

        api.searchReposByLanguage(q = "language:$language").enqueue(object :
                Callback<SearchReposResponseDTO> {
                override fun onResponse(
                    call: Call<SearchReposResponseDTO>,
                    resp: Response<SearchReposResponseDTO>
                ) {
                    progress.visibility = View.GONE
                    val body = resp.body()
                    if (!resp.isSuccessful || body == null) {
                        textError.text = "Failed: HTTP ${resp.code()}"
                        textError.visibility = View.VISIBLE
                        return
                    }

                    val list: List<SearchRepoItemDto> = body.items
                    if (list.isEmpty()) {
                        textError.text = "No $language repositories found."
                        textError.visibility = View.VISIBLE
                    } else {
                        adapter.submit(list)
                    }
                }

                override fun onFailure(call: Call<SearchReposResponseDTO>, t: Throwable) {
                    progress.visibility = View.GONE
                    textError.text = "Network error: ${t.localizedMessage ?: "try again"}"
                    textError.visibility = View.VISIBLE
                }
            })
    }
}
