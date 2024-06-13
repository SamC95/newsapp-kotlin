package com.example.newsapp.data

import com.example.newsapp.R
import com.example.newsapp.adapters.CountryDetails

// List of all the countries with their associated flags
object CountryRepository {
    fun getCountries(): List<CountryDetails> {
        return listOf(
            CountryDetails(R.drawable.icons8_argentina_48, "Argentina"),
            CountryDetails(R.drawable.icons8_australia_48, "Australia"),
            CountryDetails(R.drawable.icons8_austria_48, "Austria"),
            CountryDetails(R.drawable.icons8_belgium_48, "Belgium"),
            CountryDetails(R.drawable.icons8_brazil_48, "Brazil"),
            CountryDetails(R.drawable.icons8_bulgaria_48, "Bulgaria"),
            CountryDetails(R.drawable.icons8_canada_48, "Canada"),
            CountryDetails(R.drawable.icons8_china_48, "China"),
            CountryDetails(R.drawable.icons8_colombia_48, "Colombia"),
            CountryDetails(R.drawable.icons8_cuba_48, "Cuba"),
            CountryDetails(R.drawable.icons8_czech_republic_48, "Czech Republic"),
            CountryDetails(R.drawable.icons8_egypt_48, "Egypt"),
            CountryDetails(R.drawable.icons8_france_48, "France"),
            CountryDetails(R.drawable.icons8_germany_48, "Germany"),
            CountryDetails(R.drawable.icons8_greece_48, "Greece"),
            CountryDetails(R.drawable.icons8_hongkong_48, "Hong Kong"),
            CountryDetails(R.drawable.icons8_hungary_48, "Hungary"),
            CountryDetails(R.drawable.icons8_indonesia_48, "Indonesia"),
            CountryDetails(R.drawable.icons8_ireland_48, "Ireland"),
            CountryDetails(R.drawable.icons8_israel_48, "Israel"),
            CountryDetails(R.drawable.icons8_italy_48, "Italy"),
            CountryDetails(R.drawable.icons8_japan_48, "Japan"),
            CountryDetails(R.drawable.icons8_south_korea_48, "Republic of Korea"),
            CountryDetails(R.drawable.icons8_latvia_48, "Latvia"),
            CountryDetails(R.drawable.icons8_lithuania_48, "Lithuania"),
            CountryDetails(R.drawable.icons8_malaysia_48, "Malaysia"),
            CountryDetails(R.drawable.icons8_mexico_48, "Mexico"),
            CountryDetails(R.drawable.icons8_morocco_48, "Morocco"),
            CountryDetails(R.drawable.icons8_netherlands_48, "Netherlands"),
            CountryDetails(R.drawable.icons8_new_zealand_48, "New Zealand"),
            CountryDetails(R.drawable.icons8_nigeria_circular_48, "Nigeria"),
            CountryDetails(R.drawable.icons8_norway_48, "Norway"),
            CountryDetails(R.drawable.icons8_philippines_48, "Philippines"),
            CountryDetails(R.drawable.icons8_poland_48, "Poland"),
            CountryDetails(R.drawable.icons8_portugal_48, "Portugal"),
            CountryDetails(R.drawable.icons8_romania_48, "Romania"),
            CountryDetails(R.drawable.icons8_russia_48, "Russia"),
            CountryDetails(R.drawable.icons8_saudi_arabia_48, "Saudi Arabia"),
            CountryDetails(R.drawable.icons8_serbia_48, "Serbia"),
            CountryDetails(R.drawable.icons8_singapore_48, "Singapore"),
            CountryDetails(R.drawable.icons8_slovakia_48, "Slovakia"),
            CountryDetails(R.drawable.icons8_slovenia_48, "Slovenia"),
            CountryDetails(R.drawable.icons8_south_africa_48, "South Africa"),
            CountryDetails(R.drawable.icons8_sweden_48, "Sweden"),
            CountryDetails(R.drawable.icons8_switzerland_48, "Switzerland"),
            CountryDetails(R.drawable.icons8_thailand_48, "Thailand"),
            CountryDetails(R.drawable.icons8_turkey_48, "Türkiye"),
            CountryDetails(R.drawable.icons8_united_arab_emirates_48, "United Arab Emirates"),
            CountryDetails(R.drawable.icons8_united_kingdom_48, "United Kingdom"),
            CountryDetails(R.drawable.icons8_usa_48, "United States"),
            CountryDetails(R.drawable.icons8_ukraine_48, "Ukraine"),
            CountryDetails(R.drawable.icons8_venezuela_48, "Venezuela")
        )
    }
}