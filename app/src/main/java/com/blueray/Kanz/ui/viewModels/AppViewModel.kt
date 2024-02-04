package com.blueray.Kanz.ui.viewModels

import android.app.Application
import android.view.PixelCopy.Request
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blueray.Kanz.api.NetworkRepository
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.model.AudienceCountResponse
import com.blueray.Kanz.model.CheckUserNameResponse
import com.blueray.Kanz.model.CreateLiveResponse
import com.blueray.Kanz.model.DropDownModel
import com.blueray.Kanz.model.FollowingResponse
import com.blueray.Kanz.model.GetLiveVideosResponse
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
import com.blueray.Kanz.model.UserActionMessageModel
import com.blueray.Kanz.model.UserLoginModel
import com.blueray.Kanz.model.VideoDataModel
import com.blueray.Kanz.model.VideoUploadeDone
import com.blueray.Kanz.model.VideoUploadeDoneMessage
import com.blueray.Kanz.model.VimeoVideoModelV2
import com.blueray.Kanz.model.checkUserFollowData
import kotlinx.coroutines.launch
import java.io.File


class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val deviceId = HelperUtils.getAndroidID(application.applicationContext)

    private val language = HelperUtils.getLang(application.applicationContext)
    private val userId = HelperUtils.getUid(application.applicationContext)
    private val userToken = HelperUtils.getUserToken(application.applicationContext)

    private val repo = NetworkRepository


    private val getMyFollowingFollowerLive =
        MutableLiveData<NetworkResults<MainJsonFollowersFollowingData>>()
    private val getUserFollowingFollowerLive =
        MutableLiveData<NetworkResults<MainJsonFollowersFollowingData>>()
    private val getFollowerLive = MutableLiveData<NetworkResults<FollowingResponse>>()

    private val loginUserMessageLiveData = MutableLiveData<NetworkResults<UserLoginModel>>()

    private val getVideosLive = MutableLiveData<NetworkResults<VideoDataModel>>()

    private val getFlagContetntLive = MutableLiveData<NetworkResults<VideoDataModel>>()
    private val getNotficationLive = MutableLiveData<NetworkResults<NotfiMain>>()

    private val getSearchLive = MutableLiveData<NetworkResults<SearchResponse>>()

    private val getUserVideosLive = MutableLiveData<NetworkResults<VideoDataModel>>()
    private val viewMyPrfofile = MutableLiveData<NetworkResults<GetProfileResponse>>()
    private val viewUserPrfofile = MutableLiveData<NetworkResults<GetProfileResponse>>()

    private val vimeoVideoLiveData = MutableLiveData<NetworkResults<VimeoVideoModelV2>>()

    private val natoinalLiveData = MutableLiveData<NetworkResults<List<DropDownModel>>>()
    private val coityLiveData = MutableLiveData<NetworkResults<List<DropDownModel>>>()
    private val countyLiveData = MutableLiveData<NetworkResults<MainJsonDropDownModel>>()
    private val getCheckFollowId = MutableLiveData<NetworkResults<checkUserFollowData>>()
    private val getCheckUserName = MutableLiveData<NetworkResults<CheckUserNameResponse>>()

    private val genderLive = MutableLiveData<NetworkResults<List<DropDownModel>>>()
    private val categroLive = MutableLiveData<NetworkResults<MainJsonDropDownModelHashTag>>()


    private val createAccountLive = MutableLiveData<NetworkResults<RgetrationModel>>()
    private val createAccountBandLive = MutableLiveData<NetworkResults<UserLoginModel>>()

    private val getLiveVideosLiveData = MutableLiveData<NetworkResults<GetLiveVideosResponse>>()
    private val audienceCountLiveData = MutableLiveData<NetworkResults<AudienceCountResponse>>()
    private val userUplaodeLoive = MutableLiveData<NetworkResults<VideoUploadeDoneMessage>>()
    private val setActions = MutableLiveData<NetworkResults<UserActionMessageModel>>()
    private val updateUserLive = MutableLiveData<NetworkResults<UpdateProfileResponse>>()
    private val createLiveData = MutableLiveData<NetworkResults<CreateLiveResponse>>()
    private val deletVideoLive = MutableLiveData<NetworkResults<MessageModel>>()


    fun retriveMainVideos(page: Int, pageLimit: Int, ishome: String) {
        val authToken = userToken
        viewModelScope.launch {
            getVideosLive.value = repo.getVideos(authToken, userId, page, pageLimit, ishome)
        }
    }

    fun getMainVideos() = getVideosLive


    fun retriveDeleteVideo(video_id: String) {
        val authToken = "Bearer $userToken"
        viewModelScope.launch {
            deletVideoLive.value = repo.getDeletVideos(authToken, video_id)
        }
    }

    fun getDeletVideos() = deletVideoLive

    fun retriveFlagContent(flag: String) {

        viewModelScope.launch {
            getFlagContetntLive.value = repo.getFlagContent(userId, flag)
        }
    }

    fun getFlagContent() = getFlagContetntLive


    fun retriveNotfication() {

        viewModelScope.launch {
            getNotficationLive.value = repo.getNotfication(userId)
        }
    }

    fun getNotifcation() = getNotficationLive


    fun retrivesearchTxt(text: String) {
        val authToken = "Bearer $userToken"
        viewModelScope.launch {
            getSearchLive.value = repo.getSearchContent(authToken, text)
        }
    }

    fun getSerchData() = getSearchLive


    fun retriveUserUplaode(
        title: String,
        description: String,

        viemo_link: File,
        type_of_activity: String,
    ) {
        val authToken = "Bearer $userToken"
        viewModelScope.launch {
            userUplaodeLoive.value =
                repo.userUplaodeVideo(authToken, title, description, viemo_link, userId, type_of_activity)
        }
    }

    fun getUplaodeVide() = userUplaodeLoive

    fun retriveMyFollowingFollower(

    ) {
        val authToken = "Bearer $userToken"
        viewModelScope.launch {
            getMyFollowingFollowerLive.value = repo.getMyFollowingFollower(authToken)
        }
    }


    fun getMyFollowingFollowers() = getMyFollowingFollowerLive


    fun retriveUserFollowingFollower(
        user_id: String
    ) {
        val authToken = "Bearer $userToken"
        viewModelScope.launch {
            getUserFollowingFollowerLive.value = repo.getUserFollowingFollower(
                user_id = user_id,
                bearerToken = authToken
            )
        }
    }

    fun getUserFollowingFollowers() = getUserFollowingFollowerLive


    fun retriveFollower(
        targetUidBody: String
    ) {

//        viewModelScope.launch {
//            getFollowerLive.value = repo.getFollowingFollower(userId, targetUidBody)
//        }
    }



    fun updateUserProfile(
        first_name: String,
        user_name: String,
        email: String,
        phone: String,
        country_phone_id: String,

        sex: String,
        barth_of_date: String,
        profile_image: File
        ) {
        viewModelScope.launch {
            val authToken = "Bearer $userToken"
            updateUserLive.value = repo.getEditProfile(
                authToken,
               full_name = first_name ,
                user_name =  user_name,
                email = email,
                phone = phone,
                country_phone_id = country_phone_id,
                profile_image = profile_image,
                sex = sex,
                barth_of_date = barth_of_date,
                )


        }
    }

    fun getUpdateUserLive() = updateUserLive

    fun retriveSetAction(
        entityId: String,
        entity_type: String,
        flag_id: String,
    ) {
        val authToken = "Bearer $userToken"
        viewModelScope.launch {
            setActions.value = repo.setUserActionPost(authToken,userId, entityId, entity_type, flag_id)
        }
    }

    fun getSetAction() = setActions


    fun retriveUserVideos(
         page_limit: String, user_profile_uid: String,
        is_home: String,
        page: String
    ) {
        val authToken = userToken
        viewModelScope.launch {
            getUserVideosLive.value =
                repo.getVideosForUser(authToken, page_limit, user_profile_uid, is_home, page)
        }
    }

    fun getUserVideos() = getUserVideosLive

    fun retriveViewMyProfile (

    ) {

        viewModelScope.launch {
            val authToken = "Bearer $userToken"
            viewMyPrfofile.value = repo.getMyInfo(authToken)

            }}

    fun getMyProfile() = viewMyPrfofile

    fun retriveUserProfile(
        user_id:String
    ){

        val authToken = "Bearer $userToken"
        viewModelScope.launch {
            viewUserPrfofile.postValue(repo.getUserInfo(authToken , user_id = user_id))
        }

    }
    fun getUserProfile() = viewUserPrfofile

    fun retrieveLiveVideos(){
        val authToken = "Bearer $userToken"
        viewModelScope.launch {
            getLiveVideosLiveData.postValue(repo.getLiveVideos(authToken))
        }
    }
    fun getLiveVideos() = getLiveVideosLiveData

    fun retrieveAudienceCount(
        live_stream_id:String
    ){
        val authToken = "Bearer $userToken"
        viewModelScope.launch {
            audienceCountLiveData.postValue(repo.audienceCount(authToken,live_stream_id))
        }
    }
    fun getAudienceCount() = audienceCountLiveData

    fun retrieveVideoOption(videoUrl: String, vimeoToken: String) {

        viewModelScope.launch {
            val authToken = "Bearer $vimeoToken"
            vimeoVideoLiveData.value = repo.getVimeoVideo(videoUrl, authToken)

        }
    }

    fun getVideoOption() = vimeoVideoLiveData


    fun retriveNatonal() {
        viewModelScope.launch {
            natoinalLiveData.value = repo.getNational()
        }
    }

    fun getNatonal() = natoinalLiveData


    fun retriveContry() {
        viewModelScope.launch {
            countyLiveData.value = repo.getCountry()
        }
    }

    fun getCountry() = countyLiveData


    fun retriveCity(cid: String) {
        viewModelScope.launch {
            coityLiveData.value = repo.getCity(cid)
        }
    }

    fun getCity() = coityLiveData


    fun retriveCheckUserFolow(targetFollowId: String) {
        viewModelScope.launch {
            getCheckFollowId.value = repo.getCheckUserFollow(userId, targetFollowId)
        }
    }


    fun getFollowCheckUser() = getCheckFollowId

    fun retriveCheckUserName(user_name: String){
        val authToken = "Bearer $userToken"
        viewModelScope.launch {
            getCheckUserName.postValue(repo.checkUserName(authToken , user_name))
        }

    }
    fun getCheckUserName() = getCheckUserName
    fun retriveGender() {
        viewModelScope.launch {
            genderLive.value = repo.getGender()
        }
    }

    fun getGender() = genderLive

    fun retrieveCreateLive(){
        val authToken = "Bearer $userToken"
        viewModelScope.launch {
            createLiveData.postValue(repo.createLive(authToken))
        }
    }

    fun getCreateLive() = createLiveData


    fun retriveCategory() {
        viewModelScope.launch {
            categroLive.value = repo.getCategory()
        }
    }

    fun getCategory() = categroLive


    fun retriveCreateAccount(
        data: RegisterModel
    ) {
        viewModelScope.launch {

            createAccountLive.value = repo.registerUser(
            data
            )
        }
    }

    fun getCreateAccount() = createAccountLive


    fun retriveBandName(
        band_name: String,
        nationality: String,
        country_of_residence: String,
        types_of_activities: String,
        user_name: String,
        email: String,
        phone: String,
        password: String,
        number_of_band_members: String,
    ) {
        viewModelScope.launch {
            createAccountBandLive.value = repo.addBand(
                band_name,
                nationality,
                country_of_residence,
                types_of_activities,
                user_name,
                email,
                phone,
                password,
                number_of_band_members
            )
        }

    }

    fun getBandAccount() = createAccountBandLive


    fun retriveLogin(userName: String, password: String) {
        viewModelScope.launch {
            loginUserMessageLiveData.value = repo.userOtpLogin(userName, password, language)
        }
    }

    fun getLogin() = loginUserMessageLiveData


}