package com.test.clonereddit.modules.newsfeed.view

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.test.clonereddit.R
import com.test.clonereddit.common.BaseActivity
import com.test.clonereddit.databinding.ActivityMainBinding
import com.test.clonereddit.modules.newsfeed.model.Topic
import com.test.clonereddit.modules.newsfeed.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.floatingActionButton
import kotlinx.android.synthetic.main.activity_main.tbMain
import kotlinx.android.synthetic.main.activity_main.tvToolbarTitle
import kotlinx.android.synthetic.main.content_main.rvTopicList
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Created by anjalisingh
 * Main Activity containing topic recycler view
 */

class MainActivity : BaseActivity(), OnTopicUpdateListener {

  private var mainViewModel : MainViewModel ?= null
  private var binding: ActivityMainBinding? = null
  private var layoutManager: LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
      false)
  private var adapter = TopicAdapter(this)
  private val REQUEST_CODE_CREATE_TOPIC = 101

  override fun getLayoutResource(): Int = R.layout.activity_main

  override fun getHomeUpEnabled(): Boolean = false

  override fun needToolbar(): Boolean = true

  override fun getToolbarText(): String = getString(R.string.app_name)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    binding = DataBindingUtil.setContentView(this, getLayoutResource())
    binding?.mainViewModel = mainViewModel

    initialiseViews()
    observeViewModel()
  }

  /**
   * initialise toolbar, recyclerview
   * set action for topic creation floating button
   */
  private fun initialiseViews() {
    setSupportActionBar(tbMain)
    if (supportActionBar != null) {
      supportActionBar?.setDisplayShowTitleEnabled(false)
      supportActionBar?.title = ""
      tvToolbarTitle?.text = getToolbarText()
    }

    rvTopicList.layoutManager = layoutManager
    rvTopicList.adapter = adapter
    floatingActionButton.setOnClickListener(View.OnClickListener {
      val intent = Intent(this, CreateTopicActivity::class.java)
      startActivityForResult(intent, REQUEST_CODE_CREATE_TOPIC)
    })
  }

  /**
   * observe live data changes
   * update adapter accordingly
   */
  private fun observeViewModel() {
    mainViewModel?.getTopicsLiveData()?.observe(this, Observer<ArrayList<Topic>> { topics ->
      if(topics != null) {
        adapter.setData(topics)
      }
    })
  }
  
  override fun onTopicUpvoted(topicId: Int, position: Int) {
    mainViewModel?.upvoteTopic(topicId)
    adapter.notifyDataSetChanged()
  }

  override fun onTopicDownvoted(topicId: Int, position: Int) {
    mainViewModel?.downvoteTopic(topicId)
    adapter.notifyDataSetChanged()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CREATE_TOPIC) {
      val sdf = SimpleDateFormat("hh:mm a  MMM dd")
      val currentTime = sdf.format(Calendar.getInstance().time)

      data?.getStringExtra("topic")
          ?.let {
            mainViewModel?.addTopic(Topic(it, currentTime))
          }
    } else {
      super.onActivityResult(requestCode, resultCode, data)
    }
  }
}