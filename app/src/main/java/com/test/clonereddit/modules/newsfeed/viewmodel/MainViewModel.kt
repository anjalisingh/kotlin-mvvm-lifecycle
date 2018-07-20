package com.test.clonereddit.modules.newsfeed.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableInt
import android.view.View
import com.test.clonereddit.modules.newsfeed.model.Topic

/**
 * Created by anjalisingh
 * View Model for topic list
 * Uses Live Data model
 */

class MainViewModel : ViewModel() {

  private var topicList = ArrayList<Topic>()
  var emptyStateVisibility : ObservableInt = ObservableInt(View.VISIBLE)
  var contentVisibility : ObservableInt = ObservableInt(View.GONE)
  private val topicsLiveData = MutableLiveData<ArrayList<Topic>>()

  /**
   * initialise default state
   */
  init {
    contentVisibility.set(View.GONE)
    emptyStateVisibility.set(View.VISIBLE)
  }

  /**
   * return live data
   */
  fun getTopicsLiveData() : LiveData<ArrayList<Topic>>? {
    return topicsLiveData
  }

  /**
   * show empty state when no topics
   */
  fun updateEmptyState() {
    contentVisibility.set(View.GONE)
    emptyStateVisibility.set(View.VISIBLE)
  }

  /**
   * disable empty state when topics exist
   */
  fun updateContentState() {
    contentVisibility.set(View.VISIBLE)
    emptyStateVisibility.set(View.GONE)
  }

  /**
   * add topic to top of list
   */
  fun addTopic(topic: Topic) {
    topic.topicId = topicsLiveData.value?.size
    topicList.add(0, topic)
    if(topicList.isEmpty()) {
      updateEmptyState()
    } else {
      updateContentState()
    }
    sortList()
  }

  /**
   * sort list descending according to votes
   */
  private fun sortList() {
    val sortedList = topicList.sortedWith(compareByDescending({ it.voteCounts})).take(20)
    topicsLiveData.value = ArrayList(sortedList)
  }

  /**
   * upvote topic and refresh list
   */
  fun upvoteTopic(topicId: Int) {
    var topic : Topic ?= null
    for(item in topicList) {
      if(item.topicId == topicId) {
        topic = item
        break
      }
    }
    if(topic == null) return

    topic.voteCounts++
    topic.isUpvoted = true
    topic?.isDownvoted = false
    sortList()
  }

  /**
   * downvote topic and refresh list
   */
  fun downvoteTopic(topicId: Int) {
    var topic : Topic ?= null
    for(item in topicList) {
      if(item.topicId == topicId) {
        topic = item
        break
      }
    }
    if(topic == null) return

    topic.voteCounts--
    topic.isDownvoted = true
    topic.isUpvoted = false
    sortList()
  }

}