package com.strategies360.mililani2.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.core.CoreActivity
import com.strategies360.mililani2.fragment.SampleProductListFragment
import kotlinx.android.synthetic.main.activity_sample_product_list.*

/**
 *
 * A sample data list activity.
 */
class SampleProductListActivity : CoreActivity() {

    override val viewRes = R.layout.activity_sample_product_list

    private val fragment = SampleProductListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) initFragment()
    }

    /** Initialize the fragment */
    private fun initFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(layout_fragment.id, fragment)
        fragmentTransaction.commit()
    }

    companion object {

        /**
         * Launch this activity.
         * @param context the context
         */
        fun launchIntent(context: Context) {
            val intent = Intent(context, SampleProductListActivity::class.java)
            context.startActivity(intent)
        }
    }
}
