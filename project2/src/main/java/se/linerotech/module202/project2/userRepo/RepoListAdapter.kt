package se.linerotech.module202.project2.userRepo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import se.linerotech.module202.project2.R
import se.linerotech.module202.project2.api.RepoDto

class RepoListAdapter(
    private val items: MutableList<RepoDto> = mutableListOf(),
    private val onClick: (RepoDto) -> Unit = {}
) : RecyclerView.Adapter<RepoListAdapter.VH>() {

    fun submit(list: List<RepoDto>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_repo, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position], onClick)
    }

    override fun getItemCount(): Int = items.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.findViewById<TextView>(R.id.textRepoName)
        private val desc = itemView.findViewById<TextView>(R.id.textRepoDesc)
        private val lang = itemView.findViewById<TextView>(R.id.textRepoLang)
        private val date = itemView.findViewById<TextView>(R.id.textRepoDate)
        private val lic = itemView.findViewById<TextView>(R.id.textRepoLicense)

        fun bind(repo: RepoDto, onClick: (RepoDto) -> Unit) {
            name.text = repo.name ?: "(no name)"
            desc.text = repo.description ?: ""
            lang.text = repo.language ?: ""
            lic.text = repo.license?.name ?: ""
            date.text = repo.updatedAt?.take(10) ?: ""
            itemView.setOnClickListener { onClick(repo) }
        }
    }
}
