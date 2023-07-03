package com.alphacreators.myapplication

import com.google.gson.annotations.SerializedName


 //  This data class is used to hold the data of API

data class MainApiClass(
    @SerializedName("today_clicks") var todayClicks: Int? = null,
    @SerializedName("top_location") var topLocation: String? = null,
    @SerializedName("top_source") var topSource: String? = null,
    @SerializedName("data") var apiData: ApiData) {
}