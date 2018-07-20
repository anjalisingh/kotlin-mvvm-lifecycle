package com.test.clonereddit.modules.newsfeed.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import com.android.databinding.library.baseAdapters.BR
import com.test.clonereddit.R
import com.test.clonereddit.databinding.TopicItemRowBinding
import com.test.clonereddit.modules.newsfeed.model.Topic

/**
 * Created by anjalisingh
 * Topic Adapter with single view type
 * Uses databinbinding for adapter items
 */

class TopicAdapter(private val listener : OnTopicUpdateListener) : RecyclerView.Adapter<ViewHolder>() {
  private var items = ArrayList<Topic>()

  /**
   * set topics using DiffUtil for comparing
   */
  fun setData(topics: ArrayList<Topic>) {
    val diffResult = DiffUtil.calculateDiff(
        TopicDiffUtil(this.items, topics))
    this.items.clear()
    this.items.addAll(topics)
    diffResult.dispatchUpdatesTo(this)
  }

  fun addData(topic: Topic) {
    this.items.add(topic)
    notifyItemInserted(items.count())
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val itemBinding = DataBindingUtil.inflate<TopicItemRowBinding>(layoutInflater,
        R.layout.topic_item_row, parent, false)
    val vh = TopicViewHolder(itemBinding)
    vh.bindClickListener(OnVoteListener(vh, listener))

    return vh
  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    if (holder is TopicViewHolder) {
      val topic = get(position)
      holder.bindData(topic)
    }
  }

  fun get(position: Int): Topic? {
    return items[position]
  }

  /**
   * View Holder for topic
   */
  inner class TopicViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(
      binding.root) {
    private val context: Context = binding.root.context
    val ivUpvote : Button
    val ivDownvote : Button

    init {
      val view = binding.root.rootView
      ivUpvote = view.findViewById(R.id.ivUp)
      ivDownvote = view.findViewById(R.id.ivDown)
    }

    /**
     * Bind data to each holder
     */
    fun bindData(topic: Topic?) {
      binding?.root?.tag = topic?.topicId
      binding.setVariable(BR.topic, topic)
      binding.executePendingBindings()
      ivDownvote?.isSelected
      topic?.isUpvoted?.let {
        ivUpvote?.isSelected = it
      }
      topic?.isDownvoted?.let {
        ivDownvote?.isSelected = it
      }
    }

    /**
     * Bind handler for upvote and downvote
     */
    fun bindClickListener(handler : OnVoteListener) {
      ivUpvote?.setOnClickListener(handler)
      ivDownvote?.setOnClickListener(handler)
    }
  }

  /**
   * Listener for upvote and downvote
   */
  class OnVoteListener(private val holder : TopicViewHolder, private val listener : OnTopicUpdateListener) : OnClickListener {
    private var vh: TopicViewHolder = holder
    private var position: Int = 0

    override fun onClick(v: View?) {
      position = vh.adapterPosition
      if (position == -1) return
      val topicId: Int = vh.itemView?.tag as Int

      if(v?.id == vh.ivUpvote.id) {
        listener?.onTopicUpvoted(topicId, position)
      } else if(v?.id == vh.ivDownvote.id) {
        listener?.onTopicDownvoted(topicId, position)
      }
    }
  }
}

interface OnTopicUpdateListener {
  fun onTopicUpvoted(topicId: Int, position: Int)

  fun onTopicDownvoted(topicId: Int, position: Int)
}
