package com.strategies360.mililani2.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.WebviewAssessmentActivity
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.AppError
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.remote.assessment.AssessmentResponse
import com.strategies360.mililani2.viewmodel.AssessmentViewModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_asessment.*

class AsessmentFragment : CoreFragment(), View.OnClickListener {

  var mLayoutManager: RecyclerView.LayoutManager? = null

  private val viewModel by lazy {
    ViewModelProviders.of(this)
      .get(AssessmentViewModel::class.java)
  }
  
  override val viewRes: Int = R.layout.fragment_asessment

  override fun onViewCreated(
    view: View,

    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    initViewModel()
    viewModel.getAssessment()

    btn_open_quaterly.setOnClickListener(this)
    btn_online_payment.setOnClickListener(this)
    btn_assessment_due_date.setOnClickListener(this)
    btn_reminder_notice.setOnClickListener(this)
    btn_statement.setOnClickListener(this)
    btn_how_pay.setOnClickListener(this)

    btn_sure_pay.setOnClickListener(this)
    btn_detail_pay_online.setOnClickListener(this)
    btn_detail_pay_person.setOnClickListener(this)
    btn_detail_user.setOnClickListener(this)
    btn_pay_mail.setOnClickListener(this)

    btn_register.setOnClickListener(this)
    btn_enroll.setOnClickListener(this)
    btn_pay_assessment_online.setOnClickListener(this)
    btn_reservations.setOnClickListener(this)
  }

  @SuppressLint("UseCompatLoadingForDrawables")
  override fun onClick(view: View?) {
    if (view == btn_open_quaterly) {
      if (txt_quaterly.visibility == View.GONE) {
        ic_plus_quaterly.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
        slideDown(txt_quaterly)
        slideUp(layout_assessment_due_date)
        slideUp(layout_reminder)
        slideUp(layout_statement)
        slideUp(layout_online_payment)
        slideUp(layout_detail_sure_pay)
        slideUp(layout_detail_pay_person)
        slideUp(layout_detail_user)
        slideUp(layout_how_pay)
        slideUp(layout_pay_mail)
      } else {
        ic_plus_quaterly.setImageDrawable(requireContext().getDrawable(R.drawable.ic_plus))
        slideUp(txt_quaterly)
      }
    } else if (view == btn_online_payment) {
      if (layout_online_payment.visibility == View.GONE) {
        ic_plus_online.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
        slideDown(layout_online_payment)
        slideUp(layout_assessment_due_date)
        slideUp(layout_statement)
        slideUp(layout_reminder)
        slideUp(txt_quaterly)
        slideUp(layout_detail_sure_pay)
        slideUp(layout_detail_pay_person)
        slideUp(layout_detail_user)
        slideUp(layout_how_pay)
        slideUp(layout_pay_mail)
      } else {
        ic_plus_online.setImageDrawable(requireContext().getDrawable(R.drawable.ic_plus))
        slideUp(layout_online_payment)
      }
    } else if (view == btn_assessment_due_date) {
      if (layout_assessment_due_date.visibility == View.GONE) {
        ic_plus_assessment.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
        slideDown(layout_assessment_due_date)
        slideUp(layout_online_payment)
        slideUp(layout_statement)
        slideUp(layout_reminder)
        slideUp(txt_quaterly)
        slideUp(layout_detail_sure_pay)
        slideUp(layout_detail_pay_person)
        slideUp(layout_detail_user)
        slideUp(layout_how_pay)
        slideUp(layout_pay_mail)
      } else {
        ic_plus_assessment.setImageDrawable(requireContext().getDrawable(R.drawable.ic_plus))
        slideUp(layout_assessment_due_date)
      }
    } else if (view == btn_reminder_notice) {
      if (layout_reminder.visibility == View.GONE) {
        ic_plus_reminder.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
        slideDown(layout_reminder)
        slideUp(layout_statement)
        slideUp(layout_online_payment)
        slideUp(layout_assessment_due_date)
        slideUp(txt_quaterly)
        slideUp(layout_detail_sure_pay)
        slideUp(layout_detail_pay_person)
        slideUp(layout_detail_user)
        slideUp(layout_pay_mail)
        slideUp(layout_how_pay)
      } else {
        ic_plus_reminder.setImageDrawable(requireContext().getDrawable(R.drawable.ic_plus))
        slideUp(layout_reminder)
      }
    } else if (view == btn_statement) {
      if (layout_statement.visibility == View.GONE) {
        ic_plus_statement.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
        slideDown(layout_statement)
        slideUp(layout_reminder)
        slideUp(layout_online_payment)
        slideUp(layout_assessment_due_date)
        slideUp(txt_quaterly)
        slideUp(layout_detail_sure_pay)
        slideUp(layout_detail_pay_person)
        slideUp(layout_detail_user)
        slideUp(layout_pay_mail)
        slideUp(layout_how_pay)
      } else {
        ic_plus_statement.setImageDrawable(requireContext().getDrawable(R.drawable.ic_plus))
        slideUp(layout_statement)
      }
    } else if (view == btn_how_pay) {
      if (layout_how_pay.visibility == View.GONE) {
        ic_plus_how_pay.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
        slideDown(layout_how_pay)
        slideUp(layout_statement)
        slideUp(layout_reminder)
        slideUp(layout_online_payment)
        slideUp(layout_assessment_due_date)
        slideUp(txt_quaterly)
        slideUp(layout_detail_sure_pay)
        slideUp(layout_detail_pay_person)
        slideUp(layout_detail_user)
        slideUp(layout_pay_mail)
      } else {
        ic_plus_how_pay.setImageDrawable(requireContext().getDrawable(R.drawable.ic_plus))
        slideUp(layout_how_pay)
      }
    } else if (view == btn_sure_pay) {
      if (layout_detail_sure_pay.visibility == View.GONE) {
        ic_plus_detail_sure_pay.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
        slideDown(layout_detail_sure_pay)
        slideUp(layout_detail_pay_online)
        slideUp(layout_detail_pay_person)
        slideUp(layout_detail_user)
        slideUp(layout_pay_mail)
      } else {
        ic_plus_detail_sure_pay.setImageDrawable(requireContext().getDrawable(R.drawable.ic_plus))
        slideUp(layout_detail_sure_pay)
      }
    } else if (view == btn_detail_pay_online) {
      if (layout_detail_pay_online.visibility == View.GONE) {
        ic_plus_detail_pay_online.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
        slideDown(layout_detail_pay_online)
        slideUp(layout_detail_sure_pay)
        slideUp(layout_detail_pay_person)
        slideUp(layout_detail_user)
        slideUp(layout_pay_mail)
      } else {
        ic_plus_detail_pay_online.setImageDrawable(requireContext().getDrawable(R.drawable.ic_plus))
        slideUp(layout_detail_pay_online)
      }
    } else if (view == btn_detail_pay_person) {
      if (layout_detail_pay_person.visibility == View.GONE) {
        ic_plus_how_pay.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
        slideDown(layout_detail_pay_person)
        slideUp(layout_detail_pay_online)
        slideUp(layout_detail_sure_pay)
        slideUp(layout_detail_user)
        slideUp(layout_pay_mail)
      } else {
        ic_plus_how_pay.setImageDrawable(requireContext().getDrawable(R.drawable.ic_plus))
        slideUp(layout_detail_pay_person)
      }
    } else if (view == btn_detail_user) {
      if (layout_detail_user.visibility == View.GONE) {
        ic_plus_detail_user.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
        slideDown(layout_detail_user)
        slideUp(layout_detail_pay_online)
        slideUp(layout_detail_sure_pay)
        slideUp(layout_detail_pay_person)
        slideUp(layout_pay_mail)
      } else {
        ic_plus_detail_user.setImageDrawable(requireContext().getDrawable(R.drawable.ic_plus))
        slideUp(layout_detail_user)
      }
    } else if (view == btn_pay_mail) {
      if (layout_pay_mail.visibility == View.GONE) {
        ic_plus_detail_pay_mail.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
        slideDown(layout_pay_mail)
        slideUp(layout_detail_pay_online)
        slideUp(layout_detail_sure_pay)
        slideUp(layout_detail_pay_person)
        slideUp(layout_detail_user)
      } else {
        ic_plus_detail_pay_mail.setImageDrawable(requireContext().getDrawable(R.drawable.ic_plus))
        slideUp(layout_pay_mail)
      }
    } else if (view == btn_register) {
      WebviewAssessmentActivity.launchIntent(requireContext(), "https://secure.directbiller.com/db-payer-ui/#/login")
    } else if (view == btn_enroll) {
      WebviewAssessmentActivity.launchIntent(requireContext(), "https://mililanitown.opt-e-mail.com/signup")
    } else if (view == btn_reservations) {
      WebviewAssessmentActivity.launchIntent(requireContext(), "https://secure.directbiller.com/db-payer-ui/#/login")
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
        Resource.LOADING -> onAssessmentLoading()
        Resource.SUCCESS -> onAssessmentSuccess(it.data)
        Resource.ERROR -> onAssessmentFailure(it.error)
      }
    })
  }

  private fun onAssessmentLoading() {
    progress_bar.visibility = View.VISIBLE
    layout_assessment.visibility = View.GONE
  }

  private fun onAssessmentSuccess(Assessment: AssessmentResponse?) {
    progress_bar.visibility = View.GONE
    if (Assessment != null) {
      layout_assessment.visibility = View.VISIBLE

      val htmlAsString = Assessment.postContentAssessment?.titleCarousel?.content

      img_assessment.load(Assessment.postContentAssessment?.titleCarousel?.carousel?.get(0))
      txt_quaterly_assessment.text = Assessment.postContentAssessment?.titleCarousel?.title
      txt_quaterly.text = convertHtmlToString(Assessment.postContentAssessment?.titleCarousel?.content.toString())
      defaultSlideUp()

      txt_title_payment_site.text = convertHtmlToString(Assessment.postContentAssessment?.tabContentAssessment?.contentOneAssessment!!.heading.toString())
      txt_payment_online.text = convertHtmlToString(Assessment.postContentAssessment.tabContentAssessment.contentOneAssessment.innerContentOne.toString())

      txt_assessment_due_date.text = convertHtmlToString(Assessment.postContentAssessment.tabContentAssessment.contentTwoAssessment?.heading.toString())
      txt_detail_assesment_content_effective.text = convertHtmlToString(Assessment.postContentAssessment.tabContentAssessment.contentTwoAssessment?.innerContentNol.toString())
      txt_assesment_content_effective.text = convertHtmlToString(Assessment.postContentAssessment.tabContentAssessment.contentTwoAssessment?.innerContentOne.toString())
      txt_assessment_late_charge.text = convertHtmlToString(Assessment.postContentAssessment.tabContentAssessment.contentTwoAssessment?.innerContentTwo.toString())

      txt_title_remainder_notice.text = convertHtmlToString(Assessment.postContentAssessment.tabContentAssessment.contentThreeAssessment?.heading.toString())
      txt_sub_title_detail_remainder.text = convertHtmlToString(Assessment.postContentAssessment.tabContentAssessment.contentThreeAssessment?.innerContentThree.toString())
      txt_detail_sub_title_detail_remainder.text =convertHtmlToString(Assessment.postContentAssessment.tabContentAssessment.contentThreeAssessment?.innerContentTwo.toString())
      txt_detail_remainder.text = convertHtmlToString(Assessment.postContentAssessment.tabContentAssessment.contentThreeAssessment?.innerContentOne.toString())

      txt_title_statement.text = convertHtmlToString(Assessment.postContentAssessment.tabContentAssessment.contentFourAssessment?.heading.toString())
      txt_detail_assessment.text = convertHtmlToString(Assessment.postContentAssessment.tabContentAssessment.contentFourAssessment?.innerContentTwo.toString())
      txt_sub_detail_assessment.text = convertHtmlToString(Assessment.postContentAssessment.tabContentAssessment.contentFourAssessment?.innerContentThree.toString())

      txt_title_how_pay.text = convertHtmlToString(Assessment.postContentAssessment.accordions?.title.toString())
      txt_subtitle_accordion.text = convertHtmlToString(Assessment.postContentAssessment.accordions?.subtitle.toString())

//      val adapter = AssessmentAdapter()
//
//      recycler_Assessment.layoutManager =
//        LinearLayoutManager(
//          recycler_Assessment.context, LinearLayoutManager.VERTICAL,
//          false
//        )
//
//      recycler_Assessment.isNestedScrollingEnabled = false
//      mLayoutManager = recycler_Assessment.layoutManager
//
//      adapter.setDataList(Assessment.postContentAssessment?.tabContentAssessment?.contentThreeAssessment?.innerContenAccordionAssessment)
//      recycler_Assessment.adapter = adapter
//      adapter.notifyDataSetChanged()
    }
  }

  private fun onAssessmentFailure(error: AppError) {
    progress_bar.visibility = View.GONE
    layout_assessment.visibility = View.VISIBLE
  }

  private fun convertHtmlToString(data: String): String {
    return HtmlCompat.fromHtml(data, HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_LIST).toString()
  }

  private fun defaultSlideUp() {
    ic_plus_quaterly.setImageDrawable(requireContext().getDrawable(R.drawable.ic_min))
    slideDown(txt_quaterly)
    slideUp(layout_assessment_due_date)
    slideUp(layout_reminder)
    slideUp(layout_statement)
    slideUp(layout_online_payment)
    slideUp(layout_detail_sure_pay)
    slideUp(layout_detail_pay_person)
    slideUp(layout_detail_user)
    slideUp(layout_how_pay)
    slideUp(layout_pay_mail)
  }

}