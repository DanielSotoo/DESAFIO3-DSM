package com.example.countriesweatherapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.countriesweatherapp.R
import com.example.countriesweatherapp.controller.CountryController
import com.example.countriesweatherapp.model.Country
import com.example.countriesweatherapp.view.adapter.CountryAdapter

class CountriesActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var countryController: CountryController
    private var region: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries)

        region = intent.getStringExtra("REGION") ?: ""
        supportActionBar?.title = "Países de $region"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listView = findViewById(R.id.listViewCountries)
        progressBar = findViewById(R.id.progressBar)

        countryController = CountryController()

        loadCountries()
    }

    private fun loadCountries() {
        progressBar.visibility = View.VISIBLE
        listView.visibility = View.GONE

        countryController.getCountriesByRegion(
            region = region,
            onSuccess = { countries ->
                progressBar.visibility = View.GONE
                listView.visibility = View.VISIBLE
                setupCountriesList(countries)
            },
            onError = { error ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun setupCountriesList(countries: List<Country>) {
        val adapter = CountryAdapter(this, countries)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            try {
                val selectedCountry = countries[position]
                val intent = Intent(this, CountryDetailActivity::class.java)

                // Validaciones robustas para evitar crashes
                intent.putExtra("COUNTRY_NAME", selectedCountry.name.common)
                intent.putExtra("COUNTRY_OFFICIAL_NAME", selectedCountry.name.official)
                intent.putExtra("COUNTRY_CAPITAL", selectedCountry.capital?.firstOrNull() ?: "N/A")
                intent.putExtra("COUNTRY_REGION", selectedCountry.region)
                intent.putExtra("COUNTRY_SUBREGION", selectedCountry.subregion ?: "N/A")
                intent.putExtra("COUNTRY_POPULATION", selectedCountry.population ?: 0L)
                intent.putExtra("COUNTRY_FLAG", selectedCountry.flags?.png ?: "")
                intent.putExtra("COUNTRY_CCA2", selectedCountry.cca2 ?: "")
                intent.putExtra("COUNTRY_CCA3", selectedCountry.cca3 ?: "")

                // Convertir monedas a String con validación
                val currencies = try {
                    selectedCountry.currencies?.values?.joinToString(", ") {
                        "${it.name ?: "N/A"} (${it.symbol ?: "N/A"})"
                    } ?: "N/A"
                } catch (e: Exception) {
                    "N/A"
                }
                intent.putExtra("COUNTRY_CURRENCIES", currencies)

                // Convertir idiomas a String con validación
                val languages = try {
                    selectedCountry.languages?.values?.joinToString(", ") ?: "N/A"
                } catch (e: Exception) {
                    "N/A"
                }
                intent.putExtra("COUNTRY_LANGUAGES", languages)

                // Coordenadas con validación
                val lat = selectedCountry.latlng?.getOrNull(0) ?: 0.0
                val lng = selectedCountry.latlng?.getOrNull(1) ?: 0.0
                intent.putExtra("COUNTRY_LAT", lat)
                intent.putExtra("COUNTRY_LNG", lng)

                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Error al abrir país: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}