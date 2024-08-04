package com.example.skyflick

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skyflick.api.Constant
import com.example.skyflick.api.NetworkResponse
import com.example.skyflick.api.RetrofitInstance
import com.example.skyflick.api.WeatherApi
import com.example.skyflick.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel:ViewModel() {
    private val weatherApi:WeatherApi=RetrofitInstance.weatherApi
    private val _weatherResult=MutableLiveData<NetworkResponse<WeatherModel>>()
    val
            weatherResult: LiveData<NetworkResponse<WeatherModel>> =_weatherResult


    fun getData( city:String){

        _weatherResult.value=NetworkResponse.Loading

        viewModelScope.launch {
            val response=weatherApi.getWeather(Constant.apiKey,city)

            try {
                if(response.isSuccessful){
                    response.body()?.let {
                        _weatherResult.value=NetworkResponse.Success(it)
                    }

                }else{
                    _weatherResult.value=NetworkResponse.Error("Failed to load Data")
                }
            }
            catch (e:Exception){
                _weatherResult.value=NetworkResponse.Error("Failed to load Data")
            }
        }
    }
}