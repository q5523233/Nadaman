package com.sm.nadaman.ui.fragment

import android.widget.TextView
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.blackflagbin.kcommon.base.BaseFragment
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.sm.nadaman.R
import com.sm.nadaman.common.Config
import com.sm.nadaman.common.db.HealthOpe
import com.sm.nadaman.common.db.manager.UserDBController
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.common.navigate1
import com.sm.nadaman.common.utils.CalendarUtils
import com.sm.nadaman.mvp.contract.CalendarContract
import com.sm.nadaman.mvp.presenter.CalendarPresenter
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR

class CalendarFragment : BaseFragment<ApiService, CacheService, CalendarPresenter, Any?>(),
    CalendarContract.ICalendarView, CalendarView.OnMonthChangeListener,
    CalendarView.OnCalendarSelectListener {

    var curDate = java.util.Calendar.getInstance()

    private val healthOpe: HealthOpe by lazy {
        HealthOpe.create()
    }
    val dateListFromList by lazy {
        CalendarUtils.getSchemeDateListFromList(
            healthOpe.getAllTimeList(
                UserDBController.getInstance().currentUser,
                if (Config.isSingleEcg) 0 else 1
            )
        )
    }

    private val datePickerView: TimePickerView by lazy {
        TimePickerBuilder(context) { it, _ ->
            val calendar = java.util.Calendar.getInstance()
            calendar.time = it
            calendarView.scrollToCalendar(calendar.get(YEAR), calendar.get(MONTH) + 1, 1, true)
        }
            .setDate(java.util.Calendar.getInstance())
            .setRangDate(java.util.Calendar.getInstance().apply {
                set(2015, 10, 1)
            }, java.util.Calendar.getInstance())
            .setLayoutRes(R.layout.pickerview_custom_time_btn) {
                it.findViewById<TextView>(R.id.tv_done).setOnClickListener {
                    datePickerView.returnData()
                    datePickerView.dismiss()
                }
                it.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
                    datePickerView.dismiss()
                }
            }
            .setContentTextSize(18)
            .setType(arrayOf(true, true, false, false, false, false).toBooleanArray())
            .setLabel("年", "月", "", "", "", "")
            .setLineSpacingMultiplier(2f)
            .build()
    }

    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.fragment_calendar

    override val presenter: CalendarPresenter
        get() = CalendarPresenter(this)

    override fun initView() {
        super.initView()
        if (Config.isSingleEcg.not()) {
            toolbar.setBackgroundResource(R.mipmap.toolbar_12)
        }

        calendarView.post {
            calendarView.setSchemeDate(dateListFromList)
        }
        calendarView.scrollToCurrent()
        calendarView.setRange(
            2015,
            10,
            1,
            calendarView.curYear,
            calendarView.curMonth,
            calendarView.curDay
        )
        tv_calendar_text.text = "${calendarView.curYear}年${calendarView.curMonth}月"

        calendarView.setOnMonthChangeListener(this)
        calendarView.setOnCalendarSelectListener(this)
        iv_calendar_left.setOnClickListener {
            calendarView.scrollToPre(true)
        }
        iv_calendar_right.setOnClickListener {
            calendarView.scrollToNext(true)
        }

        iv_today.setOnClickListener {
            calendarView.scrollToCurrent(true)
        }

        tv_calendar_text.setOnClickListener {
            datePickerView.setDate(curDate)
            datePickerView.show()
        }


    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        calendar?.let {
            if (it.hasScheme()) {
                val date = it.year.toString() + "-" + String.format(
                    "%02d",
                    it.month
                ) + "-" + String.format("%02d", it.day)
                findNavController1()?.navigate1(
                    CalendarFragmentDirections.actionCalendarFragmentToReportListFragment(
                        date
                    )
                )
            }
        }
    }

    override fun onCalendarOutOfRange(calendar: Calendar?) {

    }

    override fun onMonthChange(year: Int, month: Int) {
        curDate.set(year, month - 1, 1)
        tv_calendar_text.text = "${year}年${month}月"
    }
}
