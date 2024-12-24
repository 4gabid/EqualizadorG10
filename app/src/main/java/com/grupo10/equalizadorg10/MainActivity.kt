package com.grupo10.equalizadorg10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo10.equalizadorg10.ui.theme.EqualizadorG10Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EqualizadorG10Theme {

                    EqualizadorApp(

                    )

                }
            }

    }
}


@Composable
fun EqualizadorMainScreen(modifier: Modifier = Modifier) {

    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally){
        Image(
            painter = painterResource(R.drawable.audio_waves),
            contentDescription = "Audio waves icon"
        )


        Text(
            text = "G10",
            fontSize = 55.sp,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier
                .padding(5.dp)
                .align(alignment = Alignment.CenterHorizontally)

        )

        Text(
            text = "Equalizador",
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif,
            /* TODO: change Color*/
            modifier = Modifier

                .align(alignment = Alignment.CenterHorizontally)

        )
        val profiles = listOf("Profile 1", "Profile 2", "Profile 3")

        Spacer(modifier = Modifier.height(16.dp))

        ProfileListButtons(profiles)

        Spacer(modifier = Modifier.height(10.dp))

        AddProfileButton()

    }

}

@Composable
fun ProfileListButtons(profiles: List<String>) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        profiles.forEach { profile ->
            AccessProfileButton(profile)
        }
    }
}

@Composable
private fun AccessProfileButton( profileName: String) {
    ElevatedButton(onClick = {/* TODO: implement profile screen */ }) {
        Text(text = profileName)
    }
}

@Composable
private fun AddProfileButton() {
    FilledTonalButton(onClick = {/* TODO: implement profile screen */ }) {
        Text(text = "Add new profile")
    }
}

@Preview(showBackground = true)
@Composable
fun EqualizadorApp(){
    EqualizadorG10Theme {
        EqualizadorMainScreen(modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
        )
    }
}