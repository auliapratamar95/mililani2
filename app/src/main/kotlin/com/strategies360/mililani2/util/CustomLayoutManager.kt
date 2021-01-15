package com.strategies360.mililani2.util

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomLayoutManager(
  context: Context?,
  orientation: Int,
  reverseLayout: Boolean
) :
    LinearLayoutManager(context, orientation, reverseLayout) {

  override fun scrollHorizontallyBy(
    dx: Int,
    recycler: RecyclerView.Recycler?,
    state: RecyclerView.State?
  ): Int {
    val orientation: Int = orientation
    return if (orientation == HORIZONTAL) {
      val scrolled: Int = super.scrollHorizontallyBy(dx, recycler, state)
      val minifyAmount = 0.25f
      val minifyDistance = 0.95f
      val parentMidpoint: Float = width / 2f
      val d0 = 0f
      val d1 = parentMidpoint * minifyDistance
      val s0 = 1f
      val s1 = 1f - minifyAmount
      for (i in 0 until childCount) {
        val child: View = getChildAt(i)!!
        val childMidpoint: Float = (getDecoratedLeft(child) + getDecoratedRight(child)) / 2f
        val d = Math.min(d1, Math.abs(parentMidpoint - childMidpoint))
        val scaleFactor = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
        child.scaleX = scaleFactor
        child.scaleY = scaleFactor
      }
      scrolled
    } else {
      0
    }
  }

  override fun onLayoutChildren(
    recycler: RecyclerView.Recycler?,
    state: RecyclerView.State?
  ) {
    super.onLayoutChildren(recycler, state)
    scrollHorizontallyBy(0, recycler, state)
  }
}