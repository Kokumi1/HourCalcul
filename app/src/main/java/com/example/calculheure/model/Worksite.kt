package com.example.calculheure.model

import java.util.*

class Worksite(id : Int, city: String, work: String, aM: Int, pM: Int,
               beginHour: Calendar, endHour: Calendar, var breakTime: Int = 0) {
    //var breakTime : Int = 0

    init {
        if(breakTime == 0){
            val endmorning = beginHour.get(Calendar.HOUR_OF_DAY)+ aM
            val beginAfter = endHour.get(Calendar.HOUR_OF_DAY)-pM
            breakTime= beginAfter - endmorning
        }
    }
}