package com.test.clonereddit.modules.newsfeed.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableInt
import android.view.View
import com.test.clonereddit.modules.newsfeed.model.Topic

class MainViewModel : ViewModel() {

  private var topicList = ArrayList<Topic>()
  var emptyStateVisibility : ObservableInt = ObservableInt(View.VISIBLE)
  var contentVisibility : ObservableInt = ObservableInt(View.GONE)
  private val topicsLiveData = MutableLiveData<ArrayList<Topic>>()

  init {
    contentVisibility.set(View.GONE)
    emptyStateVisibility.set(View.VISIBLE)
  }

  fun getTopicsLiveData() : LiveData<ArrayList<Topic>>? {
    return topicsLiveData
  }

  fun updateEmptyState() {
    contentVisibility.set(View.GONE)
    emptyStateVisibility.set(View.VISIBLE)
  }

  fun updateContentState() {
    contentVisibility.set(View.VISIBLE)
    emptyStateVisibility.set(View.GONE)
  }

  fun addTopic(topic: Topic) {
    topic.topicId = topicList.size
    topicList.add(0, topic)
    if(topicList.isEmpty()) {
      updateEmptyState()
    } else {
      updateContentState()
    }
    sortList()
  }

  private fun sortList() {
    val sortedList = topicList.sortedWith(compareByDescending({ it.voteCounts})).take(20)
    topicsLiveData.value = ArrayList(sortedList)
  }

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