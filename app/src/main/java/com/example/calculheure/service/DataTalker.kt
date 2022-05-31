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

    private fun getSharedPreferences() : SharedPreferences{
        return mContext.getSharedPreferences("" , Context.MODE_PRIVATE)
    }

    fun getMonthDays(){

    }

    fun getWeekDays(){

    }

    //fun getDay(pDate: Date): Day {
    fun getDay(pDate: String): Day{
        val sp = getSharedPreferences()
        //val dateForm = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(pDate)
        val tempDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(pDate)

        //return Day(
        val day = Day(
            //pDate,
            tempDate!!,
            sp.getInt(/*dateForm*/pDate + "travel", 0),
            sp.getInt(/*dateForm*/pDate + "load", 0),
            sp.getString(/*dateForm*/pDate + "work", "")!!,
            getWorksites(/*dateForm*/pDate),
            sp.getString(/*dateForm*/pDate + "type", "")!!
        )
        Log.println(Log.DEBUG,"getDate:", "Day Get:${day.dayType}")
        return day
    }

    private fun getWorksites(pDateForm: String): ArrayList<Worksite?>{
        val sp = getSharedPreferences()

        //val dateTimeForm = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateTimeForm = SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",Locale.ENGLISH)
        var i = 0
        val worksiteSize = sp.getInt(pDateForm + "worksite" + "size", 0)
        val worksites = ArrayList<Worksite?>()
        val c0 = sp.getString(
            pDateForm + "worksite" + i + "begin",
            "12/12/2012 12:12")!!

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

    fun saveDay(pDay: Day): Boolean{
        val editor: SharedPreferences.Editor = getSharedPreferences().edit()
        //val dateForm = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(pDay.date)
        val dateTimeForm = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateForm = "12/02/2022"
        //val dateForm= dateTimeForm.format(pDay.date)

        editor.putString(dateForm+"date",dateForm)
        editor.putInt(dateForm+"travel",pDay.travel)
        editor.putInt(dateForm+"load",pDay.loading)
        editor.putString(dateForm+"work",pDay.work)
        editor.putString(dateForm+"type",pDay.dayType)
        editor.putInt(dateForm+"worksite"+"size",pDay.worksite.size)

        var i =0

        while(i<pDay.worksite.size){
            System.err.println("begin: "+pDay.worksite[i]!!.beginHour+" end "+pDay.worksite[i]!!.endHour)

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