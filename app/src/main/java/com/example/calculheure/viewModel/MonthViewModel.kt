package com.example.calculheure.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calculheure.model.Day
import com.example.calculheure.model.Worksite
import java.util.*
import kotlin.collections.ArrayList

class MonthViewModel :ViewModel() {

    private var mDays : MutableLiveData<List<Day>> = MutableLiveData()

    fun getDay(): LiveData<List<Day>>{
        createDay()
        return mDays
    }

    fun createDay(){
        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        val dateAM = Date(2022,1,10,9,0)
        val datePM = Date(2022,1,10,16,0)
        c1.time = dateAM
        c2.time = datePM

        val workList = ArrayList<Worksite?>()
        workList.add(Worksite(0,"soulsForge","Kill",3,3, c1, c2))
        workList.add(Worksite(1,"Cathai","harmonize",3,3, c1, c2))
        val day = Day(
            Date(2022,1,10,9,0),1,1,"test",workList)

        val list = ArrayList<Day>()
        list.add(day)

        mDays.postValue(list)
    }
}