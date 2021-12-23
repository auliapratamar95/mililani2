package com.strategies360.mililani2

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Pair
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.ashokvarma.gander.Gander
import com.ashokvarma.gander.persistence.GanderPersistence
import com.crashlytics.android.Crashlytics
import com.onesignal.OneSignal
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.account.AccountConstant
import com.strategies360.mililani2.activity.SampleAuthActivity
import com.strategies360.mililani2.activity.core.UncaughtExceptionActivity
import io.fabric.sdk.android.Fabric
import itsmagic.present.simpleaccountmanager.AccountHelper
import java.util.concurrent.TimeUnit

/**
 *
 * The [Application] class,
 * important initializations should take place here.
 */
class App : Application() {
    private val ONESIGNAL_APP_ID = "96dfcb3d-199e-4dff-9f0f-65df16c0c2c4"

    override fun onCreate() {
        super.onCreate()
        context = this

        // Initialize Fabric
        Fabric.with(this, Crashlytics())

        // Initialize Gander
        Gander.setGanderStorage(GanderPersistence.getInstance(this))

        // Use custom Activity on fatal errors
        CaocConfig.Builder.create()
                .errorActivity(UncaughtExceptionActivity::class.java)
                .apply()

        // Initialize the account helper
        AccountHelper.init(AccountConstant.ACCOUNT_TYPE, SampleAuthActivity::class.java,
                Pair(AccountConstant.ACCOUNT_PROVIDER_PROFILE, TimeUnit.DAYS.toMillis(7)))

        //Hawk
        Hawk.init(this).build()

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)

        // OneSignal Initialization
        OneSignal.initWithContext(this)

    }

    companion object {

        /**
         * The application [Context] made static.
         * Do **NOT** use this as the context for a view,
         * this is mostly used to simplify calling of resources
         * (esp. String resources) outside activities.
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }
}
