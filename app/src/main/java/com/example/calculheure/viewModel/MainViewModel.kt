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

class MainViewModel : ViewModel() {

        private var mDays : MutableLiveData<List<Day>> = MutableLiveData()

    /**
     * get days from dataTalker
     */
    fun getDays(pContext: Context) : LiveData<List<Day>>{
        val dataTalk = DataTalker(pContext)

        Log.d("main retrieve day: ","date looking: " +
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(Calendar.getInstance().time)
        )
        val days = dataTalk.getMonthDays(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(Calendar.getInstance().time))

        mDays.postValue(days)
        return mDays
    }
}