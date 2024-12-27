package com.grupo10.equalizadorg10.ui

import ProfileRepository
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.grupo10.equalizadorg10.commom.FrequenciaSlider
import com.grupo10.equalizadorg10.commom.VolumeSlider
import com.grupo10.equalizadorg10.data.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProfileCreationScreen(
    navController: NavController,
    onProfileCreated: (Profile) -> Unit,
    modifier: Modifier = Modifier
) {
    var newProfileName by remember { mutableStateOf("") }
    var newProfileVolume by remember { mutableStateOf(0.5f) }
    var newProfileBass by remember { mutableStateOf(0.5f) }
    var newProfileMiddle by remember { mutableStateOf(0.5f) }
    var newProfileTreble by remember { mutableStateOf(0.5f) }

    val context = LocalContext.current
    val profileRepository = ProfileRepository(context)


    val createProfile: () -> Unit = {
        val newProfile = Profile(
            name = newProfileName,
            volume = newProfileVolume,
            bass = newProfileBass,
            middle = newProfileMiddle,
            treble = newProfileTreble
        )

        CoroutineScope(Dispatchers.Main).launch {
            profileRepository.insert(newProfile)
            onProfileCreated(newProfile)
            navController.popBackStack()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = newProfileName,
            onValueChange = { newProfileName = it },
            label = { Text("Profile Name") }
        )


        VolumeSlider(
            value = newProfileVolume,
            onValueChange = { newProfileVolume= it },
            label = "Volume"
        )

        FrequenciaSlider(
            value = newProfileBass,
            onValueChange = { newProfileBass= it},
            label = "Bass",
        )

        FrequenciaSlider(
            value = newProfileMiddle,
            onValueChange = { newProfileMiddle=it },
            label = "Middle",
        )

        FrequenciaSlider(
            value = newProfileTreble,
            onValueChange = { newProfileTreble = it},
            label = "Treble",
        )


        Button(onClick = createProfile) {
            Text("Create Profile")
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewProfileCreationScreen() {
    val dummyNavController = rememberNavController()
    ProfileCreationScreen(
        navController = dummyNavController,
        onProfileCreated = {}
    )
}
