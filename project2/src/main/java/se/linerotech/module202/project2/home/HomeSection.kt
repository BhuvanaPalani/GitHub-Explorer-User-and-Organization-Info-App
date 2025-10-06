package se.linerotech.module202.project2.home

data class HomeSection(
    val title: String,
    val items: List<String>,
    val type: SectionType
)

enum class SectionType { LANGUAGES, ORGANIZATIONS, USERS }
