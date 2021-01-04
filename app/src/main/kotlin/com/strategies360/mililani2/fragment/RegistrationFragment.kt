package com.strategies360.mililani2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.strategies360.mililani2.R.layout
import com.strategies360.mililani2.fragment.core.CoreFragment

class RegistrationFragment : CoreFragment() {

  override val viewRes: Int? = layout.fragment_registration

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return super.onCreateView(inflater, container, savedInstanceState)


  }

}