package com.blueray.Kanz.videoliveeventsample.view

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.blueray.Kanz.R
import com.blueray.Kanz.databinding.ActivityUserListBinding
import com.sendbird.live.SendbirdLive
import com.blueray.Kanz.videoliveeventsample.adapter.UserIdsForHostListAdapter
import com.blueray.Kanz.videoliveeventsample.model.SelectedHostUser
import com.blueray.Kanz.videoliveeventsample.util.INTENT_KEY_SELECTED_HOST_USER_LIST
import com.blueray.Kanz.videoliveeventsample.util.showEditTextDialog
import com.blueray.Kanz.videoliveeventsample.util.showListDialog
import com.blueray.Kanz.videoliveeventsample.util.showToast
import com.blueray.Kanz.videoliveeventsample.util.signOut

class UserIdsForHostListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserListBinding
    private lateinit var adapter: UserIdsForHostListAdapter
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finishActivity()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        initView()
    }

    private fun initView() {
        val currentUser = SendbirdLive.currentUser
        if (currentUser == null) {
            signOut()
            return
        }
        adapter = UserIdsForHostListAdapter {
            showListDialog(
                title = if (it.nickname.isNotBlank()) it.nickname else it.userId,
                listItem = listOf(getString(R.string.remove)),
                onClickListener = { _, position ->
                    adapter.removeItems(listOf(it))
                    updateAddButton()
                }
            )
        }.apply {
            val selectedHostUsers = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableArrayListExtra(INTENT_KEY_SELECTED_HOST_USER_LIST, SelectedHostUser::class.java)
            } else {
                intent.getParcelableArrayListExtra(INTENT_KEY_SELECTED_HOST_USER_LIST)
            }?.toMutableList() ?: mutableListOf()
            addItems(selectedHostUsers)
        }
        binding.rvUserList.adapter = adapter
        binding.ivBack.setOnClickListener { finishActivity() }
        binding.ivAdd.setOnClickListener {
            showEditTextDialog(
                title = getString(R.string.dialog_message_add_user),
                posText = getString(R.string.add),
                negText = getString(R.string.cancel),
                positiveButtonFunction = {
                    if (it.isEmpty()) {
                        showToast("error")
                    } else {
                        if (adapter.selectedHostUsers.size >= 10) {
                            showToast(R.string.max_host_count)
                        } else {
                            adapter.addItems(listOf(SelectedHostUser(it, "", null)))
                        }
                        updateAddButton()
                    }
                },
                hintText = getString(R.string.dialog_message_enter_user_id),
                backgroundDrawableResId = R.style.AppTheme
            )
        }
    }

    private fun updateAddButton() {
        val count = adapter.selectedHostUsers.size
        val isEnabled = count < 10
        val tint = if (isEnabled) R.color.primary_200 else R.color.ondark_04
        binding.ivAdd.isEnabled = isEnabled
        ImageViewCompat.setImageTintList(binding.ivAdd, ColorStateList.valueOf(ContextCompat.getColor(this, tint)))
    }

    private fun finishActivity() {
        val intent = Intent().putParcelableArrayListExtra(
            INTENT_KEY_SELECTED_HOST_USER_LIST,
            ArrayList(adapter.selectedHostUsers)
        )
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}