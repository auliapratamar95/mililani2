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
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_setting_mta_card.*
import kotlinx.android.synthetic.main.fragment_setting_mta_card.btn_edit_quick_link
import kotlinx.android.synthetic.main.fragment_setting_mta_card.btn_open_profile
import kotlinx.android.synthetic.main.fragment_setting_mta_card.btn_scan_barcode
import kotlinx.android.synthetic.main.layout_menu_quick_link.*
import org.greenrobot.eventbus.EventBus

class SampleSettingMtaFragment : CoreFragment(), View.OnClickListener {

    private var tempListDataQuickLink = ArrayList<CategoryQuickLink>()

    private val cartViewModel by lazy {
        ViewModelProviders.of(this)
            .get(CartListViewModel::class.java)
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this)
            .get(CaffeListViewModel::class.java)
    }

    override val viewRes = R.layout.fragment_setting_mta_card

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
            layout_setting.visibility = View.VISIBLE
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
                openWaitList()
            }
        }
    }

    private fun onClickOtherService() {
        btn_forms.setOnClickListener {
//            FormsActivity.launchIntent(requireContext())
            saveCategoryQuickLink(5, "form")
        }

        btn_employment.setOnClickListener {
//            EmploymentActivity.launchIntent(requireContext())
            saveCategoryQuickLink(3, "employment")
        }

        btn_contact_us.setOnClickListener {
//            openContactUs()
            saveCategoryQuickLink(2, "contact")
        }

        btn_newsletters.setOnClickListener {
//            openNewsletter()
            saveCategoryQuickLink(6, "news")
        }

        btn_history.setOnClickListener {
//            openHistory()
            saveCategoryQuickLink(10, "history")
        }

        btn_rec_Center.setOnClickListener {
//            RecCenterActivity.launchIntent(requireContext())
            saveCategoryQuickLink(8, "recreation")
        }

        btn_assessment.setOnClickListener {
//            AsessmentActivity.launchIntent(requireContext())
            saveCategoryQuickLink(4, "payment")
        }

        btn_caffe.setOnClickListener {
//            Hawk.delete(Constant.REQUIRED_CHOICE_PRODUCT)
//            CaffeActivity.launchIntent(requireContext())
            saveCategoryQuickLink(1, "caffe")
        }

        btn_news.setOnClickListener {
//            NewsActivity.launchIntent(requireContext())
            saveCategoryQuickLink(6, "news")
        }

        btn_notifications.setOnClickListener {
            saveCategoryQuickLink(7, "notification")
            openNotifications()
        }

        btn_edit_quick_link.setOnClickListener {
            if (btn_edit_quick_link.text.equals("Save")) {
                BottomMenuNavigationActivity.launchIntent(requireContext())
                hideIconQuickLink()
            } else {
                showHideIconQuickLink()
            }
        }
    }

    private fun hideIconQuickLink() {
//        btn_edit_quick_link.text = "Edit"

        btn_delete_quick_link_one.visibility = View.GONE
        btn_delete_quick_link_two.visibility = View.GONE
        btn_delete_quick_link_three.visibility = View.GONE
        btn_delete_quick_link_four.visibility = View.GONE
    }

    private fun showHideIconQuickLink() {
        if (Hawk.contains(Constant.KEY_CATEGORY_QUICK_LINK_LIST)) {
            val listCategoryQuickLink: ArrayList<CategoryQuickLink> = Hawk.get(Constant.KEY_CATEGORY_QUICK_LINK_LIST)
            btn_edit_quick_link.text = "Save"
            when (listCategoryQuickLink.size) {
                0 -> {
                    showIconQuickLink()
                }
                1 -> {
                    btn_delete_quick_link_one.visibility = View.VISIBLE
                }
                2 -> {
                    btn_delete_quick_link_one.visibility = View.VISIBLE
                    btn_delete_quick_link_two.visibility = View.VISIBLE
                }
                3 -> {
                    btn_delete_quick_link_one.visibility = View.VISIBLE
                    btn_delete_quick_link_two.visibility = View.VISIBLE
                    btn_delete_quick_link_three.visibility = View.VISIBLE
                }
                4 -> {
                    btn_delete_quick_link_one.visibility = View.VISIBLE
                    btn_delete_quick_link_two.visibility = View.VISIBLE
                    btn_delete_quick_link_three.visibility = View.VISIBLE
                    btn_delete_quick_link_four.visibility = View.VISIBLE
                }
            }
        } else {
            hideIconQuickLink()
        }
    }

    private fun showIconQuickLink() {
        btn_delete_quick_link_one.visibility = View.VISIBLE
        btn_delete_quick_link_two.visibility = View.VISIBLE
        btn_delete_quick_link_three.visibility = View.VISIBLE
        btn_delete_quick_link_four.visibility = View.VISIBLE
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
        EventBus.getDefault().postSticky(
            EventChangeQuickLink(true)
        )
        showHideDialogQuickLink()
    }

    private fun getCategoryQuickLink() {
        if (Hawk.contains(Constant.KEY_CATEGORY_QUICK_LINK_LIST)) {
            val listCategoryQuickLink: ArrayList<CategoryQuickLink> = Hawk.get(Constant.KEY_CATEGORY_QUICK_LINK_LIST)
            tempListDataQuickLink.clear()
            tempListDataQuickLink.addAll(listCategoryQuickLink)
            if (tempListDataQuickLink.size < 4 || tempListDataQuickLink.size == 4) {
                hideIconQuickLinkDash()
                hideIconQuickLink()
                for (i in tempListDataQuickLink.indices) {
                    when (i + 1) {
                        1 -> {
                            var isDeleteQuickCategoryOne = false
                            if (btn_delete_quick_link_one.visibility == View.VISIBLE) isDeleteQuickCategoryOne = true

                            bg_dash_one.visibility = View.GONE
                            txt_quick_link_one.visibility = View.VISIBLE
                            showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_one, txt_quick_link_one, 1)
                        }
                        2 -> {
                            var isDeleteQuickCategoryTwo = false
                            if (btn_delete_quick_link_two.visibility == View.VISIBLE) isDeleteQuickCategoryTwo = true
                            bg_dash_two.visibility = View.GONE
                            txt_quick_link_one.visibility = View.VISIBLE
                            showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_two, txt_quick_link_two,2)
                        }
                        3 -> {
                            var isDeleteQuickCategoryThree = false
                            if (btn_delete_quick_link_three.visibility == View.VISIBLE) isDeleteQuickCategoryThree = true
                            bg_dash_three.visibility = View.GONE
                            txt_quick_link_one.visibility = View.VISIBLE
                            showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_three, txt_quick_link_three, 3)
                        }
                        4 -> {
                            var isDeleteQuickCategoryFour = false
                            if (btn_delete_quick_link_four.visibility == View.VISIBLE) isDeleteQuickCategoryFour = true
                            bg_dash_four.visibility = View.GONE
                            txt_quick_link_one.visibility = View.VISIBLE
                            showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_four, txt_quick_link_four, 4)
                        }
                    }
                }
            } else {
                showIconQuickLinkDash()
            }
        }
    }

    private fun hideIconQuickLinkDash() {
        txt_quick_link_one.visibility = View.INVISIBLE
        txt_quick_link_two.visibility = View.INVISIBLE
        txt_quick_link_three.visibility = View.INVISIBLE
        txt_quick_link_four.visibility = View.INVISIBLE

        img_quick_link_one.visibility = View.GONE
        img_quick_link_two.visibility = View.GONE
        img_quick_link_four.visibility = View.GONE
        img_quick_link_three.visibility = View.GONE

        bg_dash_one.visibility = View.VISIBLE
        bg_dash_two.visibility = View.VISIBLE
        bg_dash_three.visibility = View.VISIBLE
        bg_dash_four.visibility = View.VISIBLE
    }

    private fun showIconQuickLinkOne(id: Int?, image: ImageView, textView: TextView, index: Int) {
        image.visibility = View.VISIBLE
        textView.visibility = View.VISIBLE
        when (id) {
            1 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteQuickLink(id)
                    } else {
                        CaffeActivity.launchIntent(requireContext())
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_caffe)
                textView.text = resources.getText(R.string.cafe)
            }
            2 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteQuickLink(id)
                    } else {
                        HelpMtaActivity.launchIntent(requireContext())
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_household)
                textView.text = "Househo.."
            }
            3 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteQuickLink(id)
                    } else {
                        EmploymentActivity.launchIntent(requireContext())
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_employment)
                textView.text = "Employm.."
            }
            4 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteQuickLink(id)
                    } else {
                        WebviewAssessmentActivity.launchIntent(requireContext(), "https://www2.mililanitown.org/wbwsc/webtrac.wsc/search.html?display=detail&module=PSS")
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_pay)
                textView.text = "Pay Asses.."
            }
            5 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteQuickLink(id)
                    } else {
                        deleteQuickLink(id)
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_form)
                textView.text = resources.getText(R.string.forms)
            }
            6 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteQuickLink(id)
                    } else {
                        NewsActivity.launchIntent(requireContext())
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_news)
                textView.text = "News Lett.."
            }
            7 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteQuickLink(id)
                    } else {
                        NotificationActivity.launchIntent(requireContext())
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_notification)
                textView.text = "Notificati.."
            }
            8 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteQuickLink(id)
                    } else {
                        RecCenterActivity.launchIntent(requireContext())
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_rec_center)
                textView.text = "Rec. Centers"
            }
            9 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteQuickLink(id)
                    } else {
                        RecCenterActivity.launchIntent(requireContext())
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_waitlist)
                textView.text = "Waitlist.."
            }
            10 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteQuickLink(id)
                    } else {
                        WebviewHistoryActivity.launchIntent(requireContext())
                    }
                }
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

    private fun openNotifications() {
        NotificationActivity.launchIntent(requireContext())
    }

    private fun openNewsletter() {
        NewsletterActivity.launchIntent(requireContext())
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

    private fun getCaffe(index: Int): Boolean {
        var isDeleteQuickLink = false

        if (index == 1) {
            if (btn_delete_quick_link_one.visibility == View.VISIBLE) isDeleteQuickLink = true
        } else if (index == 2) {
            if (btn_delete_quick_link_two.visibility == View.VISIBLE) isDeleteQuickLink = true
        } else if (index == 3) {
            if (btn_delete_quick_link_three.visibility == View.VISIBLE) isDeleteQuickLink = true
        } else if (index == 4) {
            if (btn_delete_quick_link_four.visibility == View.VISIBLE) isDeleteQuickLink = true
        }
        return isDeleteQuickLink
    }

    private fun deleteQuickLink(id: Int) {
        if (Hawk.contains(Constant.KEY_CATEGORY_QUICK_LINK_LIST)) {
            val listCategoryQuickLink: ArrayList<CategoryQuickLink> = Hawk.get(Constant.KEY_CATEGORY_QUICK_LINK_LIST)
            val tmpListQuickLink : ArrayList<CategoryQuickLink> = ArrayList()

            for (i in listCategoryQuickLink.indices) {
                if (listCategoryQuickLink[i].id != id) {
                    tmpListQuickLink.add(listCategoryQuickLink[i])
                }
            }
            Hawk.delete(Constant.KEY_CATEGORY_QUICK_LINK_LIST)
            Hawk.put((Constant.KEY_CATEGORY_QUICK_LINK_LIST), tmpListQuickLink)
            getCategoryQuickLink()
        }
    }

    private fun showTempIconQuickLinkOne(id: Int?, image: ImageView, textView: TextView, index: Int) {
        image.visibility = View.VISIBLE
        textView.visibility = View.VISIBLE
        when (id) {
            1 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteTempQuickLink(id, index)
                    } else {
                        CaffeActivity.launchIntent(requireContext())
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_caffe)
                textView.text = resources.getText(R.string.cafe)
            }
            2 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteTempQuickLink(id, index)
                    } else {
                        HelpMtaActivity.launchIntent(requireContext())
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_household)
                textView.text = "Househo.."
            }
            3 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteTempQuickLink(id, index)
                    } else {
                        EmploymentActivity.launchIntent(requireContext())
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_employment)
                textView.text = "Employm.."
            }
            4 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteTempQuickLink(id, index)
                    } else {
                        WebviewAssessmentActivity.launchIntent(requireContext(), "https://www2.mililanitown.org/wbwsc/webtrac.wsc/search.html?display=detail&module=PSS")
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_pay)
                textView.text = "Pay Asses.."
            }
            5 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteTempQuickLink(id, index)
                    } else {
                        deleteTempQuickLink(id, index)
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_form)
                textView.text = resources.getText(R.string.forms)
            }
            6 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteTempQuickLink(id, index)
                    } else {
                        NewsActivity.launchIntent(requireContext())
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_news)
                textView.text = "News Lett.."
            }
            7 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteTempQuickLink(id, index)
                    } else {
                        NotificationActivity.launchIntent(requireContext())
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_notification)
                textView.text = "Notificati.."
            }
            8 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteTempQuickLink(id, index)
                    } else {
                        RecCenterActivity.launchIntent(requireContext())
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_rec_center)
                textView.text = "Rec. Centers"
            }
            9 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteTempQuickLink(id, index)
                    } else {
                        RecCenterActivity.launchIntent(requireContext())
                    }
                }
                image.setImageResource(R.drawable.ic_quick_link_waitlist)
                textView.text = "Waitlist.."
            }
            10 -> {
                image.setOnClickListener {
                    if (getCaffe(index)) {
                        deleteTempQuickLink(id, index)
                    } else {
                        WebviewHistoryActivity.launchIntent(requireContext())
                    }
                }
                textView.text = resources.getText(R.string.history)
                image.setImageResource(R.drawable.ic_quick_link_history)
            }
        }
    }

    private fun getTempCategoryQuickLink() {
        if (tempListDataQuickLink.size < 4 || tempListDataQuickLink.size == 4) {
            hideIconQuickLinkDash()
            for (i in tempListDataQuickLink.indices) {
                when (i + 1) {
                    1 -> {
                        var isDeleteQuickCategoryOne = false
                        if (btn_delete_quick_link_one.visibility == View.VISIBLE) isDeleteQuickCategoryOne = true

                        bg_dash_one.visibility = View.GONE
                        txt_quick_link_one.visibility = View.VISIBLE
                        showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_one, txt_quick_link_one, 1)
                    }
                    2 -> {
                        var isDeleteQuickCategoryTwo = false
                        if (btn_delete_quick_link_two.visibility == View.VISIBLE) isDeleteQuickCategoryTwo = true
                        bg_dash_two.visibility = View.GONE
                        txt_quick_link_one.visibility = View.VISIBLE
                        showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_two, txt_quick_link_two,2)
                    }
                    3 -> {
                        var isDeleteQuickCategoryThree = false
                        if (btn_delete_quick_link_three.visibility == View.VISIBLE) isDeleteQuickCategoryThree = true
                        bg_dash_three.visibility = View.GONE
                        txt_quick_link_one.visibility = View.VISIBLE
                        showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_three, txt_quick_link_three, 3)
                    }
                    4 -> {
                        var isDeleteQuickCategoryFour = false
                        if (btn_delete_quick_link_four.visibility == View.VISIBLE) isDeleteQuickCategoryFour = true
                        bg_dash_four.visibility = View.GONE
                        txt_quick_link_one.visibility = View.VISIBLE
                        showTempIconQuickLinkOne(tempListDataQuickLink[i].id, img_quick_link_four, txt_quick_link_four, 4)
                    }
                }
            }
        } else {
            showIconQuickLinkDash()
        }
    }

    private fun deleteDataQuickLink() {
        Hawk.delete(Constant.KEY_CATEGORY_QUICK_LINK_LIST)
        Hawk.put((Constant.KEY_CATEGORY_QUICK_LINK_LIST), tempListDataQuickLink)
        hideIconQuickLink()
        btn_edit_quick_link.text = "Edit"
    }

    private fun deleteTempQuickLink(id: Int, index: Int) {
        if (tempListDataQuickLink.size != 0) {
            val tmpListQuickLink : ArrayList<CategoryQuickLink> = ArrayList()

            for (i in tempListDataQuickLink.indices) {
                if (tempListDataQuickLink[i].id != id) {
                    tmpListQuickLink.add(tempListDataQuickLink[i])
                }
            }
            hideIconQuickLink(index)
            tempListDataQuickLink.clear()
            tempListDataQuickLink.addAll(tmpListQuickLink)
            getTempCategoryQuickLink()
        }
    }

    private fun hideIconQuickLink(index: Int) {
        when (index) {
            1 -> {
                btn_delete_quick_link_one.visibility = View.GONE
            }
            2 -> {
                btn_delete_quick_link_two.visibility = View.GONE
            }
            3 -> {
                btn_delete_quick_link_three.visibility = View.GONE
            }
            else -> {
                btn_delete_quick_link_four.visibility = View.GONE
            }
        }
    }
}