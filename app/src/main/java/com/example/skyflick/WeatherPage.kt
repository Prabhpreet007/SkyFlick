package com.example.skyflick

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skyflick.api.NetworkResponse
import com.example.skyflick.api.WeatherModel
import org.w3c.dom.Text
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


@Composable

fun WeatherPage(viewModel: WeatherViewModel){


    var city by remember {
        mutableStateOf("")
    }
    val keyboardController= LocalSoftwareKeyboardController.current

    val weatherResult=viewModel.weatherResult.observeAsState()

Column(modifier = Modifier
    .fillMaxWidth()
    .padding(8.dp),horizontalAlignment = Alignment.CenterHorizontally) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){
        OutlinedTextField(modifier = Modifier.weight(1f),value = city, onValueChange = {
            city=it
        }, 
            label = { Text(text = "enter the city")}

            )

        IconButton(onClick = { viewModel.getData(city)
            keyboardController?.hide()
        }) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "search the city")
        }
    }
    when(val result=weatherResult.value){
        is NetworkResponse.Error -> Text(text=result.message)
        NetworkResponse.Loading -> CircularProgressIndicator()
        is NetworkResponse.Success -> WeatherDetails(result.data)
        null -> {}
    }
}
}

@Composable
fun WeatherDetails(data:WeatherModel){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom) {
            Icon(imageVector = Icons.Default.LocationOn, contentDescription ="Location icon",
                modifier = Modifier.size(40.dp))
            
            Text(text = data.location.name, fontSize = 30.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.location.country, fontSize = 18.sp, color = Color.Gray)}
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${data.current.temp_c}° c",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            AsyncImage(modifier = Modifier.size(160.dp),model = "https:${data.current.condition.icon}".replace("64x64","128x128"), contentDescription = "condition icon")

        Text(
            text = data.current.condition.text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        WeatherKeyVal(key = "Humidity", value = "${data.current.humidity}%")
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        WeatherKeyVal(key = "Wind Speed", value = "${data.current.wind_kph} km/h")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        WeatherKeyVal(key = "Precipitation", value = "${data.current.precip_mm} mm")
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        WeatherKeyVal(key = "UV", value = "${data.current.uv}")
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherKeyVal(key:String,value:String){
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value,fontSize=24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold,color=Color.Gray)
    }
}