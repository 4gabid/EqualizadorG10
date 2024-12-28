package com.grupo10.equalizadorg10.ui


import ProfileRepository
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.grupo10.equalizadorg10.commom.FrequenciaSlider
import com.grupo10.equalizadorg10.commom.VolumeSlider
import com.grupo10.equalizadorg10.data.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProfileEditScreen(
    navController: NavController,
    profile: Profile,
    onProfileUpdated: (Profile) -> Unit,
    modifier: Modifier = Modifier
) {
    var updatedProfile by remember { mutableStateOf(profile) }

    val context = LocalContext.current
    val profileRepository = ProfileRepository(context)

    val saveProfileChanges: () -> Unit = {
        CoroutineScope(Dispatchers.Main).launch {

            profileRepository.update(updatedProfile)

            profileRepository.setLastUsedProfile(updatedProfile.id)

            onProfileUpdated(updatedProfile)

            //navController.popBackStack()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }
        TextField(
            value = updatedProfile.name,
            onValueChange = { updatedProfile = updatedProfile.copy(name = it) },
            label = { Text("Profile Name") }
        )

        VolumeSlider(
            value = updatedProfile.volume,
            onValueChange = { updatedProfile = updatedProfile.copy(volume = it) },
            label = "Volume"
        )

        FrequenciaSlider(
            value = updatedProfile.bass,
            onValueChange = { updatedProfile = updatedProfile.copy(bass = it) },
            label = "Bass",
        )

        FrequenciaSlider(
            value = updatedProfile.middle,
            onValueChange = { updatedProfile = updatedProfile.copy(middle = it) },
            label = "Middle",
        )

        FrequenciaSlider(
            value = updatedProfile.treble,
            onValueChange = { updatedProfile = updatedProfile.copy(treble = it) },
            label = "Treble",
        )

        Button(onClick = saveProfileChanges) {
            Text("Save Changes")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileEditScreen() {
    val dummyNavController = rememberNavController()

    val profile = Profile(
        name = "Default",
        volume = 0.5f,
        bass = 0.5f,
        middle = 0.5f,
        treble = 0.5f
    )

    ProfileEditScreen(
        navController = dummyNavController,
        profile = profile,
        modifier = Modifier,
        onProfileUpdated = { updatedProfile ->
            println("Perfil atualizado para: ${updatedProfile.name}")
        },
    )
}
