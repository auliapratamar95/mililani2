package com.strategies360.mililani2.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.strategies360.mililani2.R
import com.strategies360.mililani2.adapter.TagHandler
import com.strategies360.mililani2.adapter.recycler.EmploymentAdapter
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.employment.Employment
import com.strategies360.mililani2.viewmodel.EmploymentViewModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_employess.*

class EmploymentFragment : CoreFragment(), View.OnClickListener {

  var mLayoutManager: RecyclerView.LayoutManager? = null

  private val viewModel by lazy {
    ViewModelProviders.of(this)
      .get(EmploymentViewModel::class.java)
  }

  override val viewRes: Int = R.layout.fragment_employess

  @SuppressLint("SetTextI18n")
  override fun onViewCreated(
    view: View,
    
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    initViewModel()

    viewModel.sample()

    btn_open_question.setOnClickListener(this)
  }

  @SuppressLint("UseCompatLoadingForDrawables")
  override fun onClick(view: View?) {
    if (view == btn_open_question) {
      if (txt_question.visibility == View.GONE) {
        ic_plus_question.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
        slideDown(txt_question)
      } else {
        ic_plus_question.setImageDrawable(requireContext().getDrawable(R.drawable.ic_plus))
        slideUp(txt_question)
      }
    }
  }

  // slide the view from below itself to the current position
  private fun slideUp(view: View) {
    view.animate()
            .translationY(2f)
            .setDuration(100)
            .setListener(object : AnimatorListenerAdapter() {
              override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE
              }
            })
    view.clearFindViewByIdCache()
  }

  // slide the view from its current position to below itself
  private fun slideDown(view: View) {
    view.visibility = View.VISIBLE
  }

  private fun initViewModel() {
    viewModel.resource.observe(viewLifecycleOwner, {
      when (it.status) {
        Resource.LOADING -> onEmploymentLoading()
        Resource.SUCCESS -> onEmploymentSuccess(it.data)
        Resource.ERROR -> onEmploymentFailure(it.error)
      }
    })
  }

  private fun onEmploymentLoading() {
    progress_bar.visibility = View.VISIBLE
    layout_employment.visibility = View.GONE
  }

  private fun onEmploymentSuccess(employment: Employment?) {
    progress_bar.visibility = View.GONE
    if (employment != null) {
      val htmlContentTwoEmployment= employment.postContentEmployment?.tabContentEmployment?.contentTwoEmployment?.innerContent0
      val htmlAsString = employment.postContentEmployment?.titleCarousel?.content
      val htmlAsHeading = employment.postContentEmployment?.tabContentEmployment?.contentThreeEmployment?.heading

      layout_employment.visibility = View.VISIBLE

      img_employment.load(employment.postContentEmployment?.titleCarousel?.carousel?.get(0))
      txt_title.text = employment.postContentEmployment?.titleCarousel?.title
      txt_subtitle.text = employment.postContentEmployment?.titleCarousel?.subtitle
      txt_detail_employment.text = HtmlCompat.fromHtml(htmlAsString.toString(), HtmlCompat.FROM_HTML_MODE_COMPACT)
      txt_detail_applications.text = HtmlCompat.fromHtml(htmlContentTwoEmployment.toString(), HtmlCompat.FROM_HTML_MODE_COMPACT)

      btn_mta_employment.setOnClickListener {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mililanitown.org/wp-content/uploads/2019/07/MTA-Application-Rev-20190709.pdf"))
        startActivity(browserIntent)
      }

      val adapter = EmploymentAdapter()

      recycler_employment.layoutManager =
        LinearLayoutManager(
          recycler_employment.context, LinearLayoutManager.VERTICAL,
          false
        )

      recycler_employment.isNestedScrollingEnabled = false
      mLayoutManager = recycler_employment.layoutManager

      adapter.setDataList(employment.postContentEmployment?.tabContentEmployment?.contentThreeEmployment?.innerContenAccordionEmployment)
      recycler_employment.adapter = adapter
      adapter.notifyDataSetChanged()
    }
  }

  private fun onEmploymentFailure(error: AppError) {
    progress_bar.visibility = View.GONE
    layout_employment.visibility = View.VISIBLE
  }
}