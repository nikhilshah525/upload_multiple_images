package com.example.upload_multiple_images

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface ImageService {


    @POST("xyz.php")
    @FormUrlEncoded
    fun upload_image(
        @Field("status") status: String,
        @Field("leadid") leadid: String,
        @Field("image") image: String,

        ): Call<Image_Response>

}