package com.blueray.Kanz.videoliveeventsample.view.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.blueray.Kanz.R
import com.blueray.Kanz.databinding.FragmentLiveEventListBinding
import com.blueray.Kanz.helpers.ViewUtils.hide
import com.blueray.Kanz.helpers.ViewUtils.show
import com.blueray.Kanz.videoliveeventsample.adapter.HostAdapter
import com.sendbird.live.LiveEvent
import com.sendbird.live.LiveEventListQuery
import com.sendbird.live.LiveEventRole
import com.sendbird.live.LiveEventState
import com.sendbird.live.SendbirdLive
import com.sendbird.live.videoliveeventsample.adapter.LiveEventListAdapter
import com.blueray.Kanz.videoliveeventsample.util.INTENT_KEY_CREATED_LIVE_EVENT_ID
import com.blueray.Kanz.videoliveeventsample.util.INTENT_KEY_LIVE_EVENT_ID
import com.blueray.Kanz.videoliveeventsample.util.OnItemClickListener
import com.blueray.Kanz.videoliveeventsample.util.areAnyPermissionsGranted
import com.blueray.Kanz.videoliveeventsample.util.showAlertDialog
import com.blueray.Kanz.videoliveeventsample.util.showListDialog
import com.blueray.Kanz.videoliveeventsample.util.showPermissionDenyDialog
import com.blueray.Kanz.videoliveeventsample.util.showToast
import com.blueray.Kanz.videoliveeventsample.view.CreateLiveEventActivity
import com.blueray.Kanz.videoliveeventsample.view.LiveEventForParticipantActivity
import com.blueray.Kanz.videoliveeventsample.view.LiveEventSetUpActivity
import com.sendbird.live.Host
import com.sendbird.live.LiveEventListener
import com.sendbird.live.ParticipantCountInfo
import com.sendbird.webrtc.SendbirdException
import java.util.UUID


class LiveEventListFragment :
    BaseFragment<FragmentLiveEventListBinding>(FragmentLiveEventListBinding::inflate) {

    private var liveEventListQuery: LiveEventListQuery? = null
    private val params = LiveEventListQuery.Params()
    private val permissions = mutableListOf(
        Manifest.permission.BLUETOOTH
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            addAll(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
                )
            )
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it.all { permission -> permission.value }) {
            requireContext().showToast(R.string.permission_dialog_granted)
        } else {
            requireContext().showPermissionDenyDialog(
                true,
                it.filter { permission -> !permission.value }.keys.toList()
            )
        }
    }
    private var createLiveEventResult : ActivityResultLauncher<Intent>? = null
    lateinit var adapter: LiveEventListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createLiveEventResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val liveEventId = result.data?.getStringExtra(INTENT_KEY_CREATED_LIVE_EVENT_ID) ?: return@registerForActivityResult
                val createdLiveEvent = SendbirdLive.getCachedLiveEvent(liveEventId) ?: return@registerForActivityResult
                adapter.addItems(listOf(createdLiveEvent))
            }
        }
        initLiveEventListView()
        initHeaderView()
    }

    private fun initHeaderView() {
        binding.tvCreateLiveEvent.setOnClickListener {
            createLiveEvent()
        }
    }

    private fun createLiveEvent() {
        val intent = Intent(requireContext(), CreateLiveEventActivity::class.java)
        startActivity(intent)
    }

    private fun initLiveEventListView() {
        adapter = LiveEventListAdapter()
        liveEventListQuery = SendbirdLive.createLiveEventListQuery(params)
        binding.rvLiveEvents.adapter = adapter
        adapter.onItemClickListener = OnItemClickListener { _, position, liveEvent ->
            getLiveEvent(liveEvent.liveEventId) { newLiveEvent ->
                if (position != -1) adapter.notifyItemChanged(position)
                if (newLiveEvent.myRole == LiveEventRole.HOST) {
                    requireActivity().showListDialog(
                        title = getString(R.string.dialog_message_choose_your_role),
                        listItem = listOf(getString(R.string.hosts), getString(R.string.participant))
                    ) { _, position ->
                        val role = if (position == 0) LiveEventRole.HOST else LiveEventRole.PARTICIPANT
                        enterTheLiveEvent(newLiveEvent, role)
                    }
                } else {
                    enterTheLiveEvent(newLiveEvent, LiveEventRole.PARTICIPANT)
                }
            }
        }
        adapter.emptyStateView = binding.clEmpty
        binding.rvLiveEvents.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    loadNext()
                }
            }
        })
        binding.srlLiveEvent.setOnRefreshListener {
            liveEventListQuery = SendbirdLive.createLiveEventListQuery(params)
            loadNext(true)
        }
        loadNext()
    }

    private fun enterAsHost(liveEvent: LiveEvent) {
        val requestPermissions = permissions.toMutableList().apply {
            addAll(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_PHONE_STATE
                )
            )
        }.toTypedArray()
        if (!requireContext().areAnyPermissionsGranted(requestPermissions)) {
            requestPermissionLauncher.launch(requestPermissions)
            return
        }
        val intent = Intent(requireContext(), LiveEventSetUpActivity::class.java)
        intent.putExtra(INTENT_KEY_LIVE_EVENT_ID, liveEvent.liveEventId)
        requireContext().startActivity(intent)
    }

    private fun enterAsParticipant(liveEvent: LiveEvent) {
        when (liveEvent.state) {
            LiveEventState.CREATED -> {
                requireActivity().showAlertDialog(
                    message = getString(R.string.error_message_created_enter_dialog_description),
                    posText = getString(R.string.okay)
                )
                return
            }
            LiveEventState.READY, LiveEventState.ONGOING -> {
                val requestPermissions = permissions.toTypedArray()
                if (!requireContext().areAnyPermissionsGranted(requestPermissions)) {
                    requestPermissionLauncher.launch(requestPermissions)
                    return
                }
                liveEvent.enter { e ->
                    if (e != null) {
                        requireActivity().showToast(e.message ?: "")
                        return@enter
                    }
                    val intent = Intent(requireContext(), LiveEventForParticipantActivity::class.java)
                    intent.putExtra(INTENT_KEY_LIVE_EVENT_ID, liveEvent.liveEventId)
                    requireContext().startActivity(intent)
                }
            }
            else -> {
            }
        }
    }

    private fun enterTheLiveEvent(liveEvent: LiveEvent, role: LiveEventRole) {
        if (liveEvent.state == LiveEventState.ENDED) {
            requireActivity().showAlertDialog(
                message = getString(R.string.error_message_ended_enter_dialog_description),
                posText = getString(R.string.okay)
            )
            return
        }

        if (role == LiveEventRole.HOST) {
            enterAsHost(liveEvent)
        } else {
            enterAsParticipant(liveEvent)
        }
    }

    private fun loadNext(isRefresh: Boolean = false) {
        if (liveEventListQuery?.hasNext == true) {
            liveEventListQuery?.next { list, e ->
                if (isRefresh) {
                    binding.srlLiveEvent.isRefreshing = false
                    binding.rvLiveEvents.scrollToPosition(0)
                }
                if (e != null) {
                    requireContext().showToast(e.message ?: "")
                    return@next
                }
                val filteredList = list?.filter { it.state == LiveEventState.ONGOING } ?: emptyList()
                if (isRefresh) {
                    adapter.submitList(filteredList)
                } else {
                    adapter.addItems(filteredList)
                }
            }
            return
        }
        if (isRefresh) binding.srlLiveEvent.isRefreshing = false
    }


    private fun getLiveEvent(liveEventId: String, callback: (LiveEvent) -> Unit) {
        SendbirdLive.getLiveEvent(liveEventId) getLiveEventLabel@{ liveEvent, e ->
            if (e != null || liveEvent == null) {
                requireContext().showToast(e?.message ?: "")
                return@getLiveEventLabel
            }
            if (liveEvent.state != LiveEventState.ONGOING) {
                // Handle the case when live event is not ongoing, e.g., show a message or perform other actions
                return@getLiveEventLabel
            }
            callback.invoke(liveEvent)
        }
    }
}