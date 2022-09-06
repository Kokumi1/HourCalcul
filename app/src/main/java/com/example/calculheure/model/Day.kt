package com.example.calculheure.model

import java.util.*

class Day(var date: Date, var travel: Int, var loading: Int, var work: String,
          var worksite: ArrayList<Worksite?>, var dayType: String = "work") { //TODO: dayType

    /**
     * Compute the number of total hour worked
     */
    fun workTime(): Int{
        var time = 0
        for(site in worksite){
            time += site!!.aM + site.pM
        }
        return time
    }
}