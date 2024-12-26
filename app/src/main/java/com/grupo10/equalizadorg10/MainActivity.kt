package com.grupo10.equalizadorg10

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grupo10.equalizadorg10.ui.ProfileScreen
import com.grupo10.equalizadorg10.ui.theme.EqualizadorG10Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EqualizadorApp()
        }
    }
}

@Composable
fun EqualizadorApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main_screen"
    ) {
        composable("main_screen") {
            EqualizadorMainScreen(navController = navController)
        }
        composable("profile_screen/{profileName}") { backStackEntry ->
            val profileName = backStackEntry.arguments?.getString("profileName") ?: "Unknown"
            ProfileScreen(
                profileName,
                navController = navController,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun EqualizadorMainScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    val savedProfileName = sharedPreferences.getString("profile_name", "Default Profile")
    val savedVolume = sharedPreferences.getFloat("volume", 0.5f)
    val savedBass = sharedPreferences.getFloat("bass", 0.5f)
    val savedMiddle = sharedPreferences.getFloat("middle", 0.5f)
    val savedTreble = sharedPreferences.getFloat("treble", 0.5f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.audio_waves),
            contentDescription = "Audio waves icon",
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = "G10",
            fontSize = 55.sp,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(5.dp).align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Equalizador",
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$savedProfileName",
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Volume: ${(savedVolume * 100).toInt()}%", fontSize = 12.sp)
        Text(text = "Bass: ${(savedBass * 100 - 50).toInt()} dB", fontSize = 12.sp)
        Text(text = "Middle: ${(savedMiddle * 100 - 50).toInt()} dB", fontSize = 12.sp)
        Text(text = "Treble: ${(savedTreble * 100 - 50).toInt()} dB", fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Exibe os botões para navegar ou criar novos perfis
        val profiles = listOf("Profile 1", "Profile 2", "Profile 3")

        ProfileListButtons(profiles) { profileName ->
            navController.navigate("profile_screen/$profileName")
        }

        Spacer(modifier = Modifier.height(10.dp))

        FilledTonalButton(onClick = { navController.navigate("profile_screen/New Profile") }) {
            Text(text = "Add new profile")
        }
    }
}


@Composable
fun ProfileListButtons(profiles: List<String>, onProfileClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        profiles.forEach { profile ->
            AccessProfileButton(profile) {
                onProfileClick(profile)
            }
        }
    }
}

@Composable
private fun AccessProfileButton(profileName: String, onClick: () -> Unit) {
    ElevatedButton(onClick = onClick, modifier = Modifier.padding(8.dp)) {
        Text(text = profileName)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEqualizadorMainScreen() {
    EqualizadorG10Theme {
        val dummyNavController = rememberNavController()
        EqualizadorMainScreen(navController = dummyNavController)
    }
}
