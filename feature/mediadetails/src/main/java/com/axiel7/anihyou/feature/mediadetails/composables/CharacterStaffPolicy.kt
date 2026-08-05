package com.axiel7.anihyou.feature.mediadetails.composables

internal enum class CharacterRoleFilter {
    ALL,
    PRIMARY,
    SECONDARY,
}

internal fun matchesCharacterRole(
    roleName: String?,
    filter: CharacterRoleFilter,
): Boolean = when (filter) {
    CharacterRoleFilter.ALL -> true
    CharacterRoleFilter.PRIMARY -> roleName == "MAIN"
    CharacterRoleFilter.SECONDARY -> roleName != "MAIN"
}

internal fun normalizedVoiceActorLanguages(values: List<String?>): List<String> = values
    .mapNotNull { it?.trim()?.takeIf(String::isNotEmpty) }
    .distinctBy(String::lowercase)
    .sortedWith(
        compareBy<String> { !it.equals("Japanese", ignoreCase = true) }
            .thenBy(String::lowercase)
    )

internal fun <T> selectVoiceActor(
    voiceActors: List<T>,
    selectedLanguage: String?,
    languageOf: (T) -> String?,
): T? = voiceActors.firstOrNull {
    languageOf(it).equals(selectedLanguage, ignoreCase = true)
} ?: voiceActors.firstOrNull()
