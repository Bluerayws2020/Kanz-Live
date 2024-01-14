package com.blueray.Kanz.hlsdemo.common.meeting.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.blueray.Kanz.adapters.MeetingsAdapter
import com.blueray.Kanz.api.OnMeetingStart
import com.blueray.Kanz.databinding.LiveListBinding
import com.blueray.Kanz.helpers.HelperUtils
import com.blueray.Kanz.hlsdemo.common.meeting.activity.CreateOrJoinActivity
import com.blueray.Kanz.hlsdemo.common.meeting.activity.MainActivitys
import com.blueray.Kanz.hlsdemo.common.utils.NetworkUtils
import com.blueray.Kanz.hlsdemo.common.utils.ResponseListener
import com.blueray.Kanz.model.MeetingItem
import com.blueray.Kanz.model.SessionData

class LiveEventList:Fragment()  {
    private lateinit var adapter: MeetingsAdapter
    private val AUTH_TOKEN: String ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiJhYWZhYTcxMS04MjQxLTQwM2YtYjg2OC1kODQ4NDRhODI4NDIiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTcwNTA4MTczMCwiZXhwIjoxODYyODY5NzMwfQ.9EfhMnAcKLHnfzxoCo120JxA77-1ooiyoLs8Av4vj-w"

    lateinit var binding: LiveListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = LiveListBinding.inflate(layoutInflater)


        binding.recyclerViewMeetings.layoutManager = LinearLayoutManager(context)

        fetchMeetings()
        return binding!!.root




    }
    private fun fetchMeetings() {
        val url = "https://api.videosdk.live/v2/hls"
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val meetingsArray = response.getJSONArray("data")
                val meetingsList = ArrayList<SessionData>()

                Log.d("gjhkl",response.toString())
                for (i in 0 until meetingsArray.length()) {
                    val meetingJson = meetingsArray.getJSONObject(i)

                    val meetingItem = SessionData(
                        apiKey = meetingJson.getString("apiKey"),
                        sessionId = meetingJson.getString("sessionId"),
                        streamKey = meetingJson.getString("streamKey"),
                        mode = meetingJson.getString("mode"),
                        start = meetingJson.getString("start"),
                        end = meetingJson.getString("end"),
                        deleted = meetingJson.getBoolean("deleted"),
                        quality = meetingJson.getString("quality"),
                        orientation = meetingJson.getString("orientation"),
                        createdAt = meetingJson.getString("createdAt"),
                        roomId = meetingJson.getString("roomId"),
                        downstreamUrl = meetingJson.getString("downstreamUrl"),
                        playbackHlsUrl = meetingJson.getString("playbackHlsUrl"),
                        livestreamUrl = meetingJson.getString("livestreamUrl"),
                        id = meetingJson.getString("id")

                    )
                    meetingsList.add(meetingItem)
                }


                adapter = MeetingsAdapter(listOf(),object:OnMeetingStart{
                    override fun onMeetingGo(pos: Int) {


                        val networkUtils = NetworkUtils(requireContext())
                        if (networkUtils.isNetworkAvailable) {
                            networkUtils.getToken(object : ResponseListener<String?> {
                                override fun onResponse(token: String?) {
                                    networkUtils.joinMeeting(
                                        token,
                                        meetingsList[pos].roomId,
                                        object : ResponseListener<String?> {
                                            override fun onResponse(meetingId: String?) {
                                                val intent = Intent(
requireContext(),
                                                    MainActivitys::class.java
                                                )

                                                intent.putExtra("token", token)
                                                intent.putExtra("meetingId", meetingId)

                                                intent.putExtra(
                                                    "participantName",
                                                    HelperUtils.getUserName(requireContext()))
                                                intent.putExtra("mode", "VIEWER")
                                                startActivity(intent)
                                            }
                                        })
                                }
                            })
                        } else {
                            Toast.makeText(requireContext(),"No Internet Connection", Toast.LENGTH_LONG).show()
                        }






                    }

                })
                binding.recyclerViewMeetings.adapter  = adapter
                adapter.updateData(meetingsList)



            },
            { error ->
                // Handle error
            }

        ){
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = AUTH_TOKEN // Replace with your actual token
                // Add any other headers here
                return headers
            }        }



        Volley.newRequestQueue(context).add(jsonObjectRequest)
    }



}