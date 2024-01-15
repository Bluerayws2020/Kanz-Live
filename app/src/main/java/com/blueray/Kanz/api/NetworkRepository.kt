package com.blueray.Kanz.api

import android.util.Base64
import android.util.Log
import com.blueray.Kanz.model.DropDownModel
import com.blueray.Kanz.model.FollowingResponse
import com.blueray.Kanz.model.GetMyProfileResponse
import com.blueray.Kanz.model.MainJsonDropDownModel
import com.blueray.Kanz.model.MainJsonDropDownModelHashTag
import com.blueray.Kanz.model.MessageModel

import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.model.NotfiMain
import com.blueray.Kanz.model.SearchDataModel
import com.blueray.Kanz.model.UpdateProfileResponse
import com.blueray.Kanz.model.UserActionMessage
import com.blueray.Kanz.model.UserLoginModel
import com.blueray.Kanz.model.UserUploadeDone
import com.blueray.Kanz.model.VideoDataModel
import com.blueray.Kanz.model.VimeoVideoModelV2
import com.blueray.Kanz.model.checkUserFollowData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.lang.Exception


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
        title: String,
        description: String,

        viemo_link: String,
        uid: String,
        type_of_activity: String,


        ): NetworkResults<UserUploadeDone> {
        return withContext(Dispatchers.IO) {
            val titleBody = title.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val descriptionBody =
                description.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val viemo_linkBody = viemo_link.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val type_of_activityBody =
                type_of_activity.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                val results = ApiClient.retrofitService.userUplaodeVideo(
                    titleBody, descriptionBody, viemo_linkBody, uidBody, type_of_activityBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

//    suspend fun getFollowing(
//        uid: String,
//        targetUid: String,
//
//
//        ): NetworkResults<FollowingResponse> {
//        return withContext(Dispatchers.IO) {
//            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//
//            val targetUidBody = targetUid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//
//            try {
//                val results = ApiClient.retrofitService.getFollowing(
//                    uidBody, targetUidBody
//                )
//                NetworkResults.Success(results)
//            } catch (e: Exception) {
//                NetworkResults.Error(e)
//            }
//        }
//    }
//
//    suspend fun getFollower(
//        uid: String,
//        targetUid: String,
//
//
//        ): NetworkResults<FollowingResponse> {
//        return withContext(Dispatchers.IO) {
//            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//
//            val targetUidBody = targetUid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//
//            try {
//                val results = ApiClient.retrofitService.getFollowers(
//                    uidBody, targetUidBody
//                )
//                NetworkResults.Success(results)
//            } catch (e: Exception) {
//                NetworkResults.Error(e)
//            }
//        }
//    }

    suspend fun getFollowingFollower(
        uid: String,
        targetUid: String,


        ): NetworkResults<FollowingResponse> {
        return withContext(Dispatchers.IO) {
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            val targetUidBody = targetUid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getUserFollowersFollowingData(
                    uidBody, targetUidBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun setUserActionPost(
        uid: String,          // my id
        entityId: String,     // video id or user id
        entity_type: String,  // node or user
        flag_id: String,


        ): NetworkResults<UserActionMessage> {
        return withContext(Dispatchers.IO) {
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val entityIdBody = entityId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val entity_typeBody =
                entity_type.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val flag_idBody = flag_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                var results =  ApiClient.retrofitService.likeOrUnlikeVideo(
                    uidBody, entityIdBody, entity_typeBody
                )


                if (flag_id == "like") {
                    results = ApiClient.retrofitService.likeOrUnlikeVideo(
                        uidBody, entityIdBody, entity_typeBody
                    )
                }
                if (flag_id == "save") {
                    results = ApiClient.retrofitService.saveOrCancelSaveVideo(
                        uidBody, entityIdBody, entity_typeBody
                    )
                }

                if (flag_id == "following") {
                    results = ApiClient.retrofitService.followOrUnfollowUser(
                        uidBody, entityIdBody, entity_typeBody
                    )
                }

                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getVideos(
        uid: String,

        page: Int,
        pageLimit: Int,
        ishome: String

    ): NetworkResults<VideoDataModel> {
        return withContext(Dispatchers.IO) {


            try {
                val results = ApiClient.retrofitService.getPoetries(
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
        uid: String,

        id: String,


        ): NetworkResults<UpdateProfileResponse> {
        return withContext(Dispatchers.IO) {

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val idBody = id.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.deletVideo(
                    uidBody,
                    idBody
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
        uid: String,

        search_key: String,


        ): NetworkResults<SearchDataModel> {
        return withContext(Dispatchers.IO) {

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val search_keyBody = search_key.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getSearch(
                    uidBody,
                    search_keyBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getVideosForUser(
        uid: String, state: String, pagesize: String, user_profile_uid: String,
        is_home: String,
        page: String

    ): NetworkResults<VideoDataModel> {
        return withContext(Dispatchers.IO) {

//                    ,pagesize.toString()


            try {
                val results = ApiClient.retrofitService.getPoetriesForuser(
                    uid,
                    pagesize,
                    state.toString(),
                    user_profile_uid,
                    is_home,
                    page
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getUserProfile(
        token: String

    ): NetworkResults<GetMyProfileResponse> {
        return withContext(Dispatchers.IO) {


            try {
                val results = ApiClient.retrofitService.getUserInfo(
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
        first_name: String,
        last_name: String,
        user_name: String,
        email: String,
        phone: String,
        country_phone_id: String,
        sex: String,
        barth_of_date: String,


        ): NetworkResults<UpdateProfileResponse> {
        return withContext(Dispatchers.IO) {
            val first_nameBody = first_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val last_nameBody = last_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val user_nameBody = user_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val country_phone_idBody = country_phone_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val genderBody = sex.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val barth_of_dateBody =
                barth_of_date.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            // TODO: dont forget to add the image edit
//            val commercial_recordBody =
//                RequestBody.create("multipart/form-data".toMediaTypeOrNull(), profile_image)
//            val commercial_record_part  =  MultipartBody.Part.createFormData("profile_image", profile_image.name, commercial_recordBody)

            try {
                val results = ApiClient.retrofitService.editProfile(
                    bearerToken,
                    first_nameBody,
                    last_nameBody,
                    user_nameBody,
                    barth_of_dateBody,
                    genderBody,
                    phoneBody,
                    country_phone_idBody,
                    emailBody ,

                    )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
            }}

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

    suspend fun addUser(
        first_name: String,
        last_name: String,
        gender: String,
        nationality: String,
        country_of_residence: String,
        types_of_activities: String,
        user_name: String,
        email: String,
        phone: String,
        password: String,
        barth_of_date: String,
    ): NetworkResults<UserLoginModel> {

        return withContext(Dispatchers.IO) {
            val first_nameBody = first_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val last_nameBody = last_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            val genderBody = gender.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val nationalityBody =
                nationality.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val country_of_residenceBody =
                country_of_residence.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val types_of_activitiesBody =
                types_of_activities.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val user_nameBody = user_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val barth_of_dateBody =
                barth_of_date.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val passwordBody = password.toRequestBody("multipart/form-data".toMediaTypeOrNull())





            try {
                val results = ApiClient.retrofitService.addUser(
                    first_nameBody,
                    last_nameBody,
                    genderBody,
                    nationalityBody,
                    country_of_residenceBody,
                    types_of_activitiesBody,
                    user_nameBody,
                    emailBody,
                    phoneBody,
                    passwordBody,
                    barth_of_dateBody

                )


                Log.d("DONE", results.toString())

                NetworkResults.Success(results)
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