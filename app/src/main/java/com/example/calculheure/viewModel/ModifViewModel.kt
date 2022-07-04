package com.example.calculheure.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calculheure.model.Day
import com.example.calculheure.model.Worksite
import com.example.calculheure.service.DataTalker
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ModifViewModel : ViewModel() {

    private var mDay: MutableLiveData<Day> = MutableLiveData()

    fun getDay(pContext: Context): LiveData<Day>{
        if(mDay.value == null){
            retrieveDay(pContext)
        }

        return mDay
    }

    fun setDay(pDay : Day, pContext: Context){
        val dataTalker = DataTalker(pContext)
        mDay .postValue(pDay)
        dataTalker.saveDay(pDay)
    }

    /*private fun retrieveDay(){
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
    }*/
    private fun retrieveDay(pContext: Context) {
        val dataTalk = DataTalker(pContext)
        val c = Calendar.getInstance()
        val dateTimeForm = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())

        //val day = dataTalk.getDay("12/02/2022")
        val day = dataTalk.getDay(dateTimeForm.format(c.time))

        mDay.postValue(day)
    }
}