package com.imzqqq.app.flow.entity

data class SearchResult(
        val accounts: List<Account>,
        val statuses: List<Status>,
        val hashtags: List<HashTag>
)
