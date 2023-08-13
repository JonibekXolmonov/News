package bek.droid.news.app

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //Initializing
        Fresco.initialize(this)

        //to remove restrictions while file sharing
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }
}