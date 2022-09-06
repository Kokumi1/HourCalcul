package com.example.calculheure.service

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.calculheure.model.Day
import com.example.calculheure.model.Worksite
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DataTalker(private val mContext: Context) {

    /**
     * get SharedPreferences
     */
    private fun getSharedPreferences() : SharedPreferences{
        return mContext.getSharedPreferences("ComputeHour" , Context.MODE_PRIVATE)
    }

    /**
     * Get days data of a month
     * pDate: String version of the desired date
     */
    fun getMonthDays(pDate: String): ArrayList<Day>{

        val days = ArrayList<Day>()
        val dayCalendar = Calendar.getInstance()
        dayCalendar.time = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(pDate) as Date
        val month= dayCalendar.get(Calendar.MONTH)+1
        val year = dayCalendar.get(Calendar.YEAR)

        var i = 1
        while(i <= dayCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
            val seekDay = if(i<10) "0$i"
            else "$i"

            val seekMonth = if(month < 10) "0$month"
            else "$month"

            days.add(getDay("$seekDay/$seekMonth/$year"))

            i++
        }

        return days
    }

    /**
     * Get days data of a week
     * pDate: String version of the desired date
     */
    fun getWeekDays(pDate: String): ArrayList<Day>{

        val days = ArrayList<Day>()
        val dayCalendar = Calendar.getInstance()
        dayCalendar.time = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(pDate) as Date
        var dayOfWeek = 0


        when (dayCalendar.get(Calendar.DAY_OF_WEEK)){
            Calendar.MONDAY -> dayOfWeek = 1
            Calendar.TUESDAY -> dayOfWeek = 2
            Calendar.WEDNESDAY -> dayOfWeek = 3
            Calendar.THURSDAY -> dayOfWeek = 4
            Calendar.FRIDAY -> dayOfWeek = 5
            Calendar.SATURDAY -> dayOfWeek = 6
            Calendar.SUNDAY -> dayOfWeek = 7
        }
        val copyDay = dayOfWeek



        // Days after
        var j=0
        while (dayOfWeek < 7){
            dayOfWeek++
            j++

            val seekedDay = dayCalendar.get(Calendar.DAY_OF_MONTH) + j
            val c = Calendar.getInstance()
                c.set(dayCalendar.get(Calendar.YEAR),dayCalendar.get(Calendar.MONTH),seekedDay)

            days.add(getDay(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                c.time)
            ))
        }

        // Today
        days.add(getDay(pDate))
        dayOfWeek = copyDay

        // Days before
        var i = 0
        while (dayOfWeek > 1){
            dayOfWeek--
            i++


            val seekedDay = dayCalendar.get(Calendar.DAY_OF_MONTH) - i
            val c = Calendar.getInstance()
            c.set(dayCalendar.get(Calendar.YEAR),dayCalendar.get(Calendar.MONTH),seekedDay)

            days.add(getDay(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                c.time
            )))
        }


        return days
    }

    /**
     * get data of a single day
     * pDate: String version of the desired date
     */
    fun getDay(pDate: String): Day{
        val sp = getSharedPreferences()
        val tempDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(pDate)

        val day = Day(
            tempDate!!,
            sp.getInt(pDate + "travel", 0),
            sp.getInt(pDate + "load", 0),
            sp.getString(pDate + "work", "")!!,
            getWorksites(pDate),
            sp.getString(pDate + "type", "")!!
        )
        Log.println(Log.DEBUG,"getDate:", "$pDate Day Get:${day.worksite.size} worksite")
        return day
    }

    /**
     * Get worksite data of a day
     * pDateForm: String version of the desired date
     */
    private fun getWorksites(pDateForm: String): ArrayList<Worksite?>{
        val sp = getSharedPreferences()

        val dateTimeForm = SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",Locale.ENGLISH)
        var i = 0
        val worksiteSize = sp.getInt(pDateForm + "worksite" + "size", 0)
        val worksites = ArrayList<Worksite?>()

        while (i < worksiteSize) {
            val c1 = Calendar.getInstance()
            c1.time = dateTimeForm.parse(
                sp.getString(
                    pDateForm + "worksite" + i + "begin",
                    "Sat Feb 12 12:12:00 GMT+02:00 2022"
                )!!
            )!!
            val c2 = Calendar.getInstance()
            c2.time = dateTimeForm.parse(
                sp.getString(
                    pDateForm + "worksite" + i + "end",
                    "Sat Feb 12 12:12:00 GMT+02:00 2022"
                )!!
            )!!
            worksites.add(
                Worksite(
                    i,
                    sp.getString(pDateForm + "worksite" + i + "city", "e404")!!,
                    sp.getString(pDateForm + "worksite" + i + "work", "e404")!!,
                    sp.getInt(pDateForm + "worksite" + i + "aM", 0),
                    sp.getInt(pDateForm + "worksite" + i + "pM", 0),
                    c1, c2,
                    sp.getInt(pDateForm + "worksite" + i + "break", 0)
                )
            )

            i++
        }
        return worksites
    }

    /**
     * Register a day to sharedPreferences
     * pDay: day data to register
     */
    fun saveDay(pDay: Day): Boolean{
        val editor: SharedPreferences.Editor = getSharedPreferences().edit()
        val dateTimeForm = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateForm= dateTimeForm.format(pDay.date)

        //register day data
        editor.putString(dateForm+"date",dateForm)
        editor.putInt(dateForm+"travel",pDay.travel)
        editor.putInt(dateForm+"load",pDay.loading)
        editor.putString(dateForm+"work",pDay.work)
        editor.putString(dateForm+"type",pDay.dayType)
        editor.putInt(dateForm+"worksite"+"size",pDay.worksite.size)

        var i =0

        //register worksite data
        while(i<pDay.worksite.size){

            editor.putInt(dateForm+"worksite"+i,pDay.worksite[i]!!.id)
            editor.putString(dateForm+"worksite"+i+"city",pDay.worksite[i]!!.city)
            editor.putString(dateForm+"worksite"+i+"work",pDay.worksite[i]!!.work)
            editor.putInt(dateForm+"worksite"+i+"aM",pDay.worksite[i]!!.aM)
            editor.putInt(dateForm+"worksite"+i+"pM",pDay.worksite[i]!!.pM)

            editor.putString(dateForm+"worksite"+i+"begin",pDay.worksite[i]!!.beginHour.time.toString())
            editor.putString(dateForm+"worksite"+i+"begin",pDay.worksite[i]!!.endHour.time.toString())

            editor.putInt(dateForm+"worksite"+i+"break",pDay.worksite[i]!!.breakTime)

            i++
        }
        return try {
            editor.apply()
            Log.println(Log.DEBUG,"SaveDate:","Date Saved")
            true
        }catch (e : Exception){
            e.printStackTrace()
            false
        }
    }

}