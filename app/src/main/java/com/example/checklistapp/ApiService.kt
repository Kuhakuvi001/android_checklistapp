package com.example.checklistapp

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {


    @POST("register")
    fun register(
        @Body body: JsonObject
    ): Call<JsonObject>

    @POST("login")
    fun login(
        @Body body: JsonObject
    ): Call<JsonObject>

    @GET("checklist")
    fun getchecklist(
        @Header("Authorization")auth:String
    ): Call<JsonObject>

    @DELETE("checklist/{ID}")
    fun deletechecklist(
        @Path("ID") id: String,
        @Header("Authorization")auth:String
    ): Call<JsonObject>

    @POST("checklist")
    fun savechecklist(
        @Header("Authorization")auth:String,
        @Body body: JsonObject
    ): Call<JsonObject>

    @DELETE("item/{ID}")
    fun deleteitem(
        @Path("ID") id: String,
        @Header("Authorization")auth:String
    ): Call<JsonObject>

    @PUT("item/{ID}")
    fun updateitem(
        @Path("ID") id: String,
        @Header("Authorization")auth:String
    ): Call<JsonObject>

    @POST("item")
    fun additem(
        @Header("Authorization")auth:String,
        @Body body: JsonObject
    ): Call<JsonObject>

    @GET("item/{ID}")
    fun getitem(
        @Path("ID") id: String,
        @Header("Authorization")auth:String
    ): Call<JsonObject>

    @PUT("item/rename/{ID}")
    fun gantinama(
        @Path("ID") id: String,
        @Header("Authorization")auth:String,
        @Body body: JsonObject
    ): Call<JsonObject>
}