package com.strategies360.mililani2.activity.authentication

import ScreenSize
import android.animation.ValueAnimator
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.LinearLayout.LayoutParams
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.flaviofaria.kenburnsview.KenBurnsView
import com.flaviofaria.kenburnsview.Transition
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.anim
import com.strategies360.mililani2.R.color
import com.strategies360.mililani2.activity.BottomMenuNavigationActivity
import com.strategies360.mililani2.activity.LoginPhoneNumberActivity
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.Config
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.view.BackgroundTransitionGenerator
import kotlinx.android.synthetic.main.activity_splash_screen.*

/**
 *
 * Sample splash screen activity.
 * This splash screen already handles its lifecycle, meaning that the
 * pending intent doesn't launch even after user has closed the app.
 */
@RequiresApi(VERSION_CODES.LOLLIPOP)
class SplashScreenActivity : CoreActivity() {

  private var f: FragmentActivity? = null
  private var screenCalc = 0
  private var fromTop = 0
  private var toBottom = 0

  private val mparcel: Map<*, *>? = null

  private var layoutParamsTitleApps: LayoutParams? = null

  private var layoutParamsBottom: RelativeLayout.LayoutParams? = null

  override val viewRes = R.layout.activity_splash_screen

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      f = this
      onSplashScreenReady()
    }
  }

  /** Called when app is ready to continue with its splash screen  */
  private fun onSplashScreenReady() {
    // Check for storage space before continuing
    // Low-end devices should have at least 64MB of free space, otherwise 128MB
    val storageThreshold = if (Config.isLowRamDevice()) 64 else 128
    if (Config.freeInternalSpaceInMb < storageThreshold)
      showStorageSpaceWarningDialog()
    else
      initSplashScreen()
  }

  private fun initSplashScreen() {
    initAnimation()

    if (Hawk.contains(Constant.KEY_IMAGE_SPLASH)) {
      val indexImage : Boolean = Hawk.get((Constant.KEY_IMAGE_SPLASH))
      if (indexImage) {
        Hawk.put((Constant.KEY_IMAGE_SPLASH), false)
        bg_screen.setImageResource(R.drawable.img_splash_mililani_one)
      } else {
        Hawk.put((Constant.KEY_IMAGE_SPLASH), true)
        bg_screen.setImageResource(R.drawable.ic_screen_effect)
      }
    } else {
      Hawk.put((Constant.KEY_IMAGE_SPLASH), true)
    }
  }

  /**
   * Display an [AlertDialog].
   * The dialog contains a warning message about storage space.
   * When the dialog is dismissed, the app will continue.
   */
  private fun showStorageSpaceWarningDialog() {
    let {
      val builder = AlertDialog.Builder(it, R.style.AppTheme_Dialog_Alert)
      builder.setTitle(resources.getString(R.string.info_warning))
      builder.setMessage(resources.getString(R.string.warning_storage_space))
      builder.setPositiveButton(
          resources.getString(android.R.string.ok)
      ) { dialog, _ -> dialog.dismiss() }
      builder.setOnDismissListener { initSplashScreen() }
      builder.show()
    }
  }

  private fun initAnimation() {
    screenCalc = ScreenSize.instance(f!!)!!.height / 14

    val screen_width: Int = ScreenSize.instance(f!!)!!.width
    val screen_height: Int = ScreenSize.instance(f!!)!!.height
    val rasio: String? = Common.ratio(screen_width, screen_height)

    fromTop = screenCalc * 1
    toBottom = screenCalc * 3
    if (rasio.equals("16:9", ignoreCase = true)) toBottom = screenCalc * 2
    layoutParamsTitleApps = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    layoutParamsTitleApps!!.setMargins(0, fromTop, 0, 0) ////l,t,r,b

    layoutParamsTitleApps!!.gravity = Gravity.CENTER_HORIZONTAL
    logo_mililani.layoutParams = layoutParamsTitleApps
    logo_mililani.requestLayout()

    layoutParamsBottom = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
    )
    layoutParamsBottom!!.setMargins(0, 0, 0, ScreenSize.instance(f!!)!!.height / 14) ////l,t,r,b

    layoutParamsBottom!!.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
    layout_send_otp.layoutParams = layoutParamsBottom
    layout_send_otp.requestLayout()

    main_layout.visibility = View.GONE

    val durAnimKenBurnEffect: Int
    durAnimKenBurnEffect = if (null == mparcel) {
      //show title before animate background transmition end
      Handler(Looper.getMainLooper()).postDelayed({
        runOnUiThread(
            Runnable { //smoothAnimateMarginChange(ScreenSize.instance(f).getHeight()/2, PixelCalc.DpToPixel(getResources().getDimension(R.dimen.Register_Margin_TopToLogo), f), 800, 0);
              logo_mililani.visibility = View.VISIBLE
              val animation = AnimationUtils.loadAnimation(f, anim.smooth_fadein)
              logo_mililani.startAnimation(animation)
            })
      }, 1000)
      2000
    } else {
      logo_mililani.visibility = View.VISIBLE
      main_layout.visibility = View.VISIBLE
      smoothAnimateMarginChange(fromTop, toBottom, 0, 0)
      changeColorAnimation(0)
      0
    }

    val na = false

    if (!na) {
      bg_screen.setTransitionListener(onTransittionListener())
      val ACCELERATE_DECELERATE = AccelerateDecelerateInterpolator()
      val generator =
        BackgroundTransitionGenerator(durAnimKenBurnEffect.toLong(), ACCELERATE_DECELERATE)
      bg_screen.setTransitionGenerator(generator)
    }
  }

  private fun onTransittionListener(): KenBurnsView.TransitionListener? {
    return object : KenBurnsView.TransitionListener {
      override fun onTransitionStart(transition: Transition) {
        //Toast.makeText(f, "start", Toast.LENGTH_SHORT).show();
      }

      override fun onTransitionEnd(transition: Transition) {
        bg_screen.pause()
        //Toast.makeText(f, "end", Toast.LENGTH_SHORT).show();
        if (null == mparcel) {
          Handler(Looper.getMainLooper()).postDelayed({
            runOnUiThread {
              main_layout.visibility = View.VISIBLE
              val animation = AnimationUtils.loadAnimation(
                  f, anim.smooth_fadein
              )
              main_layout.startAnimation(animation)
              //mainLayout.setBackgroundColor(getColor(R.color.em_white_80));
              changeColorAnimation(1500)
              Handler(Looper.getMainLooper()).postDelayed({
                runOnUiThread { //smoothAnimateMarginChange(PixelCalc.DpToPixel(50, f), PixelCalc.DpToPixel(getResources().getDimension(R.dimen.Register_Margin_TopToLogo), f), 800, 0);
                  //smoothAnimateMarginChange(PixelCalc.DpToPixel(40, f), PixelCalc.DpToPixel(60, f), 800, 0);
                  smoothAnimateMarginChange(fromTop, toBottom, 800, 0)
                }
              }, 600)
            }
          }, 200)
        }
      }
    }
  }

  private fun smoothAnimateMarginChange(
    fromMargin: Int,
    toMargin: Int,
    duration: Int,
    v: Int
  ) {
    val varl = ValueAnimator.ofInt(fromMargin, toMargin)
    varl.duration = duration.toLong()
    varl.addUpdateListener { animation ->
      when (v) {
        0 -> {
          layoutParamsTitleApps!!.topMargin = (animation.animatedValue as Int)
          logo_mililani.layoutParams = layoutParamsTitleApps
          logo_mililani.requestLayout()
          if (Hawk.contains(Constant.KEY_TOKEN))
            BottomMenuNavigationActivity.launchIntent(this)
          else
            LoginPhoneNumberActivity.launchIntent(this)
//          BottomMenuNavigationActivity.launchIntent(this)

          finish()
        }
        1 -> {
        }
      }
    }
    varl.start()
  }

  private fun changeColorAnimation(duration: Int) {
    val colorFrom = ContextCompat.getColor(this, color.Register_Main_Layout)
    val colorTo = ContextCompat.getColor(this, color.em_white_87)

    val colorAnimation = ValueAnimator.ofArgb(colorFrom, colorTo)
    colorAnimation.duration = duration.toLong() // milliseconds
    colorAnimation.addUpdateListener { animator ->
      main_layout.setBackgroundColor(
          animator.animatedValue as Int
      )
    }
    colorAnimation.start()
  }
}
