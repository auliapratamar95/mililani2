package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.fragment.CaffeFragment
import com.strategies360.mililani2.fragment.CategoryProductListFragment
import com.strategies360.mililani2.fragment.MTACardBottomListFragment
import com.strategies360.mililani2.model.remote.caffe.Caffe
import com.strategies360.mililani2.model.remote.caffe.CategoryProduct
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.activity_caffe.*
import kotlinx.android.synthetic.main.fragment_mta_card_bottom_list.*


class CaffeActivity : CoreActivity() {

  override val viewRes: Int = R.layout.activity_caffe

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

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

  override fun onBackPressed() {
    ProfileMtaActivity.launchIntent(this)
    finish()
  }

  private fun openBottomCardList() {
    val fragManager: FragmentManager = supportFragmentManager
    MTACardBottomListFragment()
        .show(fragManager, "Dialog")
  }

  private fun setupViewPager(viewPager: ViewPager) {
    val adapter = ViewPagerAdapter(supportFragmentManager)
    if (Hawk.contains(Constant.FLAG_ON_CATEGORY)) {
      val categoryProduct: ArrayList<CategoryProduct> = Hawk.get(Constant.FLAG_ON_CATEGORY)
      for (i in categoryProduct.indices) {
        adapter.addFrag(CaffeFragment(), categoryProduct[i].title.toString())
      }
    } else {
      adapter.addFrag(CaffeFragment(), "Coffe & Espresso")
      adapter.addFrag(CaffeFragment(), "Frappucino")
      adapter.addFrag(CaffeFragment(), "Startbuck Refresher")
      adapter.addFrag(CaffeFragment(), "Frappucino")
      adapter.addFrag(CaffeFragment(), "Tea")
      adapter.addFrag(CaffeFragment(), "Frozen Drinks")
      adapter.addFrag(CaffeFragment(), "Other Drinks")
      adapter.addFrag(CaffeFragment(), "Food and Snack")
    }
    viewPager.adapter = adapter
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

    fun addFrag(fragment: Fragment, title: String) {
      mFragmentList.add(fragment)
      mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
      return mFragmentTitleList[position]
    }
  }

  private fun openCategory() {
    val fragManager: FragmentManager? = supportFragmentManager
    if (fragManager != null) {
      CategoryProductListFragment()
              .show(fragManager, "Dialog")
    }
  }

  private fun usingTabOnClick() {
    tabs!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
      override fun onTabSelected(tab: TabLayout.Tab) {
        val categoryProduct: ArrayList<CategoryProduct> = Hawk.get(Constant.FLAG_ON_CATEGORY)
        for (i in categoryProduct.indices) {
          if(i == tab.position-1) {
            val adapter = ViewPagerAdapter(supportFragmentManager)
            adapter.addFrag(CaffeFragment(), tab.text.toString())

            Hawk.put((Constant.KEY_ID_CATEGORY), categoryProduct[i].id)
            viewpager_caffe.adapter = adapter
          }
        }
      }
      override fun onTabUnselected(tab: TabLayout.Tab) {

      }
      override fun onTabReselected(tab: TabLayout.Tab) {

      }
    })
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