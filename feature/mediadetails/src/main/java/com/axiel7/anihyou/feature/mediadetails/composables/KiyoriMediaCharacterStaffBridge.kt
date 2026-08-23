package com.axiel7.anihyou.feature.mediadetails.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.axiel7.anihyou.core.network.fragment.MediaCharacter
import com.axiel7.anihyou.core.resources.R as CoreR
import com.axiel7.anihyou.core.ui.common.LocalNavActionManager
import com.axiel7.anihyou.feature.mediadetails.MediaDetailsUiState
import com.axiel7.anihyou.feature.mediadetails.R

/** Bridges upstream MediaDetails into Kiyori's richer People UI. */
@Composable
fun MediaCharacterStaffView(
    uiState: MediaDetailsUiState,
    fetchData: () -> Unit,
    showVoiceActorsSheet: (MediaCharacter) -> Unit,
) {
    val navActionManager = LocalNavActionManager.current

    Column(modifier = Modifier.fillMaxWidth()) {
        MediaCharacterStaffView(
            uiState = uiState,
            fetchData = fetchData,
            navigateToCharacterDetails = navActionManager::toCharacterDetails,
            navigateToStaffDetails = navActionManager::toStaffDetails,
            showVoiceActorsSheet = showVoiceActorsSheet,
        )

        uiState.details?.id?.let { mediaId ->
            TextButton(
                onClick = { navActionManager.toMediaCharacters(mediaId) },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 12.dp),
            ) {
                Text(stringResource(R.string.media_people_all_characters))
                Icon(
                    painter = painterResource(CoreR.drawable.arrow_forward_20),
                    contentDescription = null,
                )
            }
        }
    }
}
