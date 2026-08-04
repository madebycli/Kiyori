package com.axiel7.anihyou.feature.mediadetails.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.axiel7.anihyou.core.model.character.localized
import com.axiel7.anihyou.core.model.staff.roleLocalized
import com.axiel7.anihyou.core.network.fragment.CommonVoiceActor
import com.axiel7.anihyou.core.network.fragment.MediaCharacter
import com.axiel7.anihyou.core.resources.R as CoreR
import com.axiel7.anihyou.core.ui.common.TabRowItem
import com.axiel7.anihyou.core.ui.composables.ConnectedButtonGroup
import com.axiel7.anihyou.core.ui.composables.person.PERSON_IMAGE_SIZE_SMALL
import com.axiel7.anihyou.core.ui.composables.person.PersonImage
import com.axiel7.anihyou.core.ui.composables.person.PersonItemHorizontalPlaceholder
import com.axiel7.anihyou.core.ui.theme.AniHyouTheme
import com.axiel7.anihyou.feature.mediadetails.MediaDetailsUiState
import com.axiel7.anihyou.feature.mediadetails.R

private enum class PeopleSection {
    CHARACTERS,
    TEAM,
}

private enum class CharacterRoleFilter {
    ALL,
    PRIMARY,
    SECONDARY,
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MediaCharacterStaffView(
    uiState: MediaDetailsUiState,
    fetchData: () -> Unit,
    navigateToCharacterDetails: (Int) -> Unit,
    navigateToStaffDetails: (Int) -> Unit,
    showVoiceActorsSheet: (MediaCharacter) -> Unit,
) {
    val characters = uiState.characters.orEmpty()
    val staff = uiState.staff.orEmpty()
    val isLoading = uiState.characters == null || uiState.staff == null

    var selectedSection by rememberSaveable { mutableIntStateOf(0) }
    var selectedRole by rememberSaveable { mutableIntStateOf(0) }
    var selectedLanguage by rememberSaveable { mutableStateOf<String?>(null) }

    val languages = remember(characters) {
        characters
            .flatMap(MediaCharacter::availableVoiceActors)
            .mapNotNull { it.languageV2 }
            .distinct()
            .sortedWith(
                compareBy<String> { !it.equals("Japanese", ignoreCase = true) }
                    .thenBy { it.lowercase() }
            )
    }

    LaunchedEffect(uiState.characters, uiState.staff) {
        if (uiState.characters == null && uiState.staff == null) fetchData()
    }

    LaunchedEffect(languages) {
        if (selectedLanguage !in languages) {
            selectedLanguage = languages.firstOrNull {
                it.equals("Japanese", ignoreCase = true)
            } ?: languages.firstOrNull()
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        ConnectedButtonGroup(
            items = remember {
                arrayOf(
                    TabRowItem(PeopleSection.CHARACTERS, R.string.media_people_characters),
                    TabRowItem(PeopleSection.TEAM, R.string.media_people_team),
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            selectedIndex = selectedSection,
            onItemSelection = { selectedSection = it },
        )

        when (PeopleSection.entries[selectedSection]) {
            PeopleSection.CHARACTERS -> CharactersSection(
                characters = characters,
                isLoading = isLoading,
                roleFilter = CharacterRoleFilter.entries[selectedRole],
                onRoleFilterChanged = { selectedRole = it.ordinal },
                languages = languages,
                selectedLanguage = selectedLanguage,
                onLanguageChanged = { selectedLanguage = it },
                navigateToCharacterDetails = navigateToCharacterDetails,
                showVoiceActorsSheet = showVoiceActorsSheet,
            )

            PeopleSection.TEAM -> TeamSection(
                uiState = uiState,
                isLoading = isLoading,
                navigateToStaffDetails = navigateToStaffDetails,
            )
        }
    }
}

@Composable
private fun CharactersSection(
    characters: List<MediaCharacter>,
    isLoading: Boolean,
    roleFilter: CharacterRoleFilter,
    onRoleFilterChanged: (CharacterRoleFilter) -> Unit,
    languages: List<String>,
    selectedLanguage: String?,
    onLanguageChanged: (String) -> Unit,
    navigateToCharacterDetails: (Int) -> Unit,
    showVoiceActorsSheet: (MediaCharacter) -> Unit,
) {
    var languageMenuExpanded by remember { mutableStateOf(false) }
    val filteredCharacters = remember(characters, roleFilter) {
        characters.filter { character ->
            when (roleFilter) {
                CharacterRoleFilter.ALL -> true
                CharacterRoleFilter.PRIMARY -> character.role?.name == "MAIN"
                CharacterRoleFilter.SECONDARY -> character.role?.name != "MAIN"
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CharacterRoleFilter.entries.forEach { filter ->
            FilterChip(
                selected = roleFilter == filter,
                onClick = { onRoleFilterChanged(filter) },
                label = {
                    Text(
                        when (filter) {
                            CharacterRoleFilter.ALL -> stringResource(R.string.media_people_all)
                            CharacterRoleFilter.PRIMARY -> stringResource(R.string.media_people_primary)
                            CharacterRoleFilter.SECONDARY -> stringResource(R.string.media_people_secondary)
                        }
                    )
                },
            )
        }

        if (languages.isNotEmpty()) {
            Box {
                OutlinedButton(onClick = { languageMenuExpanded = true }) {
                    Text(selectedLanguage ?: stringResource(R.string.media_people_language))
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(CoreR.drawable.expand_more_24),
                        contentDescription = stringResource(R.string.media_people_language),
                    )
                }
                DropdownMenu(
                    expanded = languageMenuExpanded,
                    onDismissRequest = { languageMenuExpanded = false },
                ) {
                    languages.forEach { language ->
                        DropdownMenuItem(
                            text = { Text(language) },
                            onClick = {
                                onLanguageChanged(language)
                                languageMenuExpanded = false
                            },
                        )
                    }
                }
            }
        }
    }

    when {
        isLoading -> repeat(5) { PersonItemHorizontalPlaceholder() }
        else -> filteredCharacters.forEach { character ->
            CharacterVoiceActorRow(
                character = character,
                selectedLanguage = selectedLanguage,
                navigateToCharacterDetails = navigateToCharacterDetails,
                showVoiceActorsSheet = showVoiceActorsSheet,
            )
        }
    }
}

@Composable
private fun CharacterVoiceActorRow(
    character: MediaCharacter,
    selectedLanguage: String?,
    navigateToCharacterDetails: (Int) -> Unit,
    showVoiceActorsSheet: (MediaCharacter) -> Unit,
) {
    val node = character.node?.commonCharacter
    val voiceActors = character.availableVoiceActors()
    val voiceActor = voiceActors.firstOrNull {
        it.languageV2.equals(selectedLanguage, ignoreCase = true)
    } ?: voiceActors.firstOrNull()
    val characterName = node?.name?.userPreferred.orEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 132.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(enabled = node != null) {
                    node?.id?.let(navigateToCharacterDetails)
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PersonImage(
                url = node?.image?.medium,
                modifier = Modifier.size(PERSON_IMAGE_SIZE_SMALL.dp),
                showShadow = true,
            )
            Column(
                modifier = Modifier.padding(start = 12.dp, end = 8.dp),
            ) {
                Text(
                    text = characterName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
                character.role?.localized()?.let { role ->
                    Text(
                        text = role,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .weight(1f)
                .clickable(enabled = voiceActors.isNotEmpty()) {
                    showVoiceActorsSheet(character)
                },
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = voiceActor?.name?.userPreferred
                        ?: stringResource(R.string.media_people_no_voice_actor),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.End,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
                voiceActor?.languageV2?.let { language ->
                    Text(
                        text = language,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.End,
                    )
                }
            }
            PersonImage(
                url = voiceActor?.image?.medium,
                modifier = Modifier.size(PERSON_IMAGE_SIZE_SMALL.dp),
                showShadow = true,
            )
        }
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
}

@Composable
private fun TeamSection(
    uiState: MediaDetailsUiState,
    isLoading: Boolean,
    navigateToStaffDetails: (Int) -> Unit,
) {
    when {
        isLoading -> repeat(5) { PersonItemHorizontalPlaceholder() }
        else -> uiState.staff.orEmpty().forEach { staff ->
            val node = staff.node?.commonStaff
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 112.dp)
                    .clickable(enabled = node != null) {
                        node?.id?.let(navigateToStaffDetails)
                    }
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PersonImage(
                    url = node?.image?.medium,
                    modifier = Modifier.size(PERSON_IMAGE_SIZE_SMALL.dp),
                    showShadow = true,
                )
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = node?.name?.userPreferred.orEmpty(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = staff.roleLocalized(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.padding(start = 120.dp, end = 16.dp))
        }
    }
}

private fun MediaCharacter.availableVoiceActors(): List<CommonVoiceActor> =
    voiceActors.orEmpty().mapNotNull { it?.commonVoiceActor }

@Preview
@Composable
private fun MediaCharacterStaffViewPreview() {
    AniHyouTheme {
        Surface {
            MediaCharacterStaffView(
                uiState = MediaDetailsUiState(),
                fetchData = {},
                navigateToCharacterDetails = {},
                navigateToStaffDetails = {},
                showVoiceActorsSheet = {},
            )
        }
    }
}
