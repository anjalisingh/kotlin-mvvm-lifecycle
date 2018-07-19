package com.test.clonereddit.modules.newsfeed.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.test.clonereddit.R
import com.test.clonereddit.common.BaseActivity
import kotlinx.android.synthetic.main.activity_create_topic.etCreateTopic
import kotlinx.android.synthetic.main.activity_create_topic.tvPostTopic

class CreateTopicActivity : BaseActivity(), TextWatcher {

  override fun getLayoutResource(): Int = R.layout.activity_create_topic

  override fun getHomeUpEnabled(): Boolean = true

  override fun needToolbar(): Boolean = true

  override fun getToolbarText(): String = getString(R.string.add_a_topic)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_clear)
    etCreateTopic.addTextChangedListener(this)
    tvPostTopic.setOnClickListener({
      val intent = Intent()
      intent.putExtra("topic", etCreateTopic.editableText.toString())
      setResult(Activity.RESULT_OK, intent)
      finish()
    })
  }

  override fun afterTextChanged(s: Editable?) {}

  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    tvPostTopic.isEnabled = s?.length!! > 0
  }
}