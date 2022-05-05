package org.libre.agosto.p2play.models

class UserModel (
        var id: Int = 0,
        var uuid: Int = 0,
        var username: String = "",
        var email: String = "",
        var nsfw: Boolean = true,
        var followers: Int = 0,
        var avatar: String = "",
        var status: Int = -1
)