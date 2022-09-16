package com.example.calculheure.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calculheure.model.Day
import com.example.calculheure.service.DataTalker
import java.text.SimpleDateFormat
import java.util.*

class MonthViewModel :ViewModel() {

    private var mDays : MutableLiveData<List<Day>> = MutableLiveData()

    /**
     * Get Days from DataTalker
     */
    fun getDay(pContext: Context, pDay: Calendar): LiveData<List<Day>>{
        val dataTalk = DataTalker(pContext)

        Log.d("monthView","selected date: ${pDay.time}")
        val days = dataTalk.getMonthDays(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).
        format(pDay.time))

        mDays.postValue(days)
        return mDays
    }
}