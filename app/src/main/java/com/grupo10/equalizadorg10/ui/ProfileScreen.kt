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
fun VolumeSlider() {
    var volumeSliderValue by remember { mutableStateOf(0.5f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top, // Alinha os itens no topo
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Volume")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "min")
            Text(text = "max")
        }
        Slider(
            value = volumeSliderValue,
            onValueChange = { newValue -> volumeSliderValue = newValue },
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun FrequenciaSlider(faixa: String) {
    var sliderValue by remember { mutableStateOf(0.5f) }

    Column(
        modifier = Modifier
            .fillMaxWidth() // Garante que cada slider ocupe toda a largura disponível
            .padding(5.dp),
        verticalArrangement = Arrangement.Top, // Alinha os itens no topo
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = faixa)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "-10 dB")
            Text(text = "+10 dB")
        }
        Slider(
            value = sliderValue,
            onValueChange = { newValue -> sliderValue = newValue },
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp)) // Adiciona espaço entre os sliders
    }
}



@Composable
fun ProfileScreen(profileName: String, navController: NavController, modifier: Modifier = Modifier ) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    }
    var editableName by remember { mutableStateOf(profileName) }

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

        Spacer(modifier = Modifier.height(16.dp))
        VolumeSlider()
        FrequenciaSlider(faixa = "Bass")
        FrequenciaSlider(faixa = "Middle")
        FrequenciaSlider(faixa = "Treble")
        Spacer(modifier = Modifier.height(16.dp))

        FilledTonalButton(
            onClick = {

                sharedPreferences.edit()
                    .putString("profile_name", editableName)
                    .apply()
            }
        ) {
            Text(text = "Save changes")
        }

        ElevatedButton(
            onClick = {
                navController.navigate("main_screen") {
                    popUpTo("main_screen") { inclusive = true } // Remove a tela atual da pilha de navegação
                }
            }
        ) {
            Text(text = "Choose other profile")
    }
}}



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




