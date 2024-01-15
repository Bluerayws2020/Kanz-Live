package com.blueray.Kanz.api

import com.blueray.Kanz.model.DropDownModel
import com.blueray.Kanz.model.FollowingResponse
import com.blueray.Kanz.model.GetMyProfileResponse
import com.blueray.Kanz.model.MainJsonDropDownModel
import com.blueray.Kanz.model.MainJsonDropDownModelHashTag
import com.blueray.Kanz.model.MainJsonFollowersFollowingData
import com.blueray.Kanz.model.MessageModel
import com.blueray.Kanz.model.NotfiMain
import com.blueray.Kanz.model.SearchDataModel
import com.blueray.Kanz.model.UpdateProfileResponse
import com.blueray.Kanz.model.UserActionMessage
import com.blueray.Kanz.model.UserLoginModel
import com.blueray.Kanz.model.UserUploadeDone
import com.blueray.Kanz.model.VideoDataModel
import com.blueray.Kanz.model.VimeoVideoModelV2
import com.blueray.Kanz.model.checkUserFollowData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiServices {
    @Multipart
    @POST("ar/app/poet-registration")
    suspend fun addUser(

        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("nationality") nationality: RequestBody,
        @Part("country_of_residence") country_of_residence: RequestBody,
        @Part("types_of_activities") types_of_activities: RequestBody,
        @Part("user_name") user_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("barth_of_date") barth_of_date: RequestBody,




        ): UserLoginModel


    @Multipart
    @POST("app/band-registration")
    suspend fun addBandUser(

        @Part("band_name") band_name: RequestBody,
        @Part("nationality") nationality: RequestBody,
        @Part("country_of_residence") country_of_residence: RequestBody,
        @Part("types_of_activities") types_of_activities: RequestBody,
        @Part("user_name") user_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("number_of_band_members") barth_of_date: RequestBody,




        ): UserLoginModel


    @Multipart
    @POST("user/login")
    suspend fun userOtpLogin(
        @Part("user_name") user_name: RequestBody,
        @Part("password") password: RequestBody,
        @Part("lang") lang: RequestBody,

        ): UserLoginModel


    @Multipart
    @POST("ar/app/add-poetry2")
    suspend fun userUplaodeVideo(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("viemo_link") viemo_link: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part("type_of_activity") type_of_activity: RequestBody,

        ): UserUploadeDone





    @Multipart
    @POST("app/check-user-permission")
    suspend fun checkUserPermission(
        @Part("uid") uid: RequestBody,

        ): UserUploadeDone
//
//    @Multipart
//    @POST("app/following-users")
//    suspend fun getFollowing(
//        @Part("uid") uid: RequestBody,
//        @Part("target_uid") target_uid:RequestBody
//
//
//        ): FollowingResponse
//
//    @Multipart
//    @POST("app/followers")
//    suspend fun getFollowers(
//        @Part("uid") uid: RequestBody,
//        @Part("target_uid") target_uid:RequestBody
//
//
//    ): FollowingResponse

    @Multipart
    @POST("user/getUserFollowersFollowingData")
    suspend fun getUserFollowersFollowingData(
        @Part("uid") uid: RequestBody,
        @Part("target_uid") target_uid:RequestBody

    ): FollowingResponse

    @Multipart
    @POST("user/getMyFollowersFollowingData")
    suspend fun getMyFollowersFollowingData(
        @Header("Authorization") bearerToken: String,
        @Part("uid") uid: RequestBody,
        @Part("target_uid") target_uid:RequestBody

    ): MainJsonFollowersFollowingData


    @Multipart
    @POST("app/flag-action")
    suspend fun ActionPost(
        @Part("uid") uid: RequestBody,
        @Part("entity_id") entity_id: RequestBody,
        @Part("entity_type") entity_type: RequestBody,
        @Part("flag_id") flag_id: RequestBody,

        ): MessageModel

    @Multipart
    @POST("user/likeOrUnlikeVideo")
    suspend fun likeOrUnlikeVideo(
        @Part("uid") uid: RequestBody,
        @Part("entity_id") entity_id: RequestBody,
        @Part("entity_type") entity_type: RequestBody,
//        @Part("flag_id") flag_id: RequestBody,

        ): UserActionMessage

    @Multipart
    @POST("user/saveOrCancelSaveVideo")
    suspend fun saveOrCancelSaveVideo(
        @Part("uid") uid: RequestBody,
        @Part("entity_id") entity_id: RequestBody,
        @Part("entity_type") entity_type: RequestBody,
//        @Part("flag_id") flag_id: RequestBody,
    ): UserActionMessage

    @Multipart
    @POST("user/followOrUnfollowUser")
    suspend fun followOrUnfollowUser(
        @Part("uid") uid: RequestBody,
        @Part("entity_id") entity_id: RequestBody,
        @Part("entity_type") entity_type: RequestBody,
//        @Part("flag_id") flag_id: RequestBody,
    ): UserActionMessage


    @GET("app2/poetries")
    suspend fun getPoetries(
                    @Query("uid")  uid:String,
                    @Query("page")  page:String,
                    @Query("page_limit")  page_limit:String,
                    @Query("is_home")is_home:String,


        ): VideoDataModel






    @GET("app2/poetries")
    suspend fun getPoetriesForuser(
        @Query("uid")  uid:String,
        @Query("page_limit")  page_limit:String,

        @Query("state")  state:String,
        @Query("user_profile_uid")user_profile_uid:String,
        @Query("is_home")is_home:String,
        @Query("page")page:String



    ): VideoDataModel






    @Multipart
    @POST("app/flag-conents-list")
    suspend fun getFlagContent(


        @Part("uid") uid: RequestBody,
    @Part("flag_id") flag_id: RequestBody,

    ): VideoDataModel


    @Multipart
    @POST("ar/app/delete-poetry")
    suspend fun deletVideo(


        @Part("uid") uid: RequestBody,
        @Part("id") id: RequestBody,

        ): UpdateProfileResponse
@Multipart
    @POST("app/notifications")
    suspend fun getNotfi(
        @Part("uid") uid: RequestBody,

    ): NotfiMain


    @Multipart
    @POST("app/search")
    suspend fun getSearch(


        @Part("uid") uid: RequestBody,
        @Part("search_key") SearchDataModel: RequestBody,

        ): SearchDataModel





    @Multipart
    @POST("app/check-user-follow")
    suspend fun checkUserFollow(


        @Part("uid") uid: RequestBody,
        @Part("target_uid") target_uid: RequestBody,

        ): checkUserFollowData

    @POST("user/getMyProfile")
    suspend fun getUserInfo(
        @Header("Authorization") bearerToken:String

    ):GetMyProfileResponse



    @Multipart
    @POST("user/updateMyProfile")
    suspend fun editProfile(
        @Header("Authorization") bearerToken: String,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("user_name") user_name: RequestBody,
        @Part("date_of_birth") date_of_birth: RequestBody,
        @Part("sex") sex: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("country_phone_id") country_phone_id: RequestBody,
        @Part("email") email: RequestBody,
//        @Part image_profile :MultipartBody.Part
    ): UpdateProfileResponse
    @GET("app/nationality-list")
    suspend fun getNational( ): List<DropDownModel>





    @GET("app/country-list")
    suspend fun getCitis(@Query("pid")  pid:String
    ): List<DropDownModel>


    @POST("frontend/getCountries")
    suspend fun getCountry  (): MainJsonDropDownModel

    @GET("app/gender-list")
    suspend fun getGender( ): List<DropDownModel>

//
//    @GET("app/activity-list")
//    suspend fun getCategory(@Header("Authorization") authHeader: String ): List<DropDownModel>


    @POST("frontend/getHashtags")
    suspend fun getCategory(@Header("Authorization") authHeader: String ): MainJsonDropDownModelHashTag


    @GET
    suspend fun getVimeoVideo(
        @Url videoUrl: String,
        @Header("Authorization") authorizationToken:String,
    ): VimeoVideoModelV2















}