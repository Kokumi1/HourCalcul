package com.example.calculheure.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calculheure.model.Day
import com.example.calculheure.service.DataTalker
import java.text.SimpleDateFormat
import java.util.*

class ModifViewModel : ViewModel() {

    private var mDay: MutableLiveData<Day> = MutableLiveData()

    /**
     * get Day from DataTalker
     */
    fun getDay(pContext: Context, pCalendar: String): LiveData<Day>{
        if(mDay.value == null){
            val dataTalk = DataTalker(pContext)
            //val c = Calendar.getInstance()
            //val dateTimeForm = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())

            val day = dataTalk.getDay(pCalendar)

            mDay.postValue(day)
        }

        return mDay
    }

    /**
     * register a new Day by DataTalker
     */
    fun setDay(pDay : Day, pContext: Context){
        val dataTalker = DataTalker(pContext)
        mDay .postValue(pDay)
        dataTalker.saveDay(pDay)
    }
}