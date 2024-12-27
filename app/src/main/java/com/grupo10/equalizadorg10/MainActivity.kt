package com.grupo10.equalizadorg10

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.grupo10.equalizadorg10.data.AppDatabase
import com.grupo10.equalizadorg10.data.Profile
import com.grupo10.equalizadorg10.data.ProfileDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.grupo10.equalizadorg10.ui.ProfileCreationScreen
import com.grupo10.equalizadorg10.ui.ProfileEditScreen

class MainActivity : ComponentActivity() {

    private lateinit var profileRepository: ProfileRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        profileRepository = ProfileRepository(applicationContext)

        setContent {
            EqualizadorApp(profileRepository)
        }
    }
}

class ProfileRepository(private val context: Context) {
    private val profileDao: ProfileDao = AppDatabase.getDatabase(context).profileDao()

    suspend fun getAllProfiles(): List<Profile> {
        val profiles = profileDao.getAllProfiles()
        if (profiles.isEmpty()) {
            val defaultProfile = Profile(
                name = "Default",
                volume = 0.5f,
                bass = 0.5f,
                middle = 0.5f,
                treble = 0.5f
            )
            profileDao.insert(defaultProfile)
        }
        return profileDao.getAllProfiles()
    }

    suspend fun update(profile: Profile) {
        profileDao.update(profile)
    }

    suspend fun getLastUsedProfile(): Profile? {
        return profileDao.getLastUsedProfile()
    }

    suspend fun insert(profile: Profile) {
        profileDao.insert(profile)
    }

    suspend fun deleteAllProfiles() {
        profileDao.deleteAllProfiles()
    }
}

@Composable
fun EqualizadorApp(profileRepository: ProfileRepository) {
    val navController = rememberNavController()
    var profiles by remember { mutableStateOf(listOf<Profile>()) }
    var currentProfile by remember { mutableStateOf<Profile?>(null) }


    LaunchedEffect(Unit) {
        profiles = profileRepository.getAllProfiles()
        currentProfile = profileRepository.getLastUsedProfile() ?: profiles.firstOrNull()
    }

    if (profiles.isEmpty()) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        NavHost(navController = navController, startDestination = "main_screen") {
            composable("main_screen") {
                EqualizadorMainScreen(
                    navController = navController,
                    profiles = profiles,  // Passando a lista de perfis
                    onProfileUpdated = { updatedProfile ->
                        CoroutineScope(Dispatchers.Main).launch {
                            profileRepository.update(updatedProfile)
                            profiles = profiles.map {
                                if (it.id == updatedProfile.id) updatedProfile else it
                            }
                            currentProfile = updatedProfile
                        }
                    },
                    profileRepository = profileRepository
                )
            }

            composable("profile_screen/{profileId}") { backStackEntry ->
                val profileId = backStackEntry.arguments?.getString("profileId")?.toInt() ?: 0
                val profile = profiles.firstOrNull { it.id == profileId }
                    ?: Profile(
                        id = 0, name = "Unknown", volume = 0.5f, bass = 0.5f, middle = 0.5f, treble = 0.5f
                    )

                ProfileEditScreen(
                    navController = navController,
                    profile = profile,
                    onProfileUpdated = { updatedProfile ->
                        CoroutineScope(Dispatchers.Main).launch {
                            profileRepository.update(updatedProfile)
                            profiles = profiles.map {
                                if (it.id == updatedProfile.id) updatedProfile else it
                            }
                            navController.popBackStack()
                        }
                    }
                )
            }

            composable("profile_screen/new_profile") {
                ProfileCreationScreen(
                    navController = navController,
                    onProfileCreated = { newProfile ->
                        CoroutineScope(Dispatchers.Main).launch {
                            profileRepository.insert(newProfile)
                            profiles = profiles + newProfile
                            navController.navigate("main_screen")
                        }
                    }
                )
            }
        }
    }
}




@Composable
fun EqualizadorMainScreen(
    navController: NavController,
    profiles: List<Profile>,
    onProfileUpdated: (Profile) -> Unit,
    profileRepository: ProfileRepository,
    modifier: Modifier = Modifier
) {
    var profileList by remember { mutableStateOf(profiles) }
    var lastProfile by remember { mutableStateOf<Profile?>(null) }

    LaunchedEffect(profiles) {
        profileList = profiles  // Atualiza a lista de perfis quando o estado mudar
        lastProfile = profileRepository.getLastUsedProfile()
    }

    val currentProfile = lastProfile ?: profileList.firstOrNull()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Image(
                painter = painterResource(R.drawable.audio_waves),
                contentDescription = "Audio waves icon",
                modifier = Modifier.size(100.dp)
            )
        }
        item {
            Text(
                text = "G10",
                fontSize = 55.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(5.dp)
            )
        }
        item {
            Text(
                text = "Equalizador",
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif
            )
        }

        item {
            ProfileListButtons(profiles = profileList) { selectedProfile ->
                onProfileUpdated(selectedProfile)
                navController.navigate("profile_screen/${selectedProfile.id}")
            }
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            FilledTonalButton(onClick = { navController.navigate("profile_screen/new_profile") }) {
                Text(text = "Add new profile")
            }
        }

//        item {
//            FilledTonalButton(onClick = {
//                CoroutineScope(Dispatchers.Main).launch {
//                    profileRepository.deleteAllProfiles()
//                    profileList = profileRepository.getAllProfiles()
//                }
//            }) {
//                Text(text = "Clear All Profiles")
//            }
//        }
    }
}



@Composable
fun ProfileListButtons(profiles: List<Profile>, onProfileClick: (Profile) -> Unit) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        profiles.forEach { profile ->
            AccessProfileButton(profile = profile) {
                onProfileClick(profile)
            }
        }
    }
}

@Composable
fun AccessProfileButton(profile: Profile, onClick: () -> Unit) {
    ElevatedButton(onClick = onClick, modifier = Modifier.padding(8.dp)) {
        Text(text = profile.name)
    }
}
