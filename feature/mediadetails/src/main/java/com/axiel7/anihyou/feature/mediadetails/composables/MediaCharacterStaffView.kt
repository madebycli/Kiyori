package com.axiel7.anihyou.feature.mediadetails.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.axiel7.anihyou.core.ui.composables.person.PersonImage
import com.axiel7.anihyou.core.ui.composables.person.PersonItemHorizontalPlaceholder
import com.axiel7.anihyou.core.ui.theme.AniHyouTheme
import com.axiel7.anihyou.feature.mediadetails.MediaDetailsUiState
import com.axiel7.anihyou.feature.mediadetails.R

private val PEOPLE_IMAGE_SIZE = 56.dp

private enum class PeopleSection {
    CHARACTERS,
    TEAM,
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
    val charactersLoading = uiState.characters == null
    val staffLoading = uiState.staff == null

    var selectedSection by rememberSaveable { mutableIntStateOf(0) }
    var selectedRole by rememberSaveable { mutableIntStateOf(0) }
    var selectedLanguage by rememberSaveable { mutableStateOf<String?>(null) }

    val languages = remember(characters) {
        normalizedVoiceActorLanguages(
            characters
                .flatMap(MediaCharacter::availableVoiceActors)
                .map(CommonVoiceActor::languageV2)
        )
    }

    LaunchedEffect(uiState.characters, uiState.staff) {
        if (charactersLoading || staffLoading) fetchData()
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            selectedIndex = selectedSection,
            onItemSelection = { selectedSection = it },
        )

        when (PeopleSection.entries[selectedSection]) {
            PeopleSection.CHARACTERS -> CharactersSection(
                characters = characters,
                isLoading = charactersLoading,
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
                isLoading = staffLoading,
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
        characters.filter { matchesCharacterRole(it.role?.name, roleFilter) }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                FilterChip(
                    selected = selectedLanguage != null,
                    onClick = { languageMenuExpanded = true },
                    label = {
                        Text(selectedLanguage ?: stringResource(R.string.media_people_language))
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(CoreR.drawable.expand_more_24),
                            contentDescription = stringResource(R.string.media_people_language),
                            modifier = Modifier.size(18.dp),
                        )
                    },
                )
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
        filteredCharacters.isEmpty() -> PeopleEmptyState(R.string.media_people_no_characters)
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
    val node = character.node
    val voiceActors = character.availableVoiceActors()
    val voiceActor = selectVoiceActor(
        voiceActors = voiceActors,
        selectedLanguage = selectedLanguage,
        languageOf = CommonVoiceActor::languageV2,
    )
    val characterName = node?.name?.userPreferred.orEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 88.dp)
            .padding(horizontal = 12.dp, vertical = 8.dp),
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
                modifier = Modifier.size(PEOPLE_IMAGE_SIZE),
                showShadow = true,
            )
            Column(
                modifier = Modifier.padding(start = 10.dp, end = 6.dp),
            ) {
                Text(
                    text = characterName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                character.role?.localized()?.let { role ->
                    Text(
                        text = role,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
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
                    .padding(horizontal = 6.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = voiceActor?.name?.userPreferred
                        ?: stringResource(R.string.media_people_no_voice_actor),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.End,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                voiceActor?.languageV2?.let { language ->
                    Text(
                        text = language,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            PersonImage(
                url = voiceActor?.image?.medium,
                modifier = Modifier.size(PEOPLE_IMAGE_SIZE),
                showShadow = true,
            )
        }
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
}

@Composable
private fun TeamSection(
    uiState: MediaDetailsUiState,
    isLoading: Boolean,
    navigateToStaffDetails: (Int) -> Unit,
) {
    val staffItems = uiState.staff.orEmpty()
    when {
        isLoading -> repeat(5) { PersonItemHorizontalPlaceholder() }
        staffItems.isEmpty() -> PeopleEmptyState(R.string.media_people_no_team)
        else -> staffItems.forEach { staff ->
            val node = staff.node
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp)
                    .clickable(enabled = node != null) {
                        node?.id?.let(navigateToStaffDetails)
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PersonImage(
                    url = node?.image?.medium,
                    modifier = Modifier.size(PEOPLE_IMAGE_SIZE),
                    showShadow = true,
                )
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(
                        text = node?.name?.userPreferred.orEmpty(),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = staff.roleLocalized().orEmpty(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.padding(start = 84.dp, end = 12.dp))
        }
    }
}

@Composable
private fun PeopleEmptyState(messageRes: Int) {
    Text(
        text = stringResource(messageRes),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
    )
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
