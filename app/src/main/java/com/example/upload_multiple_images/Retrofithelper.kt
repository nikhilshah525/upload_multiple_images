package com.example.upload_multiple_images

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder





object Retrofithelper {

    private const val BASEURL = "https://nikhil525.000webhostapp.com/practice/"

    fun getInstance(): Retrofit {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val clientInfoStatus = OkHttpClient.Builder()
        clientInfoStatus.addInterceptor(logging)

        val gson: Gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder().baseUrl(BASEURL).client(clientInfoStatus.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


}