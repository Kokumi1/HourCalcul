package com.example.calculheure.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calculheure.model.Day
import com.example.calculheure.model.Worksite
import java.util.*
import kotlin.collections.ArrayList

class ModifViewModel : ViewModel() {

    private var mDay: MutableLiveData<Day> = MutableLiveData()

    fun getDay(): LiveData<Day>{
        if(mDay.value == null){
            retrieveDay()
        }

        return mDay
    }

    fun setDay(pDay : LiveData<Day>){
        mDay = pDay as MutableLiveData<Day>
    }

    private fun retrieveDay(){
        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        val dateAM = Date(2022,3,10,9,0)
        val datePM = Date(2022,3,10,16,0)
        c1.time = dateAM
        c2.time = datePM

        val workList = ArrayList<Worksite?>()
        workList.add(Worksite(0,"soulsForge","Kill",3,3, c1, c2))
        workList.add(Worksite(1,"Cathai","harmonize",3,3, c1, c2))
        val day = Day(
            Date(2022,1,10,9,0),1,1,"test",
            workList)

        mDay.postValue(day)
    }
}