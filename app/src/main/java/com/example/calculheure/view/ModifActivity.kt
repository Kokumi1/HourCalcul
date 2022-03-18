package com.example.calculheure.view

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculheure.R
import com.example.calculheure.model.Worksite
import java.util.*
import kotlin.collections.ArrayList

class ModifActivity : AppCompatActivity() {

    private lateinit var dayTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var roadEditText: EditText
    private lateinit var loadEditText: EditText
    private lateinit var validButton: Button
    private lateinit var toolbar: Toolbar

    private val worksiteList = ArrayList<Worksite?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modif)

        validButton = findViewById(R.id.modif_confirm)
        validButton.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.modif_recycler)
        recyclerView.adapter = ModifAdapter(worksiteList, this)
        recyclerView.layoutManager = LinearLayoutManager(this)


        //Toolbar
        toolbar = findViewById(R.id.modif_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        //menu?.getItem(R.id.toolbar_today)?.isVisible = false
        //menu?.getItem(R.id.toolbar_today)?.isEnabled = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.toolbar_home->{
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun saveNewWorksite(pWorksite: Worksite){
        worksiteList.add(pWorksite)
    }

    fun saveEditWorksite(pWorksite: Worksite,pWorksiteEdit: Worksite){
        worksiteList.remove(pWorksite)
        worksiteList.add(pWorksiteEdit)
    }

    fun deleteWorksite(pWorksite: Worksite){
        worksiteList.remove(pWorksite)
    }

    /**
     * Adapter of RecyclerView
     */
 class ModifAdapter(private val worksiteList: ArrayList<Worksite?>,val pModifActivity: ModifActivity) : RecyclerView.Adapter<ModifAdapter.ModifViewHolder>(){

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModifViewHolder {
         worksiteList.add(null)

         val view = LayoutInflater.from(parent.context)
             .inflate(R.layout.modif_cell,parent, false)
         return ModifViewHolder(view, pModifActivity)
     }

     override fun onBindViewHolder(holder: ModifViewHolder, position: Int) {
         holder.display(worksiteList[position])
         if(worksiteList.lastIndex == position){
             holder.display(null)
         }
     }

     override fun getItemCount(): Int {
         return worksiteList.count()+1
     }

        /**
         * ViewHolder of the RecyclerView Cell
         */
     class ModifViewHolder(view: View,val pModif: ModifActivity) : RecyclerView.ViewHolder(view){

         private val worksiteEditText = view.findViewById<EditText>(R.id.modif_cell_worksite)
         private val cityEditText = view.findViewById<EditText>(R.id.modif_cell_city)
         private val workEditText = view.findViewById<EditText>(R.id.modif_cell_work)
         private val morningEditText = view.findViewById<EditText>(R.id.modif_cell_morning)
         private val afternoonEditText = view.findViewById<EditText>(R.id.modif_cell_afternoon)
         private val beginEditText = view.findViewById<EditText>(R.id.modif_cell_begin_hour)
         private val endEditTex = view.findViewById<EditText>(R.id.modif_cell_end_hour)
         private val validButton = view.findViewById<ImageButton>(R.id.modif_cell_valid)
         private val eraseButton = view.findViewById<ImageButton>(R.id.modif_cell_erase)

            /**
             * display data in the cell
             */
            @SuppressLint("SetTextI18n")
            fun display(pWorksite: Worksite?){
                if(pWorksite != null) {
                    worksiteEditText.setText(pWorksite.id)
                    cityEditText.setText(pWorksite.city)
                    workEditText.setText(pWorksite.work)
                    morningEditText.setText(pWorksite.aM)
                    afternoonEditText.setText(pWorksite.pM)
                    beginEditText.setText(pWorksite.beginHour.get(Calendar.HOUR_OF_DAY))
                    endEditTex.setText(pWorksite.endHour.get(Calendar.HOUR_OF_DAY))

                    eraseButton.setOnClickListener { eraseWorksite(pWorksite) }
                }

                validButton.setOnClickListener { if(pWorksite == null) saveWorksite(true)
                else saveWorksite(false, pWorksite) }

                //add listener to editText
                beginEditText.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    val hour = calendar.get(Calendar.HOUR_OF_DAY)
                    val minute = calendar.get(Calendar.MINUTE)

                    val picker = TimePickerDialog(pModif,
                        { tp, sHour, sMinute ->
                            beginEditText.setText("$sHour : $sMinute")
                        }, hour,minute,true)
                    picker.show()
                }

                endEditTex.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    val hour = calendar.get(Calendar.HOUR_OF_DAY)
                    val minute = calendar.get(Calendar.MINUTE)

                    val picker = TimePickerDialog(pModif,
                        { tp, sHour, sMinute ->
                            endEditTex.setText("$sHour : $sMinute")
                        }, hour,minute,true)
                    picker.show()
                }
            }
            /**
             * save the edit
             */
            private fun saveWorksite(new : Boolean, pOriginal: Worksite? = null){
                 val partsBegin = beginEditText.text.toString().split(":")
                 val partsEnd = endEditTex.text.toString().split(":")

                 val beginCalendar = Calendar.getInstance()
                 val endCalendar = Calendar.getInstance()
                 beginCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(partsBegin[0]))
                 beginCalendar.set(Calendar.MINUTE, Integer.parseInt(partsBegin[1]))

                 endCalendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(partsEnd[0]))
                 endCalendar.set(Calendar.MINUTE,Integer.parseInt(partsEnd[1]))

                 val worksite = Worksite(
                     Integer.parseInt(workEditText.text.toString()),
                     cityEditText.text.toString(),
                     workEditText.text.toString(),
                     Integer.parseInt(morningEditText.text.toString()),
                     Integer.parseInt(afternoonEditText.text.toString()),
                     beginCalendar,
                     endCalendar
                 )
             if(new){
                pModif.saveNewWorksite(worksite)
             } else{
                 pModif.saveEditWorksite(pOriginal!!,worksite)
             }
            }
            /**
             * Delete Worksite
             */
            private fun eraseWorksite(pWorksite: Worksite){
                pModif.deleteWorksite(pWorksite)
            }
     }
 }
}