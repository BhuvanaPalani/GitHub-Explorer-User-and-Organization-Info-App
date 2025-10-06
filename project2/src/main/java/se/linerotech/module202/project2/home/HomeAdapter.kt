package se.linerotech.module202.project2.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import se.linerotech.module202.project2.R

class HomeAdapter(
    private var sections: List<HomeSection>,
    private val onLanguageClick: (String) -> Unit,
    private val onUserClick: (String) -> Unit
) : RecyclerView.Adapter<HomeAdapter.SectionVH>() {

    class SectionVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.textViewSectionTitle)
        val chips: ChipGroup = itemView.findViewById(R.id.chipGroupSection)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_section_chips, parent, false)
        return SectionVH(view)
    }

    override fun getItemCount(): Int = sections.size

    override fun onBindViewHolder(holder: SectionVH, position: Int) {
        val section = sections[position]
        holder.title.text = section.title
        holder.chips.removeAllViews()
        holder.chips.isSingleLine = true

        val inflater = LayoutInflater.from(holder.itemView.context)
        section.items.forEach { label ->
            val chipView = inflater.inflate(R.layout.item_chip, holder.chips, false)
            val chip = chipView.findViewById<Chip>(R.id.chipItem)
            chip.text = label
            chip.isCheckable = false
            chip.setOnClickListener {
                when (section.type) {
                    SectionType.LANGUAGES -> onLanguageClick(label)
                    SectionType.ORGANIZATIONS, SectionType.USERS -> onUserClick(label)
                }
            }
            holder.chips.addView(chip)
        }
    }

    fun submitSections(newSections: List<HomeSection>) {
        sections = newSections
        notifyDataSetChanged()
    }
}
