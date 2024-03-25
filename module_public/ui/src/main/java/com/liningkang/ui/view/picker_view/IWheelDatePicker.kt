package com.liningkang.ui.view.picker_view

import android.widget.TextView
import java.util.Date


interface IWheelDatePicker {
    fun setOnDateSelectedListener(listener: WheelDatePicker.OnDateSelectedListener?)

    fun getCurrentDate(): Date?

    fun getItemAlignYear(): Int

    fun setItemAlignYear(align: Int)

    fun getItemAlignMonth(): Int

    fun setItemAlignMonth(align: Int)

    fun getItemAlignDay(): Int

    fun setItemAlignDay(align: Int)

    fun getWheelYearPicker(): WheelYearPicker?

    fun getWheelMonthPicker(): WheelMonthPicker?

    fun getWheelDayPicker(): WheelDayPicker?

    fun getTextViewYear(): TextView?

    fun getTextViewMonth(): TextView?

    fun getTextViewDay(): TextView?
}