package com.sm.nadaman.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sm.nadaman.R
import com.trello.rxlifecycle2.components.RxFragment

class ScanBleFragment: RxFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_sacn_ble, container, false)
    }
}