package com.blueray.Kanz.model

data class SearchResponse(
    val msg: SearchMsg,
    val results: List<SearchResult>
)