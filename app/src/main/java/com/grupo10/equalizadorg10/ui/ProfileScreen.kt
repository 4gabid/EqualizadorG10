package com.grupo10.equalizadorg10.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo10.equalizadorg10.R
import com.grupo10.equalizadorg10.ui.theme.EqualizadorG10Theme

import androidx.compose.material3.*
import android.content.Context
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.grupo10.equalizadorg10.EqualizadorMainScreen

@Composable
fun ProfileScreen(profileName: String, navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    }
    var editableName by remember { mutableStateOf(profileName) }

    // Sliders
    var volumeSliderValue by remember { mutableStateOf(sharedPreferences.getFloat("volume", 0.5f)) }
    var bassSliderValue by remember { mutableStateOf(sharedPreferences.getFloat("bass", 0.5f)) }
    var middleSliderValue by remember { mutableStateOf(sharedPreferences.getFloat("middle", 0.5f)) }
    var trebleSliderValue by remember { mutableStateOf(sharedPreferences.getFloat("treble", 0.5f)) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Equalizador")
        TextField(
            value = editableName,
            onValueChange = { editableName = it },
            label = { Text("Profile Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Volume Slider
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Volume")
            Slider(
                value = volumeSliderValue,
                onValueChange = { volumeSliderValue = it },
                valueRange = 0f..1f,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Bass Slider
        FrequenciaSlider("Bass", bassSliderValue) { bassSliderValue = it }

        // Middle Slider
        FrequenciaSlider("Middle", middleSliderValue) { middleSliderValue = it }

        // Treble Slider
        FrequenciaSlider("Treble", trebleSliderValue) { trebleSliderValue = it }

        Spacer(modifier = Modifier.height(16.dp))

        // Save changes button
        FilledTonalButton(
            onClick = {
                sharedPreferences.edit()
                    .putString("profile_name", editableName)
                    .putFloat("volume", volumeSliderValue)
                    .putFloat("bass", bassSliderValue)
                    .putFloat("middle", middleSliderValue)
                    .putFloat("treble", trebleSliderValue)
                    .apply()
            }
        ) {
            Text(text = "Save changes")
        }

        // Back to main screen button
        ElevatedButton(
            onClick = {
                navController.navigate("main_screen") {
                    popUpTo("main_screen") { inclusive = true }
                }
            }
        ) {
            Text(text = "Choose another profile")
        }
    }
}

@Composable
fun FrequenciaSlider(faixa: String, value: Float, onValueChange: (Float) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = faixa)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewOpenProfileScreen() {
    EqualizadorG10Theme {
        val dummyNavController = rememberNavController()
        ProfileScreen(
            profileName = "Profile 1",
            navController = dummyNavController,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )

    }
}




