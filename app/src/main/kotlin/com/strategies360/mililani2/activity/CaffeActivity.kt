package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.eventbus.EventChangeViewCategoryCaffe
import com.strategies360.mililani2.eventbus.EventFlagGetProductCaffe
import com.strategies360.mililani2.eventbus.EventFlagGetSubProductCaffe
import com.strategies360.mililani2.fragment.CaffeFragment
import com.strategies360.mililani2.fragment.CategoryProductListFragment
import com.strategies360.mililani2.fragment.MTACardBottomListFragment
import com.strategies360.mililani2.fragment.SubCaffeFragment
import com.strategies360.mililani2.model.remote.caffe.ProductCaffe
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.activity_caffe.btn_back
import kotlinx.android.synthetic.main.activity_caffe.btn_category
import kotlinx.android.synthetic.main.activity_caffe.btn_scan_barcode
import kotlinx.android.synthetic.main.activity_caffe.layout_tabs
import kotlinx.android.synthetic.main.activity_caffe.tabs
import kotlinx.android.synthetic.main.activity_caffe.viewpager_caffe
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.MAIN

class CaffeActivity : CoreActivity() {

  override val viewRes: Int = R.layout.activity_caffe

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    fade()
    setupViewPager(viewpager_caffe)
    tabs.setupWithViewPager(viewpager_caffe)
    usingTabOnClick()
    btn_back.setOnClickListener {
      onBackPressed()
      finish()
    }

    btn_scan_barcode.setOnClickListener {
      openBottomCardList()
    }

    btn_category.setOnClickListener {
      openCategory()
    }
  }

  override fun onStart() {
    super.onStart()
    EventBus.getDefault().register(this)
  }

  override fun onStop() {
    super.onStop()
    EventBus.getDefault().unregister(this)
  }

  private fun openBottomCardList() {
    val fragManager: FragmentManager = supportFragmentManager
    MTACardBottomListFragment()
        .show(fragManager, "Dialog")
  }

  private fun setupViewPager(viewPager: ViewPager) {
    val adapter = ViewPagerAdapter(supportFragmentManager)
    if (Hawk.contains(Constant.PRODUCT_CAFFE_LIST)) {
      val productCaffe: ArrayList<ProductCaffe> = Hawk.get(Constant.PRODUCT_CAFFE_LIST)
      for (i in productCaffe.indices) {
        if (productCaffe[i].subProductCaffeList!!.size != 0) {
          adapter.addFrag(SubCaffeFragment(), productCaffe[i].title.toString())
        } else {
          adapter.addFrag(CaffeFragment(), productCaffe[i].title.toString())
        }
      }
    }
    viewPager.adapter = adapter
  }

  fun fade() {
    val animation1: Animation = AnimationUtils.loadAnimation(
        applicationContext,
        R.anim.sample
    )
    layout_tabs.startAnimation(animation1)
//    val objectAnimator = ObjectAnimator.ofInt(layout_tabs, "translationX", 100, 250).setDuration(1000)
//    objectAnimator.start()
  }

  internal class ViewPagerAdapter(manager: FragmentManager?) : FragmentPagerAdapter(manager!!) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()
    override fun getItem(position: Int): Fragment {
      return mFragmentList[position]
    }

    override fun getCount(): Int {
      return mFragmentList.size
    }

    fun addFrag(
      fragment: Fragment,
      title: String
    ) {
      mFragmentList.add(fragment)
      mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
      return mFragmentTitleList[position]
    }
  }

  private fun openCategory() {
    val fragManager: FragmentManager = supportFragmentManager
    CategoryProductListFragment()
            .show(fragManager, "Dialog")
  }

  private fun usingTabOnClick() {
    tabs!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab) {
        if (Hawk.contains(Constant.PRODUCT_CAFFE_LIST)) {
          val productCaffe: ArrayList<ProductCaffe> = Hawk.get(Constant.PRODUCT_CAFFE_LIST)
          for (i in productCaffe.indices) {
            if (productCaffe[i].title == tab.text.toString()) {
              Hawk.delete(Constant.KEY_ID_CATEGORY)
              Hawk.put((Constant.KEY_ID_CATEGORY), productCaffe[i].id)
              if (productCaffe[i].subProductCaffeList?.size == 0) {
                EventBus.getDefault().postSticky(
                    EventFlagGetProductCaffe(true, tab.text.toString())
                )
              } else {
                EventBus.getDefault().postSticky(
                    EventFlagGetSubProductCaffe(true, tab.text.toString())
                )
              }
              break
            }
          }
        }
      }

      override fun onTabUnselected(tab: TabLayout.Tab) {

      }

      override fun onTabReselected(tab: TabLayout.Tab) {

      }
    })
  }

  @Subscribe(sticky = true, threadMode = MAIN)
  fun onChangeViewCategory(event: EventChangeViewCategoryCaffe) {
    if (event.isGetProductCaffe) {
      viewpager_caffe.currentItem = event.currentItem
      EventBus.getDefault().removeStickyEvent(event)
    }
  }

  companion object {

    /**
     * Launch this activity.
     * @param context the context
     */
    fun launchIntent(context: Context) {
      val intent = Intent(context, CaffeActivity::class.java)
      context.startActivity(intent)
    }
  }
}