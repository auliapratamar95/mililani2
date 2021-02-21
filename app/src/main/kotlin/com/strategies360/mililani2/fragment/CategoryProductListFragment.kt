package com.strategies360.mililani2.fragment

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.orhanobut.hawk.Hawk
import com.strategies360.mililani2.R
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.R.style
import com.strategies360.mililani2.adapter.recycler.CategoryProductAdapter
import com.strategies360.mililani2.model.remote.caffe.Caffe
import com.strategies360.mililani2.util.Constant
import kotlinx.android.synthetic.main.layout_bottom_category.*

class CategoryProductListFragment : BottomSheetDialogFragment() {

    private var adapter = CategoryProductAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setStyle(STYLE_NORMAL, style.BottomSheetDialogTheme)
        return inflater.inflate(layout.layout_bottom_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    override fun getTheme(): Int = style.BottomSheetDialogTheme

    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        val view = View.inflate(context, layout.layout_bottom_category, null)
        val dialog = BottomSheetDialog(requireContext(), theme)

        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }
        dialog.setContentView(view)

        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams

        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 65 / 100
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    private fun initData() {
        if (Hawk.contains(Constant.FLAG_ON_CATEGORY)) {
            val categoryList: ArrayList<Caffe> = Hawk.get(Constant.FLAG_ON_CATEGORY)
            recycler_category.setHasFixedSize(true)
            recycler_category.layoutManager = LinearLayoutManager(requireContext())

            adapter.setDataList(categoryList)
            recycler_category.adapter = adapter
        }
    }
}