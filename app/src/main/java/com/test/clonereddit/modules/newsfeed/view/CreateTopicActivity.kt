package com.test.clonereddit.modules.newsfeed.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.jakewharton.rxbinding.widget.RxTextView
import com.test.clonereddit.R
import com.test.clonereddit.common.BaseActivity
import kotlinx.android.synthetic.main.activity_create_topic.etCreateTopic
import kotlinx.android.synthetic.main.activity_create_topic.tvPostTopic
import rx.android.schedulers.AndroidSchedulers

/**
 * Created by anjalisingh
 * Topic Creation Activity
 */

class CreateTopicActivity : BaseActivity() {

  override fun getLayoutResource(): Int = R.layout.activity_create_topic

  override fun getHomeUpEnabled(): Boolean = true

  override fun needToolbar(): Boolean = true

  override fun getToolbarText(): String = getString(R.string.add_a_topic)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_clear)
    observeTopicChanges()

    tvPostTopic.setOnClickListener({
      val intent = Intent()
      intent.putExtra("topic", etCreateTopic.editableText.toString())
      setResult(Activity.RESULT_OK, intent)
      finish()
    })

  }

  /**
   * Observer topic text changes.
   * enable posting only when valid topic
   */
  private fun observeTopicChanges() {
    RxTextView.textChanges(etCreateTopic)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ charSequence ->
          tvPostTopic.isEnabled = charSequence?.length!! > 0
        })
  }
}