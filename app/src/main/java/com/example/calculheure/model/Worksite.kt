package com.example.calculheure.model

import java.util.*

class Worksite(var id : Int,var city: String,var work: String,var aM: Int,var pM: Int,
               var beginHour: Calendar,var endHour: Calendar, var breakTime: Int = 0) {

    /**
     * Compute the breakTime
     */
    init {
        if (breakTime == 0) {
            val endMorning = beginHour.get(Calendar.HOUR_OF_DAY) + aM
            val beginAfter = endHour.get(Calendar.HOUR_OF_DAY) - pM
            breakTime = beginAfter - endMorning
        }
    }
}
