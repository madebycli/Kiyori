package com.axiel7.anihyou.feature.mediadetails.composables

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.Test

class CharacterStaffPolicyTest {
    @Test
    fun `characters are the default-compatible all role set`() {
        assertTrue(matchesCharacterRole("MAIN", CharacterRoleFilter.ALL))
        assertTrue(matchesCharacterRole("SUPPORTING", CharacterRoleFilter.ALL))
        assertTrue(matchesCharacterRole(null, CharacterRoleFilter.ALL))
    }

    @Test
    fun `primary and secondary role filters preserve existing semantics`() {
        assertTrue(matchesCharacterRole("MAIN", CharacterRoleFilter.PRIMARY))
        assertFalse(matchesCharacterRole("SUPPORTING", CharacterRoleFilter.PRIMARY))
        assertFalse(matchesCharacterRole(null, CharacterRoleFilter.PRIMARY))

        assertFalse(matchesCharacterRole("MAIN", CharacterRoleFilter.SECONDARY))
        assertTrue(matchesCharacterRole("SUPPORTING", CharacterRoleFilter.SECONDARY))
        assertTrue(matchesCharacterRole(null, CharacterRoleFilter.SECONDARY))
    }

    @Test
    fun `languages prefer Japanese then sort and deduplicate case-insensitively`() {
        assertEquals(
            listOf("Japanese", "English", "Spanish"),
            normalizedVoiceActorLanguages(
                listOf("Spanish", "english", "Japanese", "English", null, " ")
            ),
        )
    }

    @Test
    fun `selected voice language wins and unavailable language falls back to first`() {
        data class Actor(val name: String, val language: String?)
        val actors = listOf(
            Actor("First", "Japanese"),
            Actor("Second", "English"),
        )

        assertEquals(
            "Second",
            selectVoiceActor(actors, "english", Actor::language)?.name,
        )
        assertEquals(
            "First",
            selectVoiceActor(actors, "German", Actor::language)?.name,
        )
        assertNull(selectVoiceActor(emptyList<Actor>(), "Japanese", Actor::language))
    }
}
