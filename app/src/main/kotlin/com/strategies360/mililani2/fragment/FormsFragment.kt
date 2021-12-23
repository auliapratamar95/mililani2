package com.strategies360.mililani2.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.strategies360.mililani2.R
import com.strategies360.mililani2.adapter.recycler.FormAdapter
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.form.FormPostContent
import com.strategies360.mililani2.model.remote.form.FormResponse
import com.strategies360.mililani2.viewmodel.FormViewModel
import kotlinx.android.synthetic.main.fragment_forms.*

class FormsFragment : CoreFragment(), View.OnClickListener {

  var mLayoutManager: RecyclerView.LayoutManager? = null

  private val viewModel by lazy {
    ViewModelProviders.of(this)
      .get(FormViewModel::class.java)
  }

  override val viewRes: Int = R.layout.fragment_forms

  @SuppressLint("SetTextI18n")
  override fun onViewCreated(
    view: View,

    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    initViewModel()

    viewModel.getForm()
  }

  @SuppressLint("UseCompatLoadingForDrawables")
  override fun onClick(view: View?) {

  }

  private fun initViewModel() {
    viewModel.resource.observe(viewLifecycleOwner, {
      when (it.status) {
        Resource.LOADING -> onFormLoading()
        Resource.SUCCESS -> onFormSuccess(it.data?.formPostContent)
        Resource.ERROR -> onFormFailure(it.error)
      }
    })
  }

  private fun onFormLoading() {
    progress_bar.visibility = View.VISIBLE
    layout_form.visibility = View.GONE
  }

  private fun onFormSuccess(form: FormPostContent?) {
    progress_bar.visibility = View.GONE
    if (form != null) {
      val htmlAsString = form.titlecarousel?.content

      layout_form.visibility = View.VISIBLE

      img_form.load(form.titlecarousel?.carousel?.get(0))
      txt_title.text = form.titlecarousel?.title
      txt_subtitle.text = form.titlecarousel?.subtitle
      txt_detail_form.text = HtmlCompat.fromHtml(htmlAsString.toString(), HtmlCompat.FROM_HTML_MODE_COMPACT)

      val adapter = FormAdapter()

      recycler_form.layoutManager =
        LinearLayoutManager(
          recycler_form.context, LinearLayoutManager.VERTICAL,
          false
        )

      recycler_form.isNestedScrollingEnabled = false
      mLayoutManager = recycler_form.layoutManager

      adapter.setDataList(form.accordions?.contentList)
      recycler_form.adapter = adapter
      adapter.notifyDataSetChanged()
    }
  }

  private fun onFormFailure(error: AppError) {
    progress_bar.visibility = View.GONE
    layout_form.visibility = View.VISIBLE
  }
}