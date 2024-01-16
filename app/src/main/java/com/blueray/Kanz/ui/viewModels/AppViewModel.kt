package com.blueray.Kanz.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blueray.Kanz.api.NetworkRepository
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.model.DropDownModel
import com.blueray.Kanz.model.FollowingResponse
import com.blueray.Kanz.model.GetMyProfileResponse
import com.blueray.Kanz.model.MainJsonDropDownModel
import com.blueray.Kanz.model.MainJsonDropDownModelHashTag
import com.blueray.Kanz.model.MessageModel

import com.blueray.Kanz.model.NetworkResults
import com.blueray.Kanz.model.NotfiMain
import com.blueray.Kanz.model.RegisterModel
import com.blueray.Kanz.model.RgetrationModel
import com.blueray.Kanz.model.SearchDataModel
import com.blueray.Kanz.model.UpdateProfileResponse
import com.blueray.Kanz.model.UserActionMessage
import com.blueray.Kanz.model.UserLoginModel
import com.blueray.Kanz.model.UserUploadeDone
import com.blueray.Kanz.model.VideoDataModel
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


    private val getFollowingLive = MutableLiveData<NetworkResults<FollowingResponse>>()
    private val getFollowerLive = MutableLiveData<NetworkResults<FollowingResponse>>()

    private val loginUserMessageLiveData = MutableLiveData<NetworkResults<UserLoginModel>>()

    private val getVideosLive = MutableLiveData<NetworkResults<VideoDataModel>>()

    private val getFlagContetntLive = MutableLiveData<NetworkResults<VideoDataModel>>()
    private val getNotficationLive = MutableLiveData<NetworkResults<NotfiMain>>()

    private val getSearchLive = MutableLiveData<NetworkResults<SearchDataModel>>()

    private val getUserVideosLive = MutableLiveData<NetworkResults<VideoDataModel>>()
    private val viewUserPrfofile = MutableLiveData<NetworkResults<GetMyProfileResponse>>()

    private val vimeoVideoLiveData = MutableLiveData<NetworkResults<VimeoVideoModelV2>>()

    private val natoinalLiveData = MutableLiveData<NetworkResults<List<DropDownModel>>>()
    private val coityLiveData = MutableLiveData<NetworkResults<List<DropDownModel>>>()
    private val countyLiveData = MutableLiveData<NetworkResults<MainJsonDropDownModel>>()
    private val getCheckFollowId = MutableLiveData<NetworkResults<checkUserFollowData>>()

    private val genderLive = MutableLiveData<NetworkResults<List<DropDownModel>>>()
    private val categroLive = MutableLiveData<NetworkResults<MainJsonDropDownModelHashTag>>()


    private val createAccountLive = MutableLiveData<NetworkResults<RgetrationModel>>()
    private val createAccountBandLive = MutableLiveData<NetworkResults<UserLoginModel>>()


    private val userUplaodeLoive = MutableLiveData<NetworkResults<UserUploadeDone>>()
    private val setActions = MutableLiveData<NetworkResults<UserActionMessage>>()
    private val updateUserLive = MutableLiveData<NetworkResults<UpdateProfileResponse>>()

    private val deletVideoLive = MutableLiveData<NetworkResults<UpdateProfileResponse>>()


    fun retriveMainVideos(page: Int, pageLimit: Int, ishome: String) {

        viewModelScope.launch {
            getVideosLive.value = repo.getVideos(userId, page, pageLimit, ishome)
        }
    }

    fun getMainVideos() = getVideosLive


    fun retriveDeleteVideo(id: String) {

        viewModelScope.launch {
            deletVideoLive.value = repo.getDeletVideos(userId, id)
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


    fun retrivesearchTxt(txt: String) {

        viewModelScope.launch {
            getSearchLive.value = repo.getSearchContent(userId, txt)
        }
    }

    fun getSerchData() = getSearchLive


    fun retriveUserUplaode(
        title: String,
        description: String,

        viemo_link: String,
        type_of_activity: String,
    ) {

        viewModelScope.launch {
            userUplaodeLoive.value =
                repo.userUplaodeVideo(title, description, viemo_link, userId, type_of_activity)
        }
    }

    fun getUplaodeVide() = userUplaodeLoive
    fun retriveFollowing(
        targetUidBody: String
    ) {

        viewModelScope.launch {
            getFollowingLive.value = repo.getFollowingFollower(userId, targetUidBody)
        }
    }

    fun getFollowing() = getFollowingLive


    fun retriveFollower(
        targetUidBody: String
    ) {

        viewModelScope.launch {
            getFollowerLive.value = repo.getFollowingFollower(userId, targetUidBody)
        }
    }

    fun getFollower() = getFollowerLive


    fun updateUserProfile(
        first_name: String,
        last_name: String,
        user_name: String,
        email: String,
        phone: String,
        country_phone_id: String,

        sex: String,
        barth_of_date: String,

        ) {
        viewModelScope.launch {
            val authToken = "Bearer $userToken"
            updateUserLive.value = repo.getEditProfile(
                authToken,
                first_name =  first_name,
                last_name = last_name,
                user_name =  user_name,
                email = email,
                phone = phone,
                country_phone_id = country_phone_id,
                //todo check why this doesn't work
                sex = sex,
                barth_of_date = barth_of_date,)


        }
    }

    fun getUpdateUserLive() = updateUserLive

    fun retriveSetAction(
        entityId: String,
        entity_type: String,
        flag_id: String,
    ) {

        viewModelScope.launch {
            setActions.value = repo.setUserActionPost(userId, entityId, entity_type, flag_id)
        }
    }

    fun getSetAction() = setActions


    fun retriveUserVideos(
        state: String, pagesize: String, user_profile_uid: String,
        is_home: String,
        page: String
    ) {

        viewModelScope.launch {
            getUserVideosLive.value =
                repo.getVideosForUser(userId, state, pagesize, user_profile_uid, is_home, page)
        }
    }

    fun getUserVideos() = getUserVideosLive

    fun retriveViewUserProfile () {

        viewModelScope.launch {
            val authToken = "Bearer $userToken"
            viewUserPrfofile.value = repo.getUserProfile(authToken)

            }}

    fun getUserProfile() = viewUserPrfofile

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

    fun retriveGender() {
        viewModelScope.launch {
            genderLive.value = repo.getGender()
        }
    }

    fun getGender() = genderLive


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