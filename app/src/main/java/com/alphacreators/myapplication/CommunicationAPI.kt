package com.alphacreators.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

// This interface is used to define API Endpoints


interface CommunicationAPI {


    // This endpoint will used to fetch databoard data

    @GET("dashboardNew")
    fun getDashBoardData(@Header("Authorization") authorization: String): Call<MainApiClass>


    // This endpoint will used to fetch top links  data

    @GET("dashboardNew")
     fun getTopLinksData(@Header("Authorization") authorization: String): Call<MainApiClass>

    // This endpoint will used to fetch recent links data

    @GET("dashboardNew")
    fun getRecentLinksData(@Header("Authorization") authorization: String): Call<MainApiClass>

}