package com.example.calculheure

import com.example.calculheure.model.Worksite
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun breakWorkSiteTest(){
        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        val dateAM = Date(2022,1,10,9,0)
        val datePM = Date(2022,1,10,16,0)
        c1.time = dateAM
        c2.time = datePM
        val workSite = Worksite(1,"soulsForge","Kill",3,3,
        c1,c2)

        assertEquals(1,workSite.breakTime)
    }
}