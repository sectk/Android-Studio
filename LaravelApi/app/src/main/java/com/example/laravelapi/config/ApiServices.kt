package com.example.laravelapi.config

import retrofit2.Call
import com.example.laravelapi.model.ResponseListMahasiswa
import com.example.laravelapi.model.SubmitMahasiswa
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServices {

    @GET("mahasiswa")
    fun getMahasiswa(): Call<ResponseListMahasiswa>

    @GET("carimahasiswa")
    fun cariMahasiswa(@Query("cari") terms: String?): Call<ResponseListMahasiswa>

    @FormUrlEncoded
    @POST("mahasiswa")
    fun postMahasiswa(
        @Field("namamahasiswa") namamahasiswa: String,
        @Field("nim") nim: String,
        @Field("alamat") alamat: String,
        @Field("gender") gender: String,
        @Field("agama") agama: String,
        @Field("usia") usia: String
    ): Call<SubmitMahasiswa>
}
