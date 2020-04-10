package com.sm.nadaman.mvp.presenter

import com.blackflagbin.kcommon.base.BasePresenter
import com.sm.nadaman.mvp.contract.CalendarContract
import com.sm.nadaman.mvp.model.CalendarModel

class CalendarPresenter(iCalendarView: CalendarContract.ICalendarView) :
    BasePresenter<CalendarContract.ICalendarModel, CalendarContract.ICalendarView>(iCalendarView),
    CalendarContract.ICalendarPresenter {

    override val model: CalendarContract.ICalendarModel
        get() = CalendarModel()

    override fun initData(dataMap: Map<String, String>?) {

    }
}
