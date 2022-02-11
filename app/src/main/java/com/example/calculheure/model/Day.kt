package com.example.calculheure.model

import java.time.LocalDate
import java.util.*

class Day(var date: Date, var travel: Int, var loading: Int, var work: String, var worksite: ArrayList<Worksite>) {

    fun workTime(): Int{
        var time = 0
        for(site in worksite){
            time += site.endHour.get(Calendar.HOUR_OF_DAY) - site.beginHour.get(Calendar.HOUR_OF_DAY) - site.breakTime
        }
        return time
    }
}