package com.test.clonereddit.modules.newsfeed.view

import android.support.v7.util.DiffUtil
import com.test.clonereddit.modules.newsfeed.model.Topic

class TopicDiffUtil(private val oldList: ArrayList<Topic>,
    private val newList: ArrayList<Topic>) : DiffUtil.Callback() {

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return oldList[oldItemPosition]?.topicId == newList[newItemPosition]?.topicId
  }

  override fun getOldListSize(): Int {
    return oldList.size
  }

  override fun getNewListSize(): Int {
    return newList.size
  }

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return oldList[oldItemPosition] == newList[newItemPosition]
  }
}