package se.linerotech.module202.project2.home

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import se.linerotech.module202.project2.databinding.ActivityHomeBinding
import se.linerotech.module202.project2.language.LanguageReposActivity
import se.linerotech.module202.project2.userInfo.UserInfoActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var b: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(b.root)

        val adapter = HomeAdapter(
            sections = viewModel.sections,
            onLanguageClick = { lang ->
                startActivity(
                    Intent(this, LanguageReposActivity::class.java)
                        .putExtra("language", lang)
                )
            },
            onUserClick = { username ->

                viewModel.validateUser(username)
            }
        )
        b.recyclerViewSections.layoutManager = LinearLayoutManager(this)
        b.recyclerViewSections.adapter = adapter

        b.editTextSearchUsername.setOnEditorActionListener { _, actionId, event ->
            val isEnter = actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP)
            if (isEnter) {
                val username = b.editTextSearchUsername.text?.toString()?.trim().orEmpty()
                if (username.isNotEmpty()) viewModel.validateUser(username)
                true
            } else {
                false
            }
        }

        viewModel.state.observe(this) { state ->
            when (state) {
                is HomeUiState.Idle -> Unit
                is HomeUiState.Loading -> {
                    Toast.makeText(this, "Checking…", Toast.LENGTH_SHORT).show()
                }

                is HomeUiState.UserExists -> {
                    startActivity(
                        Intent(this, UserInfoActivity::class.java)
                            .putExtra("username", state.user.login)
                    )
                }

                is HomeUiState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
