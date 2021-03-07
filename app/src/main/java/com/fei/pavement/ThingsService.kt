package com.example.networktest

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ThingsService  {
    @GET("/user/getUser/{username}/{password}")
    fun searchThings(@Path("username")queryAdmin:String, @Path("password")queryPassword:String): Call<ThingsResponse>

    @POST("final_network-1.0-SNAPSHOT/user/register")
    fun searchPostThings( @Body thingsPost:searchNeedThings): Call<ThingsPost>

    @FormUrlEncoded
    @POST("api/user/register")
    fun searchRegister2(@Field("username")username :String,@Field("password")password :String, @Field("email") email:String,@Field("nicknames")nicknames:String):Call<ThingsResponse>

    @FormUrlEncoded
    @POST("api/user/login")
    fun searchLogin(@Field("username")account :String,@Field("password") password:String):Call<ThingsResponse2>

    @GET("departmentNotice/getDeptNotice")
    fun searchDeptNotice(@Query("departmentName") departmentName:String):Call<SearchDeptNotices>
//    ("/user/getUser/{username}/{password}")
//    fun searchThings(@Path("username")queryAdmin:String,@Path("password")queryPassword:String): Call<ThingsResponse>

    @GET("Myproject/getLatitudeLongitude")
    fun searchPage(@Query("pageNum")pageNum:String,@Query("pageSize")pageSize:String):Call<SearchPage>
}
