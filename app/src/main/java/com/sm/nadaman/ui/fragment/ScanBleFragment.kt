package com.sm.nadaman.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import com.sm.nadaman.R
import com.trello.rxlifecycle2.components.RxFragment
import kotlinx.android.synthetic.main.fragment_sacn_ble.*

class ScanBleFragment : RxFragment() {
    val animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.anim_rotate).apply {
            interpolator = DecelerateInterpolator(0.5f)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_sacn_ble, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        iv_scan.startAnimation(animation)
    }

    override fun onStop() {
        super.onStop()
        animation.cancel()
    }
}