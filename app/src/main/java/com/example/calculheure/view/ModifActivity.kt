package com.example.calculheure.view

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculheure.R
import com.example.calculheure.model.Day
import com.example.calculheure.model.Worksite
import com.example.calculheure.viewModel.ModifViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ModifActivity : AppCompatActivity() {

    private lateinit var dayTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var roadEditText: EditText
    private lateinit var loadEditText: EditText
    private lateinit var validButton: Button
    private lateinit var toolbar: Toolbar

    private lateinit var mModifViewModel: ModifViewModel

    private var worksiteList = ArrayList<Worksite?>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modif)

        dayTextView = findViewById(R.id.modif_day)
        roadEditText = findViewById(R.id.modif_road)
        loadEditText = findViewById(R.id.modif_load)


        //RecyclerView
        recyclerView = findViewById(R.id.modif_recycler)
        val modifAdapter = ModifAdapter(worksiteList,this)
        recyclerView.adapter = modifAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //View Model
        mModifViewModel = ViewModelProvider(this).get(ModifViewModel::class.java)
        lateinit var data : Day
        mModifViewModel.getDay(this).observe(this){
            kotlin.run {
                data = it
                worksiteList = it.worksite
                modifAdapter.notifyDataSetChanged()
                modifAdapter.resetWorksite(worksiteList)
                Log.d("recycler","data changed ${data.worksite.size}")
                dayTextView.text = SimpleDateFormat("dd/MM/yyyy",Locale.getDefault()).format(it.date)
                roadEditText.setText(it.travel.toString())
                loadEditText.setText(it.loading.toString())
            }
        }
        //Button
        validButton = findViewById(R.id.modif_confirm)
        validButton.setOnClickListener {
            data.loading = loadEditText.text.toString().toInt()
            data.travel = roadEditText.text.toString().toInt()
            mModifViewModel.setDay(data,this)
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        //Toolbar
        toolbar = findViewById(R.id.modif_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    /**
     * toolbar
     */
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

    /**
     * save a worksite
     */
    fun saveNewWorksite(pWorksite: Worksite){
        worksiteList.add(pWorksite)
        Toast.makeText(this,"Worksite Save",Toast.LENGTH_SHORT).show()
    }

    /**
     * save a existing worksite
     */
    fun saveEditWorksite(pWorksite: Worksite,pWorksiteEdit: Worksite){
        worksiteList.remove(pWorksite)
        worksiteList.add(pWorksiteEdit)
        Toast.makeText(this,"Worksite Save",Toast.LENGTH_SHORT).show()
    }

    /**
     * delete a existing worksite
     */
    fun deleteWorksite(pWorksite: Worksite){
        worksiteList.remove(pWorksite)
    }




    /**
     * Adapter of RecyclerView
     */
 class ModifAdapter(private var worksiteList: ArrayList<Worksite?>, private val pModifActivity: ModifActivity) : RecyclerView.Adapter<ModifAdapter.ModifViewHolder>(){

        var listWorksite = worksiteList.size
        init {
            listWorksite += 1
        }

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
         Log.d("recycler","actual size: $listWorksite real size: ${worksiteList.size}")
         return listWorksite
     }

        public fun resetWorksite( pWorksiteList: ArrayList<Worksite?>){
            this.worksiteList = pWorksiteList
            listWorksite = worksiteList.size + 1
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
         private val endEditText = view.findViewById<EditText>(R.id.modif_cell_end_hour)
         private val validButton = view.findViewById<ImageButton>(R.id.modif_cell_valid)
         private val eraseButton = view.findViewById<ImageButton>(R.id.modif_cell_erase)

            /**
             * display data in the cell
             */
            @SuppressLint("SetTextI18n")
            fun display(pWorksite: Worksite?){
                if(pWorksite != null) {
                    Log.d("recycler view holder","worksite show: no${pWorksite.id}")
                    worksiteEditText.setText(pWorksite.id.toString())
                    cityEditText.setText(pWorksite.city)
                    workEditText.setText(pWorksite.work)
                    morningEditText.setText(pWorksite.aM.toString())
                    afternoonEditText.setText(pWorksite.pM.toString())
                    beginEditText.setText(pWorksite.beginHour.get(Calendar.HOUR_OF_DAY).toString())
                    endEditText.setText(pWorksite.endHour.get(Calendar.HOUR_OF_DAY).toString())

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

                endEditText.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    val hour = calendar.get(Calendar.HOUR_OF_DAY)
                    val minute = calendar.get(Calendar.MINUTE)

                    val picker = TimePickerDialog(pModif,
                        { _, sHour, sMinute ->
                            endEditText.setText("$sHour : $sMinute")
                        }, hour,minute,true)
                    picker.show()
                }
            }

            /**
             * save the edit
             */
            private fun saveWorksite(new : Boolean, pOriginal: Worksite? = null){
                 val partsBegin = beginEditText.text.toString().split(" : ")
                 val partsEnd = endEditText.text.toString().split(" : ")

                 val beginCalendar = Calendar.getInstance()
                 val endCalendar = Calendar.getInstance()
                 beginCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(partsBegin[0]))
                 beginCalendar.set(Calendar.MINUTE, Integer.parseInt(partsBegin[1]))

                 endCalendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(partsEnd[0]))
                 endCalendar.set(Calendar.MINUTE,Integer.parseInt(partsEnd[1]))

                 val worksite = Worksite(
                     Integer.parseInt(worksiteEditText.text.toString()),
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
