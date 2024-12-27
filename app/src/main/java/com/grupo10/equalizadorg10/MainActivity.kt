package com.grupo10.equalizadorg10

import ProfileRepository
import android.content.Context
import android.os.Bundle
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.platform.LocalContext
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
import com.grupo10.equalizadorg10.ui.ProfileCreationScreen
import com.grupo10.equalizadorg10.ui.ProfileEditScreen
import com.grupo10.equalizadorg10.ui.theme.EqualizadorG10Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            insert(defaultProfile)  // Cria o perfil default
        }
        return profileDao.getAllProfiles()
    }

    suspend fun insert(profile: Profile) {
        profileDao.insert(profile)
    }

    suspend fun update(profile: Profile) {
        profileDao.update(profile)
    }

    suspend fun getLastUsedProfile(): Profile? {
        return profileDao.getLastUsedProfile()
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun EqualizadorApp(profileRepository: ProfileRepository) {
    val navController = rememberNavController()
    var profiles by remember { mutableStateOf(listOf<Profile>()) }
    var currentProfile by remember { mutableStateOf<Profile?>(null) }

    LaunchedEffect(Unit) {
        if (profileRepository.getAllProfiles().isEmpty()) {
            val defaultProfile = Profile(
                name = "Default Profile",
                volume = 0.5f,
                bass = 0.5f,
                middle = 0.5f,
                treble = 0.5f,
                isLastUsed = true
            )
            profileRepository.insert(defaultProfile)
        }

        profiles = profileRepository.getAllProfiles()
        currentProfile = profileRepository.getLastUsedProfile() ?: profiles.firstOrNull()
    }

    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            EqualizadorMainScreen(
                navController = navController,
                profiles = profiles.map { it.name },
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
                        profiles = profileRepository.getAllProfiles() // Atualiza a lista de perfis
                        navController.navigate("main_screen")
                    }
                }
            )
        }
    }
}

@Composable
fun EqualizadorMainScreen(
    navController: NavController,
    profiles: List<String>,
    onProfileUpdated: (Profile) -> Unit,
    profileRepository: ProfileRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var profileList by remember { mutableStateOf(listOf<Profile>()) }

    LaunchedEffect(Unit) {
        profileList = profileRepository.getAllProfiles()
    }

    val currentProfile = profileList.firstOrNull { it.isLastUsed }


    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp), // Spacing between items
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
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
            )
        }
        item {
            Text(
                text = currentProfile?.name ?: "No profile selected",
                fontSize = 15.sp,
                modifier = Modifier
            )
        }

        currentProfile?.let {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Volume: ${(it.volume * 100).toInt()}%", fontSize = 12.sp)
                Text(text = "Bass: ${(it.bass * 100 - 50).toInt()} dB", fontSize = 12.sp)
                Text(text = "Middle: ${(it.middle * 100 - 50).toInt()} dB", fontSize = 12.sp)
                Text(text = "Treble: ${(it.treble * 100 - 50).toInt()} dB", fontSize = 12.sp)
            }
        }

        // Display profile list buttons
        item {
            ProfileListButtons(profiles = profileList) { selectedProfile ->
                onProfileUpdated(selectedProfile)
                navController.navigate("profile_screen/${selectedProfile.id}")
            }
        }

        // Add a new profile button at the bottom
        item {
            Spacer(modifier = Modifier.height(10.dp))
            FilledTonalButton(onClick = { navController.navigate("profile_screen/new_profile") }) {
                Text(text = "Add new profile")
            }
        }
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



