package com.example.calculheure.model

import java.util.*

class Day(var date: Date, var travel: Int, var loading: Int, var work: String,
          var worksite: ArrayList<Worksite>, var dayType: String = "work") {

    fun workTime(): Int{
        var time = 0
        for(site in worksite){
            time += site.aM + site.pM
        }
        return time
    }
}