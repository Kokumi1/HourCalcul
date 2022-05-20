package com.example.calculheure

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.calculheure.model.Day
import com.example.calculheure.model.Worksite
import com.example.calculheure.service.DataTalker

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.calculheure", appContext.packageName)
    }

    @Test
    fun saveDayTest(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dataTalk = DataTalker(appContext)

        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        val dateAM = Date(2022,2,10,9,0)
        val datePM = Date(2022,2,10,16,0)
        c1.time = dateAM
        c2.time = datePM

        val workList = ArrayList<Worksite?>()
        workList.add(Worksite(0,"soulsForge","Kill",3,3, c1, c2))
        workList.add(Worksite(1,"Cathai","harmonize",3,3, c1, c2))
        val day = Day(
            Date(2022,2,10,9,0),1,1,"test",
            workList)

        assertTrue(dataTalk.saveDay(day))
    }

    @Test
    fun loadDayTest(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dataTalk = DataTalker(appContext)

        saveForTest()
        //val day = dataTalk.getDay(Date(2022,2,10))
        val day = dataTalk.getDay("12/02/2022")
        assertEquals("test",day.work)
    }

    private fun saveForTest(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dataTalk = DataTalker(appContext)

        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        val dateAM = Date(2022,2,10,9,0)
        val datePM = Date(2022,2,10,16,0)
        c1.time = dateAM
        c2.time = datePM

        val workList = ArrayList<Worksite?>()
        workList.add(Worksite(0,"soulsForge","Kill",3,3, c1, c2))
        workList.add(Worksite(1,"Cathai","harmonize",3,3, c1, c2))
        val day = Day(
            Date(2022,2,10,9,0),1,1,"test",
            workList)

        dataTalk.saveDay(day)
    }
}