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
    topic.topicId = if(topicsLiveData.value == null) 0 else topicsLiveData?.value?.size
    val topicList = topicsLiveData?.value ?: ArrayList()
    topicList?.add(0, topic)
    if(topicList?.isEmpty()) {
      updateEmptyState()
    } else {
      updateContentState()
    }

    topicsLiveData.value = ArrayList(topicList)
    sortList()
  }

  /**
   * sort list descending according to votes
   */
  private fun sortList() {
    val sortedList = topicsLiveData?.value?.sortedWith(compareByDescending({ it.voteCounts}))?.take(20)
    topicsLiveData.value = ArrayList(sortedList)
  }

  /**
   * upvote topic and refresh list
   */
  fun upvoteTopic(topicId: Int) {
    var topic : Topic ?= null
    val iter = topicsLiveData.value?.iterator() ?: return
    while (iter.hasNext()) {
      topic = iter.next()
      if(topic.topicId == topicId) {
        topic.voteCounts++
        topic.isDownvoted = false
        topic.isUpvoted = true
        break
      }
    }

    sortList()
  }

  /**
   * downvote topic and refresh list
   */
  fun downvoteTopic(topicId: Int) {
    var topic : Topic ?= null
    val iter = topicsLiveData.value?.iterator() ?: return
    while (iter.hasNext()) {
      topic = iter.next()
      if(topic.topicId == topicId) {
        topic.voteCounts--
        topic.isDownvoted = true
        topic.isUpvoted = false
        break
      }
    }
    sortList()
  }

}