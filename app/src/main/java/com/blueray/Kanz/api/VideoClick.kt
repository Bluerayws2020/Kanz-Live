package com.blueray.Kanz.api

import com.blueray.Kanz.model.NewAppendItItems

interface VideoClick {
    fun OnVideoClic(pos:List<NewAppendItItems>, position: Int)
    fun OnVideoClic( position: Int)

}