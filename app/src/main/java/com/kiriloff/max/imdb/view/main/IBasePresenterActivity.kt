package com.kiriloff.max.imdb.view.main

import android.os.Bundle

interface IBasePresenterActivity<View : IBaseView> {

    fun attach(view: View)

    fun detach()

    fun onCreate(bundle: Bundle?)

    fun onStart()

    fun onRestart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onDestroy()
}