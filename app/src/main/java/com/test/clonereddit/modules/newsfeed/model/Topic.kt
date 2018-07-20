package com.test.clonereddit.modules.newsfeed.model

/**
 * Created by anjalisingh
 * Topic Model
 */

data class Topic(
    var title : String?,
    var formattedTime : String,
    var voteCounts : Int = 0,
    var isUpvoted : Boolean = false,
    var isDownvoted : Boolean = false,
    var topicId : Int? = 0
)
