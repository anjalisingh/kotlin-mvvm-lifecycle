package com.test.clonereddit.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.MenuItem
import com.test.clonereddit.R
import kotlinx.android.synthetic.main.toolbar.tvToolbarTitle

/**
 * Created by anjalisingh
 * Base abstract Activity
 */

abstract class BaseActivity : AppCompatActivity() {

  var toolbar: Toolbar? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(getLayoutResource())

    if (needToolbar()) {
      toolbar = findViewById(R.id.toolbar)
      if (toolbar != null) {
        setSupportActionBar(toolbar)

        if (supportActionBar != null) {
          val mActionBar = supportActionBar
          if (getHomeUpEnabled()) {
            mActionBar!!.setDisplayHomeAsUpEnabled(true)
            mActionBar.setDisplayShowHomeEnabled(true)
          }
          mActionBar?.setDisplayShowTitleEnabled(false)
          mActionBar?.title = ""
          if (!TextUtils.isEmpty(getToolbarText())) {
            tvToolbarTitle.text = getToolbarText()
          }
        }
      }
    }
  }

  protected abstract fun getLayoutResource(): Int

  protected abstract fun getHomeUpEnabled(): Boolean

  protected abstract fun needToolbar(): Boolean

  protected abstract fun getToolbarText(): String

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val id = item.itemId
    if (getHomeUpEnabled() && id == android.R.id.home) {
      onBackPressed()
      return true
    }
    return super.onOptionsItemSelected(item)
  }
}
