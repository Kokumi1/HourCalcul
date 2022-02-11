package com.example.calculheure.model

import java.util.*

class Worksite(var id : Int,var city: String,var work: String,var aM: Int,var pM: Int,
               var beginHour: Calendar,var endHour: Calendar, var breakTime: Int = 0) {
    //var breakTime : Int = 0

    init {
        if(breakTime == 0){
            val endmorning = beginHour.get(Calendar.HOUR_OF_DAY)+ aM
            val beginAfter = endHour.get(Calendar.HOUR_OF_DAY)-pM
            breakTime= beginAfter - endmorning
        }
    }
}