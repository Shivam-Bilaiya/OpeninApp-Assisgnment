package com.alphacreators.myapplication

import com.google.gson.annotations.SerializedName


data class ApiData(
    @SerializedName("recent_links") var recentLinks: List<RecentLinks> = arrayListOf(),
    @SerializedName("top_links") var topLinks: List<TopLinks> = arrayListOf(),
    @SerializedName("overall_url_chart") var overallUrlChart: OverallUrlChart? = OverallUrlChart()
) {
    override fun toString(): String {
        return "ApiData(topLinks=$topLinks)"
    }
}