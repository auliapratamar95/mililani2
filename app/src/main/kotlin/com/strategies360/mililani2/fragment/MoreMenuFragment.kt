package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.activity.*
import com.strategies360.mililani2.eventbus.EventChangeQuickLink
import com.strategies360.mililani2.eventbus.EventFlagGetSubProductCaffe
import com.strategies360.mililani2.fragment.core.CoreFragment
import com.strategies360.mililani2.model.core.Resource
import com.strategies360.mililani2.model.local.categoryquicklink.CategoryQuickLink
import com.strategies360.mililani2.model.remote.caffe.ProductCaffeResponse
import com.strategies360.mililani2.model.remote.caffe.cart.CartResponse
import com.strategies360.mililani2.util.Common
import com.strategies360.mililani2.util.Constant
import com.strategies360.mililani2.viewmodel.CaffeListViewModel
import com.strategies360.mililani2.viewmodel.CartListViewModel
import kotlinx.android.synthetic.main.fragment_more_menu.*
import kotlinx.android.synthetic.main.include_toolbar.btn_back
import kotlinx.android.synthetic.main.include_toolbar.btn_barcode
import kotlinx.android.synthetic.main.layout_menu_quick_link.*
import org.greenrobot.eventbus.EventBus

class MoreMenuFragment : CoreFragment(), View.OnClickListener {
    private val cartViewModel by lazy {
        ViewModelProviders.of(this)
            .get(CartListViewModel::class.java)
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this)
            .get(CaffeListViewModel::class.java)
    }

    override val viewRes = R.layout.fragment_more_menu

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        Hawk.put((Constant.FLAG_ON_BACK_MENU), false)

        initView()
        viewModel.fetchProductCaffe()

//        Hawk.delete(Constant.KEY_CATEGORY_QUICK_LINK_LIST)

        getCategoryQuickLink()

        onClickQuickLink()
        onClickOtherService()
    }

    private fun initView() {
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.resourceProductCaffe.observe(viewLifecycleOwner, Observer {
            when (it?.status) {
                Resource.LOADING -> onProductCaffeLoading()
                Resource.ERROR -> onProductCaffeFailure()
                Resource.SUCCESS -> onProductCaffeSuccess(it.data!!)
            }
        })

        cartViewModel.resource.observe(viewLifecycleOwner, Observer {
            when (it?.status) {
                Resource.LOADING -> onCartLoading()
                Resource.ERROR -> onCartFailure()
                Resource.SUCCESS -> onCartSuccess(it.data!!)
            }
        })
    }

    private fun onProductCaffeLoading() {
        progress_setting.visibility = View.VISIBLE
    }

    private fun onProductCaffeSuccess(productCaffeResponse: ProductCaffeResponse) {
        Hawk.delete(Constant.PRODUCT_CAFFE_LIST)
        Hawk.delete(Constant.KEY_ID_CATEGORY)

        Hawk.put((Constant.PRODUCT_CAFFE_LIST), productCaffeResponse.caffeListResponse)
        if (Hawk.contains(Constant.KEY_CUSTOMER_ID)) {
            val customerId: String = Common.getCookies()
            cartViewModel.fetchData(customerId)
        } else {
            progress_setting.visibility = View.GONE
            progress_bar_more_menu.visibility = View.GONE
            layout_setting.visibility = View.VISIBLE
            layout_more_menu.visibility = View.VISIBLE
        }
    }

    private fun onProductCaffeFailure() {
        layout_setting.visibility = View.VISIBLE
        progress_setting.visibility = View.GONE
    }

    private fun onCartLoading() {
        progress_setting.visibility = View.VISIBLE
    }

    private fun onCartSuccess(cartResponse: CartResponse) {
        Hawk.delete(Constant.KEY_CART_LIST)

        progress_bar_more_menu.visibility = View.GONE
        layout_more_menu.visibility = View.VISIBLE

        Hawk.put((Constant.KEY_CART_LIST), cartResponse.cart)
        progress_setting.visibility = View.GONE
        layout_setting.visibility = View.VISIBLE
    }

    private fun onCartFailure() {
        layout_setting.visibility = View.VISIBLE
        progress_setting.visibility = View.GONE
    }

    private fun onClickQuickLink() {
        btn_add_caffe.setOnClickListener(this)
        btn_add_payment.setOnClickListener(this)
        btn_add_notification.setOnClickListener(this)
        btn_add_news.setOnClickListener(this)
        btn_add_recreation.setOnClickListener(this)
        btn_add_contact.setOnClickListener(this)
        btn_add_form.setOnClickListener(this)
        btn_add_history.setOnClickListener(this)
        btn_add_waiting_list.setOnClickListener(this)
        btn_add_employment.setOnClickListener(this)
        btn_scan_barcode.setOnClickListener(this)
        btn_waiting_list.setOnClickListener(this)
        btn_open_profile.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            btn_add_caffe -> {
                saveCategoryQuickLink(1, "caffe")
            }
            btn_add_contact -> {
                saveCategoryQuickLink(2, "contact")
            }
            btn_add_employment -> {
                saveCategoryQuickLink(3, "employment")
            }
            btn_add_payment -> {
                saveCategoryQuickLink(4, "payment")
            }
            btn_add_form -> {
                saveCategoryQuickLink(5, "form")
            }
            btn_add_news -> {
                saveCategoryQuickLink(6, "news")
            }
            btn_add_notification -> {
                saveCategoryQuickLink(7, "notification")
            }
            btn_add_recreation -> {
                saveCategoryQuickLink(8, "recreation")
            }
            btn_add_waiting_list -> {
                saveCategoryQuickLink(9, "waiting list")
            }
            btn_add_history -> {
                saveCategoryQuickLink(10, "history")
            }
            btn_scan_barcode -> {
                openBottomCardList()
            }
            btn_open_profile -> {
                openProfile()
            }
            btn_waiting_list -> {
                saveCategoryQuickLink(9, "waiting list")
                openWaitList()
            }
        }
    }

    private fun onClickOtherService() {
        btn_forms.setOnClickListener {
            saveCategoryQuickLink(5, "form")
            FormsActivity.launchIntent(requireContext())
        }

        btn_employment.setOnClickListener {
            saveCategoryQuickLink(3, "employment")
            EmploymentActivity.launchIntent(requireContext())
        }

        btn_contact_us.setOnClickListener {
            saveCategoryQuickLink(2, "contact")
            openContactUs()
        }

        btn_newsletters.setOnClickListener {
            saveCategoryQuickLink(6, "news")
            openNewsletter()
        }

        btn_notifications.setOnClickListener {
            saveCategoryQuickLink(7, "notification")
            openNotifications()
        }

        btn_history.setOnClickListener {
            saveCategoryQuickLink(10, "history")
            openHistory()
        }

        btn_rec_Center.setOnClickListener {
            saveCategoryQuickLink(8, "recreation")
            RecCenterActivity.launchIntent(requireContext())
        }

        btn_assessment.setOnClickListener {
            saveCategoryQuickLink(4, "payment")
            AsessmentActivity.launchIntent(requireContext())
        }

        btn_caffe.setOnClickListener {
            Hawk.delete(Constant.REQUIRED_CHOICE_PRODUCT)
            saveCategoryQuickLink(1, "caffe")
            CaffeActivity.launchIntent(requireContext())
        }

        btn_news.setOnClickListener {
            saveCategoryQuickLink(6, "news")
            NewsActivity.launchIntent(requireContext())
        }

        btn_back.setOnClickListener {
            BottomMenuNavigationActivity.launchIntent(requireContext())
        }

        btn_barcode.setOnClickListener {
            openBottomCardList()
        }
    }

    private fun openBottomCardList() {
        val fragManager: FragmentManager? = fragmentManager
        if (fragManager != null) {
            MTACardBottomListFragment()
                .show(fragManager, "Dialog")
        }
    }

    private fun saveCategoryQuickLink(id: Int, name: String) {
        var isCategoryAvailable = false
        if (Hawk.contains(Constant.KEY_CATEGORY_QUICK_LINK_LIST)) {
            val saveCategoryQuickLink: ArrayList<CategoryQuickLink> = ArrayList()
            val getCategoryQuickLink: ArrayList<CategoryQuickLink> =
                Hawk.get(Constant.KEY_CATEGORY_QUICK_LINK_LIST)

            if (getCategoryQuickLink.size < 4) {
                for (i in getCategoryQuickLink.indices) {
                    if (id == getCategoryQuickLink[i].id) {
                        isCategoryAvailable = true
                        break
                    }
                }

                if (!isCategoryAvailable) {
                    val categoryQuickLink = CategoryQuickLink()
                    categoryQuickLink.id = id
                    categoryQuickLink.name = name
                    getCategoryQuickLink.add(categoryQuickLink)
                }
                saveCategoryQuickLink.addAll(getCategoryQuickLink)
                Hawk.put((Constant.KEY_CATEGORY_QUICK_LINK_LIST), saveCategoryQuickLink)
            }
        } else {
            val listCategoryQuickLink: ArrayList<CategoryQuickLink> = ArrayList()
            val categoryQuickLink = CategoryQuickLink()
            categoryQuickLink.id = id
            categoryQuickLink.name = name
            listCategoryQuickLink.add(categoryQuickLink)
            Hawk.put((Constant.KEY_CATEGORY_QUICK_LINK_LIST), listCategoryQuickLink)
        }
//        showHideDialogQuickLink()
    }

    private fun getCategoryQuickLink() {
        if (Hawk.contains(Constant.KEY_CATEGORY_QUICK_LINK_LIST)) {
            val listCategoryQuickLink: ArrayList<CategoryQuickLink> =
                Hawk.get(Constant.KEY_CATEGORY_QUICK_LINK_LIST)

            if (listCategoryQuickLink.size < 5) {
                for (i in listCategoryQuickLink.indices) {
                    when (i + 1) {
                        1 -> {
                            bg_dash_one.visibility = View.GONE
                            txt_quick_link_one.visibility = View.VISIBLE
                            showIconQuickLinkOne(listCategoryQuickLink[i].id, img_quick_link_one, txt_quick_link_one)
                        }
                        2 -> {
                            bg_dash_two.visibility = View.GONE
                            txt_quick_link_two.visibility = View.VISIBLE
                            showIconQuickLinkOne(listCategoryQuickLink[i].id, img_quick_link_two, txt_quick_link_two)
                        }
                        3 -> {
                            bg_dash_three.visibility = View.GONE
                            txt_quick_link_three.visibility = View.VISIBLE
                            showIconQuickLinkOne(listCategoryQuickLink[i].id, img_quick_link_three, txt_quick_link_three)
                        }
                        4 -> {
                            bg_dash_four.visibility = View.GONE
                            txt_quick_link_four.visibility = View.VISIBLE
                            showIconQuickLinkOne(listCategoryQuickLink[i].id, img_quick_link_four, txt_quick_link_four)
                        }
                    }
                }
            } else {
                showIconQuickLinkDash()
            }
        } else {
            showIconQuickLinkDash()
        }
        EventBus.getDefault().postSticky(
            EventChangeQuickLink(true)
        )
    }

    private fun showIconQuickLinkOne(id: Int?, image: ImageView, textView: TextView) {
        image.visibility = View.VISIBLE
        when (id) {
            1 -> {
                image.setOnClickListener { CaffeActivity.launchIntent(requireContext()) }
                image.setImageResource(R.drawable.ic_quick_link_caffe)
                textView.text = resources.getText(R.string.cafe)
            }
            2 -> {
                image.setOnClickListener { HelpMtaActivity.launchIntent(requireContext()) }
                image.setImageResource(R.drawable.ic_quick_link_household)
                textView.text = "Househo.."
            }
            3 -> {
                image.setOnClickListener { EmploymentActivity.launchIntent(requireContext()) }
                image.setImageResource(R.drawable.ic_quick_link_employment)
                textView.text = "Employm.."
            }
            4 -> {
                image.setImageResource(R.drawable.ic_quick_link_pay)
                textView.text = "Pay Asses.."
            }
            5 -> {
                image.setImageResource(R.drawable.ic_quick_link_form)
                textView.text = resources.getText(R.string.forms)
            }
            6 -> {
                image.setOnClickListener { NewsActivity.launchIntent(requireContext()) }
                image.setImageResource(R.drawable.ic_quick_link_news)
                textView.text = "News Lett.."
            }
            7 -> {
                image.setOnClickListener { NotificationActivity.launchIntent(requireContext()) }
                image.setImageResource(R.drawable.ic_quick_link_notification)
                textView.text = "Notificati.."
            }
            8 -> {
                image.setOnClickListener { RecCenterActivity.launchIntent(requireContext()) }
                image.setImageResource(R.drawable.ic_quick_link_rec_center)
                textView.text = "Rec. Centers"
            }
            9 -> {
                image.setImageResource(R.drawable.ic_quick_link_waitlist)
                textView.text = "Waitlist.."
            }
            10 -> {
                textView.text = resources.getText(R.string.history)
                image.setImageResource(R.drawable.ic_quick_link_history)
            }
        }
    }

    private fun showIconQuickLinkDash() {
        txt_quick_link_one.visibility = View.INVISIBLE
        txt_quick_link_two.visibility = View.INVISIBLE
        txt_quick_link_three.visibility = View.INVISIBLE
        txt_quick_link_four.visibility = View.INVISIBLE

        bg_dash_one.visibility = View.VISIBLE
        bg_dash_two.visibility = View.VISIBLE
        bg_dash_three.visibility = View.VISIBLE
        bg_dash_four.visibility = View.VISIBLE
    }

    private fun showHideDialogQuickLink() {
        if (Hawk.contains(Constant.FLAG_ON_DIALOG)) {
            val isDialog: Boolean = Hawk.get(Constant.FLAG_ON_DIALOG)
            if (!isDialog) {
                openDialogQuickLink()
            }
        } else {
            openDialogQuickLink()
        }
        getCategoryQuickLink()
    }

    private fun openDialogQuickLink() {
        val fragManager: FragmentManager? = fragmentManager
        if (fragManager != null) {
            DialogConfirmationQuickLink()
                .show(fragManager, "Dialog")
        }
    }

    private fun openHousehold() {
        WebviewHouseholdActivity.launchIntent(requireContext())
    }

    private fun openHistory() {
        WebviewHistoryActivity.launchIntent(requireContext())
    }

    private fun openNewsletter() {
        NewsletterActivity.launchIntent(requireContext())
    }

    private fun openNotifications() {
        NotificationActivity.launchIntent(requireContext())
    }


    private fun openContactUs() {
        ContactUSActivity.launchIntent(requireContext())
    }


    private fun openProfile() {
        Hawk.put((Constant.FLAG_ON_BACK_MENU), false)
        ProfileMtaActivity.launchIntent(requireContext())
    }

    private fun openWaitList() {
        WebviewAssessmentActivity.launchIntent(requireContext(), "https://www2.mililanitown.org/wbwsc/webtrac.wsc/search.html?display=detail&module=PSS")
    }
}