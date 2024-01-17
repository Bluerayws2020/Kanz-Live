package com.blueray.Kanz.model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.io.Serializable

sealed class NetworkResults<out R> {
    data class Success<out T>(val data: T) : NetworkResults<T>()
    data class Error(val exception: Exception) : NetworkResults<Nothing>()
    data class NoInternet(val exception: String) : NetworkResults<Nothing>()
    // can be added
//    data class Loading<T>(val data: T? = null) : NetworkResults<T>()

}

data class UserLoginModel(
    @SerializedName("msg") val status: MessageModel,

    @SerializedName("results") val datas: LoginModel

)


//data class ViewUserLoginModel(
//    @SerializedName("uid") val uid: String,
//    @SerializedName("role") val role: String,
//    @SerializedName("mail") val mail: String,
//    @SerializedName("username") val username: String,
//    @SerializedName("user_picture") val user_picture: String,
//    @SerializedName("phone_number") val phone_number: String,
//    @SerializedName("profile_data") val profile_data: Pprofile_data,
//    @SerializedName("auther") val autherFoloower : AuthresFolow,
//
//)

data class GetProfileResponse(
    val msg: Msg,
    val results: Results
)

data class Results(
    val bio: Any,
    val country_phone_id: String,
    val date_of_birth: String,
    val email: String,
    val first_name: String,
    val followers_count: String,
    val following_count: String,
    val id: Int,
    val last_name: String,
    val likes_count: String,
    val live_status: String,
    val my_videos: List<MyVideo>,
    val my_videos_count: Int,
    val my_videos_save: Any,
    val phone: String,
    val profile_image: Any,
    val sex: String,
    val token: String,
    val upload_video_status: String,
    val user_name: String
)

data class Msg(
    val message: String,
    val status: Int
)

data class MyVideo(
    val comments_data: List<Any>,
    val description: String,
    val hashtagTitles: List<String>,
    val hashtag_ids: List<String>,
    val id: Int,
    val number_of_comments: String,
    val number_of_likes: String,
    val number_of_save: String,
    val number_of_share: Int,
    val share_count: String,
    val video: String,
    val vimeo_id: Int,
    val vimeo_transcode_status: String
)

data class UserUploadeDone(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val datas: NidVideoUplaode

)



data class NidVideoUplaode(
    val nid: String
)
//this is the old register

//data class  RgetrationModel(
//    @SerializedName("msg") val status: MessageModel,
//@SerializedName("poet_data") val data: RigsterModel,
//)
//data class RigsterModel(
//    @SerializedName("uid") val uid: String,
//
//    @SerializedName("type") val type: String,
//    @SerializedName("phone_number") val phone_number: String,
//    @SerializedName("user_name") val user_name: String,
//    @SerializedName("email") val email: String,
//    @SerializedName("user_picture") val user_picture: String,
//
//    )

//this is the new one

data class RgetrationModel(
    val msg: MessageModel,
    val data: String,
    @SerializedName("results") val results: LoginModel
)
//todo make a response for the results


data class RegisterModel(
    val first_name: String,
    val last_name: String,
    val user_name: String,
    val date_of_birth: String,
    val sex: Int,
    val hashtags_ids: MutableList<String>,
    val password: String,
    val country_phone_id: Int,
    val phone: String,
    val email: String
)

data class LoginModel(
    @SerializedName("id") val id: String,
    @SerializedName("uid") val uid: String,
    @SerializedName("token") val token: String,
    @SerializedName("first_name") val first_name: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("user_name") val user_name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("country_phone_id") val country_phone_id: String,
    @SerializedName("date_of_birth") val date_of_birth: String,
    @SerializedName("email_verified_at") val email_verified_at: String,
    @SerializedName("profile_image") val profile_image: String,
    @SerializedName("login_way") val login_way: String,
    @SerializedName("sex") val sex: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("live_status") val live_status: String,
    @SerializedName("upload_video_status") val upload_video_status: String,


    )


data class FollowingResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val msg: String,
    //  @SerializedName("data") val data: List<FollowingList>,
    @SerializedName("result") val result: FollowType,

    )

data class FollowersFollowingResult(
    val followers: List<FollowingList>,
    val following: List<FollowingList>
)

data class MainJsonFollowersFollowingData(
    val msg: UserActionMessage,
    val results: FollowersFollowingResult
)

data class FollowType(
    @SerializedName("followers") val followers: List<FollowingList>,
    @SerializedName("following") val following: List<FollowingList>,

    )

data class FollowingList(
    @SerializedName("id") val uid: String,
    @SerializedName("user_name") val user_name: String,
    @SerializedName("profile_image") val picture: String,
    @SerializedName("flag") var flag: Int,
    @SerializedName("is_following") var is_following: String,

    )

data class MessageModel(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val msg: String,
    @SerializedName("msg") var msgs: Int

)

//data class MessageModelData(
//    @SerializedName("msg") val status: MessageModel,
//
//)
data class UpdateProfileResponse(
    val msg: Msg,
    val results: UpdateResults
)

data class UpdateResults(
    val bio: Any,
    val country_phone_id: String,
    val date_of_birth: String,
    val email: String,
    val first_name: String,
    val followers_count: String,
    val following_count: String,
    val id: Int,
    val last_name: String,
    val likes_count: String,
    val live_status: String,
    val my_videos: Any,
    val my_videos_count: Int,
    val my_videos_save: Any,
    val phone: String,
    val profile_image: Any,
    val sex: String,
    val token: String,
    val upload_video_status: String,
    val user_name: String
)

data class UserActionMessage(
    @SerializedName("msg") val msg: Int,
    @SerializedName("message") val message: String
)

data class UserActionMessageModel(
    @SerializedName("msg") val msg: UserActionMessage
)


data class Item(
    val id: String,
    val title: String,
    val file: String,
    val created: String,
    val uuid: String,
    val token: String,
    val vimeo_detials: VimeoVideoModelV2
)


data class DropDownModel(
    val id: String,
    val hashtag: String,
)

data class CountriesDropDownModel(
    val id: String,
    val name: String,
)

data class MainJsonDropDownModel(
    val msg: MessageModel,
    val results: List<CountriesDropDownModel>
)

data class MainJsonDropDownModelHashTag(
    val msg: MessageModel,
    val results: List<DropDownModel>
)


data class VimeoVideoModelV2(

    @SerializedName("uri") val uri: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("type") val type: String,
    @SerializedName("link") val link: String,
    @SerializedName("player_embed_url") val player_embed_url: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("width") val width: Int,
    @SerializedName("language") val language: String,
    @SerializedName("height") val height: Int,
    @SerializedName("created_time") val created_time: String,
    @SerializedName("modified_time") val modified_time: String,
    @SerializedName("release_time") val release_time: String,
    @SerializedName("files") val files: List<VimeoFileModel>,
    @SerializedName("status") val status: String,
    @SerializedName("is_playable") val is_playable: Boolean,
    @SerializedName("has_audio") val has_audio: Boolean
)


data class NotfiMain(
    @SerializedName("data") val datass: List<NofiItem>,

    )

data class NofiItem(
    @SerializedName("title") val title: String,
    @SerializedName("body") val body: String,
    @SerializedName("entity_id") val entity_id: String? = null,
    @SerializedName("flag_id") val flag_id: String? = null,
    @SerializedName("created") val created: String? = null,


    )


data class VimeoFileModel(

    @SerializedName("rendition") val rendition: String,
    @SerializedName("height") val height: Int,
    @SerializedName("link") val link: String? = null,

    ) {
    override fun toString(): String {
        return rendition
    }
}

data class NewAppendItItems(

    val videoTitle: String,
    val videoDesc: String,
    val date: String,

    val videoUrl: String,
    val userId: String,
    val userName: String,
    val duration: Int,
    val imageThum: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val bandNam: String = "",
    val type: String = "",
    val userPic: String = "",
    val status: String = "",
    var favorites: String = "",
    var userSave: String = "",
    var target_user: TargetUsers? = null,
    var video_counts: VideoCounts? = null,

    val numOfFollowers: Int = 0,
    val numOfFollowing: Int = 0,
    val numOfLikes: Int = 0,
    val nodeId: String = "",


    ) : Serializable
//    {
//        // Toggle the like status
//        fun toggleLike() {
//            favorites = if (favorites == "1") "0" else "1"
//        }
//
//        // Toggle the save status
//        fun toggleSave() {
//            userSave = if (userSave == "1") "0" else "1"
//        }
//    }


data class VideoDataModel(
    @SerializedName("data") val datass: List<VideoResponse>,
    @SerializedName("target_user") val target_user: TargetUsers? = null,


    )

data class SearchDataModel(
    @SerializedName("data") val datass: List<SarchItem>,


    )

data class checkUserFollowData(
    @SerializedName("data") val datass: CheckUserFollow,


    )

data class CheckUserFollow(
    @SerializedName("im_follow_him") val im_follow_him: String,
    @SerializedName("he_follow_me") val he_follow_me: String,
    @SerializedName("this_is_me") val this_is_me: String,

    )

data class SarchItem(
    @SerializedName("uid") val uid: String,
    @SerializedName("user_name") val user_name: String,
    @SerializedName("picture") val picture: String,

    @SerializedName("profile_data") val profile_data: Pprofile_data,

    @SerializedName("auther") val autherFoloower: AuthresFolow,


    )

data class AuthresFolow(

    val numOfFollowers: Int,
    val numOfFollowing: Int,
    val numOfLikes: Int,
)
//data class VideoResponse(
//    val id: String,
//    val title: String,
//    val created: String,
//    val file: String,
//    val uuid: String,
//    val token: String,
//    val moderation_state :String,
//    val vimeo_detials: VimeoDetails,
//    val auther: Author,
//val video_actions_per_user:Video_actions_per_user,
//    val video_counts:VideoCounts? = null
//
//)

data class VideoResponse(
    val id: String,
    val title: String,
    val created_at: String,
    val file: String,
    val moderation_state: String,
    val vimeo_detials: VimeoDetails,
    val auther: Author,
    val video_actions_per_user: Video_actions_per_user,
    val video_counts: VideoCounts? = null

)

data class VideoCounts(

    var like_count: Int,
    val save_count: Int,


    )

data class TargetUsers(
    val im_in_user_profile: Int,
    val target_user_follow_flag: Int,
    val is_blocked: Int,
    val im_blocked: Int,

    )

data class Video_actions_per_user(
    val save: String,
    @SerializedName("like") val favorites: Boolean,

    )

data class Pictures(
    val base_link: String? = ""
)

data class VimeoDetails(
    val uri: String,
    val name: String,
    val description: String,
    val type: String,
    val link: String,
    val player_embed_url: String,
    val duration: Int,
    val width: Int,
    val language: String,
    val height: Int,
    val files: List<VideoFile>,


    val transcode: Transcode? = null,
    val pictures: Pictures? = null
    // Add other fields as necessary...
)

data class Transcode(
    val status: String
)

data class VideoFile(

    val quality: String,
    val rendition: String,
    val type: String,
    val width: Int,
    val height: Int,
    val link: String,
    // Add other fields as necessary...
)

data class Author(
    val uid: String,
    val username: String,
    val type: String,

    val numOfFollowers: Int,
    val numOfFollowing: Int,
    val numOfLikes: Int,


    val profile_data: Pprofile_data
)

data class Pprofile_data(
    val first_name: String,
    val last_name: String,
    val user_picture: String,
    val band_name: String,
    val mail: String,
    val birth_date: String? = null,
    @SerializedName("gender") val gender: String?

)


data class MeetingItem(
    val autoCloseConfig: JSONObject,
    val apiKey: String,
    val disabled: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val roomId: String,
    val links: JSONObject,
    val id: String
)

data class AutoCloseConfig(
    val type: String

)


data class Links(
    val get_room: String,
    val get_session: String

)

data class Grid(
    val image: Int,
    val sounds: Int,

    val name: String,
    val price: String
)

data class ApiResponse(
    val pageInfo: PageInfo,
    val data: List<SessionData>
)

data class PageInfo(
    val currentPage: Int,
    val perPage: Int,
    val lastPage: Int,
    val total: Int
)

data class SessionData(
    val apiKey: String,
    val sessionId: String,
    val streamKey: String,
    val mode: String,
    val start: String,
    val end: String?,
    val deleted: Boolean,
    val quality: String,
    val orientation: String,
    val createdAt: String,
    val roomId: String,
    val downstreamUrl: String,
    val playbackHlsUrl: String,
    val livestreamUrl: String,
    val id: String
)

data class Template(
    val url: String,
    val config: Config,
    val isCustom: Boolean
)

data class Config(
    val layout: Layout,
    val theme: String
)

data class Layout(
    val type: String,
    val priority: String,
    val gridSize: Int
)

data class Webhook(
    val totalCount: Int,
    val successCount: Int,
    val data: List<Any>
)

data class Linkss(
    val get_room: String,
    val get_session: String
)

