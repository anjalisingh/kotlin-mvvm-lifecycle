package com.test.clonereddit

import android.app.Application
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class CloneApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    CalligraphyConfig.initDefault(
        CalligraphyConfig.Builder().setDefaultFontPath(getString(R.string.font_regular))
            .setFontAttrId(R.attr.fontPath)
            .build())
  }
}