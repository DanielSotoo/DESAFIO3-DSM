package com.example.countriesweatherapp.utils

object Constants {
    const val COUNTRIES_BASE_URL = "https://restcountries.com/"
    const val WEATHER_BASE_URL = "https://api.weatherapi.com/"

    const val WEATHER_API_KEY = "aee3225c8fbf48d69ea34830252910"

    val REGIONS = listOf(
        "Africa",
        "Americas",
        "Asia",
        "Europe",
        "Oceania"
    )

    val REGIONS_DISPLAY = mapOf(
        "Africa" to "África",
        "Americas" to "América",
        "Asia" to "Asia",
        "Europe" to "Europa",
        "Oceania" to "Oceanía"
    )
}