package com.sm.nadaman.common.utils

import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.sm.nadaman.common.Config
import com.sm.nadaman.ui.fragment.*


class FragmentBottomNavigation(
    val fragmentManager: FragmentManager,
    val pre_tag: String,
    @IdRes val containerIdRes: Int,
    val defIndex: Int
) {

    val CURRENT_FRAGMENT_INDEX: String = "frg_current_index"
    var mCurrentIndex: Int = 0
    fun onCreate(inState: Bundle?) {
        if (inState != null) {
            val index = inState.getInt(CURRENT_FRAGMENT_INDEX, -1)
            mCurrentIndex = if (index != -1) index else defIndex
        } else {
            mCurrentIndex = defIndex
        }
        switchFragment(mCurrentIndex)
    }

    fun hideAllFragment() {
        val fragments = fragmentManager.getFragments()

        if (fragments != null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            for (fragment in fragments!!) {
                if (fragment == null) {
                    continue
                }
                val tag = fragment?.getTag()
                if (TextUtils.isEmpty(tag)) {
                    continue
                }

                if (tag!!.startsWith(pre_tag + "_")) {
                    fragment!!.setUserVisibleHint(false)
                    fragmentTransaction.hide(fragment)
                }
            }

            fragmentTransaction.commitAllowingStateLoss()
        }
    }


    fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_FRAGMENT_INDEX, mCurrentIndex)
    }

    fun switchFragment(index: Int) {
        hideAllFragment()
        var fragment = fragmentManager.findFragmentByTag(tag(index))
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (null == fragment) {
            fragment = getFragment(index)
            fragmentTransaction.add(containerIdRes, fragment, tag(index))
        } else {
            fragmentTransaction.show(fragment)
        }
        fragment?.setUserVisibleHint(true)
        mCurrentIndex = index
        fragmentTransaction.commitAllowingStateLoss()
    }

    //偷懒写法....
    private fun getFragment(index: Int): Fragment {
        var frg: Fragment?
        when (index) {
            0 -> frg = if (Config.isSingleEcg) EcgMeasureFragment() else Ecg12MeasureFragment()
            1 -> frg = CalendarMainFragment()
            3 -> frg = FollowMainFragment()
            4 -> frg = MyMainFragment()
            else -> throw IndexOutOfBoundsException("getFragment index out of size")
        }
        return frg
    }

    private fun tag(index: Int): String {
        return "${pre_tag}_${index}"
    }
}