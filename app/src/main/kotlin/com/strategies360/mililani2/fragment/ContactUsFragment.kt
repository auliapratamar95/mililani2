package com.strategies360.mililani2.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.strategies360.mililani2.R
import com.strategies360.mililani2.adapter.recycler.ContactUsAdapter
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.contact.Contact
import com.strategies360.mililani2.viewmodel.ContactUsViewModel
import kotlinx.android.synthetic.main.fragment_contact_us.*
import java.lang.String
import java.util.*


class ContactUsFragment: CoreFragment() {

  var mLayoutManager: RecyclerView.LayoutManager? = null

  private val viewModel by lazy {
    ViewModelProviders.of(this)
            .get(ContactUsViewModel::class.java)
  }

  override val viewRes = R.layout.fragment_contact_us

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    initViewModel()
    viewModel.sample()
  }

  private fun initViewModel() {
    viewModel.resource.observe(viewLifecycleOwner, Observer {
      when (it.status) {
        Resource.LOADING -> onContactLoading()
        Resource.SUCCESS -> onContactSuccess(it.data)
        Resource.ERROR -> onContactFailure(it.error)
      }
    })
  }

  private fun onContactLoading() {
    progress_bar.visibility = View.VISIBLE
    layout_contact_us.visibility = View.GONE
  }

  private fun onContactSuccess(contact: Contact?) {
    progress_bar.visibility = View.GONE
    if (contact != null) {
      layout_contact_us.visibility = View.VISIBLE

      img_contact.load(contact.acf?.titlecarousel!!.listImageContact?.get(0))
      txt_title.text = contact.acf?.titlecarousel!!.subtitle

      val htmlAsString = contact.acf?.titlecarousel!!.content.toString()

      sample.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null)
      txt_content.text = HtmlCompat.fromHtml(contact.acf?.titlecarousel!!.content.toString(), HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST)

      val adapter = ContactUsAdapter()

      adapter.onItemContactClick = { _, data ->
        val browserIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.phone))
        startActivity(browserIntent)
      }

      adapter.onItemMapsContactClick = { _, data ->

        val longlat: kotlin.String = data.lat + "," + data.lng
        val gmmIntentUri = Uri.parse("google.streetview:cbll= $longlat")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)


      }

      recycler_contact_us.layoutManager =
              LinearLayoutManager(
                      recycler_contact_us.context, LinearLayoutManager.VERTICAL,
                      false
              )

      recycler_contact_us.isNestedScrollingEnabled = false
      mLayoutManager = recycler_contact_us.layoutManager

      adapter.setDataList(contact.acf!!.address)
      recycler_contact_us.adapter = adapter
      adapter.notifyDataSetChanged()
    }
  }

  private fun onContactFailure(error: AppError) {
    progress_bar.visibility = View.GONE
    layout_contact_us.visibility = View.VISIBLE
  }
}