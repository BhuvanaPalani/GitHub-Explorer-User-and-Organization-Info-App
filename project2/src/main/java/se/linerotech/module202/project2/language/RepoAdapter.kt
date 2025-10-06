package se.linerotech.module202.project2.language

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import se.linerotech.module202.project2.R
import se.linerotech.module202.project2.api.SearchRepoItemDto

class RepoAdapter(
    private val items: MutableList<SearchRepoItemDto> = mutableListOf(),
    private val onClick: (SearchRepoItemDto) -> Unit = {}
) : RecyclerView.Adapter<RepoAdapter.VH>() {

    fun submit(list: List<SearchRepoItemDto>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repo, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position], onClick)
    }

    override fun getItemCount(): Int = items.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageOwner = itemView.findViewById<ImageView>(R.id.imageOwner)
        private val textName = itemView.findViewById<TextView>(R.id.textName)
        private val textDesc = itemView.findViewById<TextView>(R.id.textDesc)
        private val textStars = itemView.findViewById<TextView>(R.id.textStars)
        private val textForks = itemView.findViewById<TextView>(R.id.textForks)
        private val textLang = itemView.findViewById<TextView>(R.id.textLang)

        fun bind(repo: SearchRepoItemDto, onClick: (SearchRepoItemDto) -> Unit) {
            imageOwner.load(repo.owner?.avatarUrl)
            textName.text = repo.fullName ?: repo.name ?: "(no name)"
            textDesc.text = repo.description ?: ""
            textStars.text = "★ ${repo.stars ?: 0}"
            textForks.text = "⑂ ${repo.forks ?: 0}"
            textLang.text = repo.language ?: ""

            // IMPORTANT: pass the data item, not the View
            itemView.setOnClickListener { onClick(repo) }
        }
    }
}
