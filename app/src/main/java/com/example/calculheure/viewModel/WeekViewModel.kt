package com.example.calculheure.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calculheure.model.Day
import com.example.calculheure.service.DataTalker
import java.text.SimpleDateFormat
import java.util.*

class WeekViewModel : ViewModel() {

    private var mDays: MutableLiveData<List<Day>> = MutableLiveData()

    /**
     * get days from DataTalker
     */
    fun getDays(pContext: Context, pDate: Date): MutableLiveData<List<Day>>{
        val dataTalk = DataTalker(pContext)

        val today = Calendar.getInstance()
        Log.d("weekView","selected date: $pDate")
        val days = dataTalk.getWeekDays(
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).
            format(pDate))

        mDays.postValue(days)
        return mDays
    }

    private fun retrieveDays(pContext: Context, pDate: Date){
        val dataTalk = DataTalker(pContext)

        val today = Calendar.getInstance()
        Log.d("weekView","selected date: $pDate")
        val days = dataTalk.getWeekDays(
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).
        format(pDate))

        mDays.postValue(days)
    }
}