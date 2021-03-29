package com.osicorp.carbon_test_app.model

import java.util.*

open class Post : PostId {
    var Title: String? = null
    var Url: String? = null
    var MediaType: Long? = null
    var TimeStamp: Date? = null

    constructor() {}
    constructor(Title: String?, Url: String?, MediaType: Long, TimeStamp: Date?) {
        this.Title = Title
        this.Url = Url
        this.MediaType = MediaType
        this.TimeStamp = TimeStamp
    }
}