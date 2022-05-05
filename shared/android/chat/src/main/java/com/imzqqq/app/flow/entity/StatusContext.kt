package com.imzqqq.app.flow.entity

data class StatusContext(
        val ancestors: List<Status>,
        val descendants: List<Status>
)
