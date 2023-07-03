package com.alphacreators.myapplication

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// This will create retrofit instance and return it


object RetrofitService {

    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api.inopenapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create(Gson())).build()
    }

}