package se.linerotech.module202.project2.userFollowers

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import se.linerotech.module202.project2.R
import se.linerotech.module202.project2.api.FollowerDto
import se.linerotech.module202.project2.userInfo.UserInfoActivity

class FollowersAdapter :
    ListAdapter<FollowerDto, FollowersAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<FollowerDto>() {
            override fun areItemsTheSame(oldItem: FollowerDto, newItem: FollowerDto) =
                oldItem.login == newItem.login

            override fun areContentsTheSame(oldItem: FollowerDto, newItem: FollowerDto) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_follower, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatar: ImageView = itemView.findViewById(R.id.imageAvatar)
        private val login: TextView = itemView.findViewById(R.id.textLogin)

        fun bind(item: FollowerDto) {
            login.text = item.login
            avatar.load(item.avatarUrl) {
                crossfade(true)
                placeholder(R.drawable.person)
                error(R.drawable.person)
            }

            itemView.setOnClickListener {
                val ctx = itemView.context
                val intent = Intent(ctx, UserInfoActivity::class.java)
                    .putExtra("extra_username", item.login)
                ctx.startActivity(intent)
            }
        }
    }
}
