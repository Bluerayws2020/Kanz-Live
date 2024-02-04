package com.blueray.Kanz.api

import android.util.Base64
import android.util.Log
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.model.AudienceCountResponse
import com.blueray.Kanz.model.CheckUserNameResponse
import com.blueray.Kanz.model.CreateLiveResponse
import com.blueray.Kanz.model.DropDownModel
import com.blueray.Kanz.model.GetLiveVideosResponse
import com.blueray.Kanz.model.GetLiveVideosResults
import com.blueray.Kanz.model.GetProfileResponse
import com.blueray.Kanz.model.MainJsonDropDownModel
import com.blueray.Kanz.model.MainJsonDropDownModelHashTag
import com.blueray.Kanz.model.MainJsonFollowersFollowingData
import com.blueray.Kanz.model.MessageModel
import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.model.NotfiMain
import com.blueray.Kanz.model.RegisterModel
import com.blueray.Kanz.model.RgetrationModel
import com.blueray.Kanz.model.SearchDataModel
import com.blueray.Kanz.model.SearchResponse
import com.blueray.Kanz.model.UpdateProfileResponse
import com.blueray.Kanz.model.UserActionMessage
import com.blueray.Kanz.model.UserActionMessageModel
import com.blueray.Kanz.model.UserLoginModel
import com.blueray.Kanz.model.VideoDataModel
import com.blueray.Kanz.model.VideoUploadeDoneMessage
import com.blueray.Kanz.model.VimeoVideoModelV2
import com.blueray.Kanz.model.checkUserFollowData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException


object NetworkRepository {
    private fun String.toFormBody() = toRequestBody("multipart/form-data".toMediaTypeOrNull())


    suspend fun userOtpLogin(
        user: String,
        password: String,

        language: String,


        ): NetworkResults<UserLoginModel> {
        return withContext(Dispatchers.IO) {
            val passwordBody = password.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userBody = user.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                val results = ApiClient.retrofitService.userOtpLogin(
                    userBody, passwordBody,
                    languageBody,
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun userUplaodeVideo(
        bearerToken: String,
        title: String,
        description: String,
        viemo_link: File,
        uid: String,
        type_of_activity: String,


        ): NetworkResults<VideoUploadeDoneMessage> {
        return withContext(Dispatchers.IO) {
            val titleBody = title.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val descriptionBody = description.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            val commercial_recordBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), viemo_link)
            val videoFile  =  MultipartBody.Part.createFormData("file", viemo_link.name, commercial_recordBody)

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val type_of_activityBody =
                type_of_activity.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                val results = ApiClient.retrofitService.uploadVideoOrImage(
                    bearerToken, titleBody, descriptionBody, videoFile, type_of_activityBody
                )

                Log.e("***", results.toString())
                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.e("***", e.toString())
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getMyFollowingFollower(
        bearerToken: String


        ): NetworkResults<MainJsonFollowersFollowingData> {
        return withContext(Dispatchers.IO) {

            try {
                val results = ApiClient.retrofitService.getMyFollowersFollowingData(
                    bearerToken
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getUserFollowingFollower(

        user_id: String,
        bearerToken: String


    ): NetworkResults<MainJsonFollowersFollowingData> {
        return withContext(Dispatchers.IO) {

            val user_idBody = user_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getUserFollowersFollowingData(
                     bearerToken = bearerToken,
                    user_id = user_idBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun setUserActionPost(
        authToken: String,
        uid: String,          // my id
        entityId: String,     // video id or user id
        entity_type: String,  // node or user
        flag_id: String,


        ): NetworkResults<UserActionMessageModel> {
        return withContext(Dispatchers.IO) {

            val entityIdBody = entityId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val lang = HelperUtils.LANG2.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {

                var results = UserActionMessageModel(UserActionMessage(-1 , "no result"))

                if (flag_id == "like") {
                    results = ApiClient.retrofitService.likeOrUnlikeVideo(
                        authToken, entityIdBody , lang
                    )
                }
                if (flag_id == "save") {
                    results = ApiClient.retrofitService.saveOrCancelSaveVideo(
                        authToken, entityIdBody , lang
                    )
                }


                if (flag_id == "following") {
                    results = ApiClient.retrofitService.followOrUnfollowUser(
                        authToken, entityIdBody , lang
                    )
                }

                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getVideos(
        bearerToken: String,
        uid: String,
        page: Int,
        pageLimit: Int,
        ishome: String

    ): NetworkResults<VideoDataModel> {
        return withContext(Dispatchers.IO) {

               try {
                val results = ApiClient.retrofitService.getVideos(
                    bearerToken,
                    uid,
                    page.toString(), pageLimit.toString(), ishome
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getFlagContent(
        uid: String,

        flagId: String,


        ): NetworkResults<VideoDataModel> {
        return withContext(Dispatchers.IO) {

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val flagIDBody = flagId.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getFlagContent(
                    uidBody,
                    flagIDBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getDeletVideos(
        bearerToken: String,
        video_id: String


    ): NetworkResults<MessageModel> {
        return withContext(Dispatchers.IO) {

            val video_idBody = video_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val lang = HelperUtils.LANG2.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.deletVideo(
                    bearerToken = bearerToken,
                    video_id = video_idBody,
                    lang = lang


                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getNotfication(
        uid: String,


        ): NetworkResults<NotfiMain> {
        return withContext(Dispatchers.IO) {

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getNotfi(
                    uidBody,
                )
                Log.d("notifications", results.datass.toString())

                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("notifications", e.localizedMessage.toString())

                NetworkResults.Error(e)
            }
        }

    }

    suspend fun getSearchContent(
        bearerToken: String,
        text:String


        ): NetworkResults<SearchResponse> {
        return withContext(Dispatchers.IO) {

            val textBody = text.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                val results = ApiClient.retrofitService.getSearch(
                    bearerToken,
                    textBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getVideosForUser(
        bearerToken: String,
        page_limit: String,
        user_profile_uid: String,
        is_home: String,
        page: String

    ): NetworkResults<VideoDataModel> {
        return withContext(Dispatchers.IO) {

//                    ,pagesize.toString()


            try {
                val results = ApiClient.retrofitService.getVideosForUser(
                    bearerToken,
                    page,
                    page_limit,
                    is_home,
                    user_profile_uid
                )
                Log.d("egwky",results.datass.toString())

                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("egwky",e.localizedMessage.toString())
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getMyInfo(
        token: String

    ): NetworkResults<GetProfileResponse> {
        return withContext(Dispatchers.IO) {


            try {
                val results = ApiClient.retrofitService.getMyInfo(
                    token
                )


                Log.d("TEsst", results.toString())
                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("TEsst100", e.localizedMessage.toString())

                NetworkResults.Error(e)

            }
        }
    }
    suspend fun getUserInfo(
        token: String,
        user_id:String
    ):NetworkResults<GetProfileResponse>{
        return withContext(Dispatchers.IO){
            try {
                val results = ApiClient.retrofitService.getUserInfo(
                    token,
                    user_id = user_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
                NetworkResults.Success(results)
            }catch (e:Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getNational(


    ): NetworkResults<List<DropDownModel>> {


        return withContext(Dispatchers.IO) {


            try {
                val results = ApiClient.retrofitService.getNational(
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getCountry(


    ): NetworkResults<MainJsonDropDownModel> {
        return withContext(Dispatchers.IO) {


            try {
                val results = ApiClient.retrofitService.getCountry(
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getCity(

        cid: String

    ): NetworkResults<List<DropDownModel>> {
        return withContext(Dispatchers.IO) {

            val cidBody = cid.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                val results = ApiClient.retrofitService.getCitis(
                    cid
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getEditProfile(

        bearerToken: String,
        full_name: String,
        user_name: String,
        email: String,
        phone: String,
        country_phone_id: String,
        sex: String,
        barth_of_date: String,
        profile_image: File

    ): NetworkResults<UpdateProfileResponse> {
        return withContext(Dispatchers.IO) {
            val full_nameBody = full_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val user_nameBody = user_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val country_phone_idBody = country_phone_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val genderBody = sex.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val barth_of_dateBody =
                barth_of_date.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val lang = HelperUtils.LANG2.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val commercial_recordBody =
                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), profile_image)
            val commercial_record_part  =  MultipartBody.Part.createFormData("profile_image", profile_image.name, commercial_recordBody)

            try {
                val results = ApiClient.retrofitService.editProfile(
                    bearerToken,
                    full_name = full_nameBody,
                    user_name = user_nameBody,
                    date_of_birth = barth_of_dateBody,
                    sex = genderBody,
                    phone = phoneBody,
                    country_phone_id = country_phone_idBody,
                    email = emailBody,
                    image_profile = commercial_record_part,
                    lang
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
            }}

    suspend fun createLive(
        bearerToken: String,
    ):NetworkResults<CreateLiveResponse>{
        return withContext(Dispatchers.IO){
            try {
                val results = ApiClient.retrofitService.createLive(bearerToken)
                NetworkResults.Success(results)
            }catch (e:Exception){
                NetworkResults.Error(e)
            }
        }
    }
    suspend fun getLiveVideos(
        bearerToken: String
    ):NetworkResults<GetLiveVideosResponse>{
        return withContext(Dispatchers.IO){
            try {
                val results = ApiClient.retrofitService.getLiveVideos(bearerToken)
                NetworkResults.Success(results)
            }catch (e:Exception){
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun audienceCount(
        bearerToken: String ,
        live_stream_id:String
    ):NetworkResults<AudienceCountResponse>{
        return withContext(Dispatchers.IO){
            val liveIdBody = live_stream_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {

                val results = ApiClient.retrofitService.audienceCount(bearerToken,liveIdBody)
                NetworkResults.Success(results)
            }catch (e:Exception){
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getCheckUserFollow(

        uid: String,
        targetFollow: String


    ): NetworkResults<checkUserFollowData> {
        return withContext(Dispatchers.IO) {

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val targetFollowUid =
                targetFollow.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                val results = ApiClient.retrofitService.checkUserFollow(
                    uidBody, targetFollowUid
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }
    suspend fun checkUserName(
        bearerToken: String,
        user_name: String
    ):NetworkResults<CheckUserNameResponse>{
        return withContext(Dispatchers.IO){
            val user_nameBody = user_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =ApiClient.retrofitService.checkUserName(bearerToken,user_nameBody)
                NetworkResults.Success(results)
            }catch (e:Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getGender(


    ): NetworkResults<List<DropDownModel>> {
        return withContext(Dispatchers.IO) {


            try {
                val results = ApiClient.retrofitService.getGender(
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getCategory(


    ): NetworkResults<MainJsonDropDownModelHashTag> {
        return withContext(Dispatchers.IO) {


            try {
                val results = ApiClient.retrofitService.getCategory(
                    createBasicAuthHeader("h12", "12")

                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun registerUser(
    data:RegisterModel
    ): NetworkResults<RgetrationModel> {

        return withContext(Dispatchers.IO) {
            try {
                val results = ApiClient.retrofitService.registerUser(
                    data
                )
                when(results.code()){
                200 -> NetworkResults.Success(results.body()!!)
                else -> NetworkResults.Error(IOException("Unexpected response code ${results.code()}"))
                }


            } catch (e: Exception) {
                Log.d("UnDONE", e.toString())
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun addBand(
        band_name: String,
        nationality: String,
        country_of_residence: String,
        types_of_activities: String,
        user_name: String,
        email: String,
        phone: String,
        password: String,
        number_of_band_members: String,
    ): NetworkResults<UserLoginModel> {

        return withContext(Dispatchers.IO) {

            val nationalityBody =
                nationality.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val country_of_residenceBody =
                country_of_residence.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val types_of_activitiesBody =
                types_of_activities.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val user_nameBody = user_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val number_of_band_membersBody =
                number_of_band_members.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val passwordBody = password.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val band_nameBody = band_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())





            try {
                val results = ApiClient.retrofitService.addBandUser(
                    band_nameBody,
                    nationalityBody,
                    country_of_residenceBody,
                    types_of_activitiesBody,
                    user_nameBody,
                    emailBody,
                    phoneBody,
                    passwordBody,
                    number_of_band_membersBody

                )


                Log.d("DONE", results.toString())

                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("UnDONE", e.toString())

                NetworkResults.Error(e)
            }
        }
    }

    fun createBasicAuthHeader(username: String, password: String): String {
        val auth = "$username:$password"
        val encodedAuth = Base64.encodeToString(auth.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
        return "Basic $encodedAuth"
    }


    suspend fun getVimeoVideo(
        videoUrl: String,
        vimeoToken: String
    ): NetworkResults<VimeoVideoModelV2> {
        return withContext(Dispatchers.IO) {
            try {
                val results = ApiClient.retrofitService.getVimeoVideo(videoUrl, vimeoToken)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }
}