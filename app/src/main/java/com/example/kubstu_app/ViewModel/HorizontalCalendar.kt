package com.example.kubstu_app.ViewModel

import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.kubstu_app.Model.HorizontalDateModel
import com.example.kubstu_app.ViewModel.Adapters.AdapterForHorizontalDates
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.Year
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HorizontalCalendar(Listener: HorizontalDateItemClickListener,
                         dates_rv: RecyclerView,
                         month: TextView,
                         private var context: Context) {
    private var mFirstComleteVisibleItemPosition = -1
    private var mLastComleteVisibleItemPosition = -1
    private var mEndDate: Date? = null
    private var mMonth = DateFormatSymbols().months
    private var mStartD: Date? = null
    private lateinit var mBaseDateList: ArrayList<Date>
    private val mCal = Calendar.getInstance()
    private var dates_rv: RecyclerView? = dates_rv
    private var month: TextView? = month
    private var mFormatter = SimpleDateFormat("dd-mm-yyy", Locale("ru", "RU"))
    private lateinit var adapterForHorizontalDates: AdapterForHorizontalDates
    private val mFinalDateList = ArrayList<HorizontalDateModel>()

    init {
        horisontalDates(Listener)
    }

    private fun horisontalDates(dateItemClickListener: HorizontalDateItemClickListener) {
        mFormatter = SimpleDateFormat("dd-mm-yyy", Locale("ru", "RU"))
        mStartD = Date()
        val calendar = Calendar.getInstance()
        calendar.time = mStartD
        val currentMonth = mMonth[calendar.get(Calendar.MONTH)]
        val currentYear =  calendar.get(Calendar.YEAR)
        month?.text = "$currentMonth, $currentYear"
        calendar.add(Calendar.MONTH, -1)
        mEndDate = calendar.time
        mBaseDateList = getDate(mFormatter.format(mEndDate), mFormatter.format(mStartD))
        setAdapter(mBaseDateList, dateItemClickListener)
        val layoutManager3 =dates_rv?.layoutManager as LinearLayoutManager
        layoutManager3.scrollToPosition(mBaseDateList.size-1)
    }

    private fun setAdapter(DDates: ArrayList<Date>, dateItemClickListener: HorizontalDateItemClickListener) {
        val finalDates = ArrayList<HorizontalDateModel>()
        val clickedDate = ArrayList<String>()
        for (i in 0 until DDates.size) {
            val lDates = DDates[i]
            val c =Calendar.getInstance()
            c.time = lDates
            
            val dayOfWeek = c.get(Calendar.DATE)
            val month = c.get(Calendar.MONTH)
            val year = c.get(Calendar.YEAR)
            
            val dayLongName = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())//------------------------------------------
            finalDates.add(HorizontalDateModel(c.time, dayLongName))
            clickedDate.add(String().format("%02d-%02d-%04d", dayOfWeek, month + 1, year))
            
        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.stackFromEnd = true
        dates_rv?.layoutManager = layoutManager
        layoutManager.scrollToPosition(30)
        adapterForHorizontalDates = AdapterForHorizontalDates(finalDates, context, clickedDate, DDates, dateItemClickListener)
        dates_rv?.adapter = adapterForHorizontalDates
        dates_rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManagerForPos = recyclerView.layoutManager
                val totalItemCount = layoutManagerForPos?.itemCount
                //-------------------------------------------------------------------------------------------
                mFirstComleteVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                mLastComleteVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition() 
                //-------------------------------------------------------------------------------------------
                if (mFirstComleteVisibleItemPosition == 0) {
                    if (dy < 0 || dx < 0) {
                        if (dx < 0) {
                            val cc = Calendar.getInstance()
                            cc.time = mEndDate
                            cc.add(Calendar.MONTH, -1)
                            val temp = cc.time
                            month?.text = mMonth[cc.get(Calendar.MONTH)] + ", " + cc.get(Calendar.YEAR)
                            mBaseDateList = getDate(mFormatter.format(temp), mFormatter.format(mStartD))

                            setAdapter(mBaseDateList, dateItemClickListener)
                            mEndDate = temp
                        }
                    }
                }
                else if (totalItemCount != null) {
                    if (mLastComleteVisibleItemPosition == totalItemCount - 1) {
                        val ccc = Calendar.getInstance()
                        val date = mBaseDateList[mLastComleteVisibleItemPosition]
                        ccc.time = date
                        month?.text = mMonth[ccc.get(Calendar.MONTH)] + ", " + ccc.get(Calendar.YEAR)
                        if (dy > 0 || dx > 0){
                            if (dy > 0) {
                                Log.d("status","Scrolled Top")
                            }
                            if (dx > 0) {
                                Log.d("status","Scrolled Right")
                            }}
                    }
                }
                else {

                        if (dy > 0) {

                                val calendar = Calendar.getInstance()
                                val date = mBaseDateList[mFirstComleteVisibleItemPosition]
                                calendar.time = date
                                month?.text = mMonth[calendar.get(Calendar.MONTH)] + ", " + calendar.get(Calendar.YEAR)

                            }
                        if (dx < 0) {
                            Log.d("status","Scrolled Left")
                            val calendar = Calendar.getInstance()
                            val date = mBaseDateList[mFirstComleteVisibleItemPosition + 1]
                            calendar.time = date
                            month?.text = mMonth[calendar.get(Calendar.MONTH)] + ", " + calendar.get(Calendar.YEAR)

                        }
                }
            }
        })
        mFinalDateList.addAll(finalDates)
        for (i in finalDates.indices) {
            if (mFormatter.format(finalDates[i].date) == mFormatter.format(mCal.time)) {
                adapterForHorizontalDates.UpdatePosition(i)
            }
        }
    }
    internal fun setCalendarDate(year: Int, month: Int, DayOfMonth: Int,
                                 dateItemClickListener: HorizontalDateItemClickListener,
                                 monthText: TextView) {
        val finalDates = ArrayList<HorizontalDateModel>()
        mCal.set(Calendar.YEAR, year)
        mCal.set(Calendar.MONTH, month)
        mCal.set(Calendar.DAY_OF_MONTH, DayOfMonth)
        val calendarSelect = Calendar.getInstance()
        calendarSelect.time = mCal.time
        calendarSelect.add(Calendar.MONTH, -1)
        val endDate = calendarSelect.time
        mBaseDateList = getDate(mFormatter.format(endDate), mFormatter.format(mStartD))
        setAdapter(mBaseDateList, dateItemClickListener)
        mEndDate = endDate
        val da = "${month + 1}-$DayOfMonth-$year"
        dateItemClickListener.onDateClick(da, -1)
        if (mBaseDateList.size > 30) {
            dates_rv?.smoothScrollToPosition(33)
        } else {
            dates_rv?.smoothScrollToPosition(30)
        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.stackFromEnd = true
        dates_rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManagerForPos = recyclerView.layoutManager
                val totalItemCount = layoutManagerForPos?.itemCount
                //-------------------------------------------------------------------------------------------
                mFirstComleteVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                mLastComleteVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                //-------------------------------------------------------------------------------------------
                if (mFirstComleteVisibleItemPosition == 0) {
                    if (dy < 0 || dx < 0) {
                        if (dx < 0) {
                            val cc = Calendar.getInstance()
                            cc.time = mEndDate
                            cc.add(Calendar.MONTH, -1)
                            val temp = cc.time
                            monthText?.text = mMonth[cc.get(Calendar.MONTH)] + ", " + cc.get(Calendar.YEAR)
                            mBaseDateList = getDate(mFormatter.format(temp), mFormatter.format(mStartD))

                            setAdapter(mBaseDateList, dateItemClickListener)
                            mEndDate = temp
                        }
                    }
                }
                else if (totalItemCount != null) {
                    if (mLastComleteVisibleItemPosition == totalItemCount - 1) {
                        val ccc = Calendar.getInstance()
                        val date = mBaseDateList[mLastComleteVisibleItemPosition]
                        ccc.time = date
                        monthText?.text = mMonth[ccc.get(Calendar.MONTH)] + ", " + ccc.get(Calendar.YEAR)
                        if (dy > 0 || dx > 0){
                            if (dy > 0) {
                                Log.d("status","Scrolled Top")
                            }
                            if (dx > 0) {
                                Log.d("status","Scrolled Right")
                            }}
                    }
                }
                else {

                    if (dy > 0) {

                        val calendar = Calendar.getInstance()
                        val date = mBaseDateList[mFirstComleteVisibleItemPosition]
                        calendar.time = date
                        monthText?.text = mMonth[calendar.get(Calendar.MONTH)] + ", " + calendar.get(Calendar.YEAR)

                    }
                    if (dx < 0) {
                        Log.d("status","Scrolled Left")
                        val calendar = Calendar.getInstance()
                        val date = mBaseDateList[mFirstComleteVisibleItemPosition + 1]
                        calendar.time = date
                        monthText?.text = mMonth[calendar.get(Calendar.MONTH)] + ", " + calendar.get(Calendar.YEAR)

                    }
                }
            }
        })
        mFinalDateList.addAll(finalDates)
        for (i in finalDates.indices) {
            if (mFormatter.format(finalDates[i].date) == mFormatter.format(mCal.time)) {
                adapterForHorizontalDates.UpdatePosition(i)
            }
        }
    }

    private fun getDate(format: String, format1: String): ArrayList<Date> {
        val mDates = ArrayList<Date>()
        var startDate: Date? = null
        startDate = mFormatter.parse(format)
        var endDate: Date? = null
        endDate = mFormatter.parse(format1)
        val interval = (24 * 100 * 60 * 60).toLong()
        val endTime = endDate.time
        var currentTime = startDate.time
        while (currentTime <= endTime) {
            mDates.add(Date(currentTime))
            currentTime += interval
        }
        return mDates
    }
}