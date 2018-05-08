package com.example.dxlr8.tiptracker

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.hardware.*
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Vibrator
import android.support.v4.content.ContextCompat.startActivity
import android.text.Spannable
import com.example.dxlr8.tiptracker.R.id.*

import kotlinx.android.synthetic.main.activity_tip_tracker.*
import kotlinx.android.synthetic.main.content_tip_tracker.view.*
import java.io.File
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TipTracker : AppCompatActivity(), SensorEventListener {



    var total: Double  = 0.00
    var values = arrayListOf<Double>()
    var valuess = arrayListOf<String>()
    val contacts = arrayListOf<String>("Devon, 3036815904", "Rissa, 7202247392")
    val f = "ttotals"
    lateinit var totalbox: TextView
    lateinit var date: TextView
    lateinit var input: EditText
    lateinit var fin: Button
    lateinit var add: Button
    lateinit var clr: Button
    var msha:Long = 0
    var sm: SensorManager? = null
    var sa: Sensor? = null
    val SHAKE_SLOP_TIME_MS = 500
    var vibr = true
    var snd = true

    var time1 = false
    var time2 = false
    var time3 = false
    var time4 = false
    var time5 = false
    var rissa:Boolean? = null
    var dev:Boolean? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip_tracker)
        setSupportActionBar(toolbar)
        var first = true






        sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sa = sm!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)




clr = findViewById(R.id.clear)
        date = findViewById(R.id.date)
        input = findViewById(R.id.input)
        add = findViewById(R.id.add)
        fin = findViewById(R.id.fin)
        totalbox = findViewById(R.id.calc)
        totalbox.movementMethod = ScrollingMovementMethod()
        totalbox.append("          $$total \n")
        if(first==true){
            values.add(total)
            first = false
        }
        val c = Calendar.getInstance().time
        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)

        val currentDateandTime = sdf.format(c)

        date.setText("Date: " + currentDateandTime)


        fin.setOnClickListener(){

            writeFi()

        }

        clr.setOnClickListener(){
            values.clear()
            total = 0.00
            totalbox.setText("")
            totalbox.append("          $$total \n")
            date.setText("Date: " + currentDateandTime)
        }



        add.setOnClickListener(){

            var value1: Double = 0.00


            if(input.text.toString().isEmpty() ){
                Toast.makeText(this, "That is empty try putting in a Numeric value ##.##", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                value1 = input.text.toString().toDouble()
            }
            total += value1



            if(total >= 50 && time1 ==false){
                time1 = true
                goal()
            }else if(total>=100 &&time2 ==false){
                time2 = true
                goal()
            }else if(total>=150 &&time3 ==false){
                time3 = true
                goal()
            }else if(total>=200 &&time4 ==false){
                time4 = true
                goal()
            }else if(total>=250 &&time5 ==false){
                time5 = true
                goal()
            }

            input.text.clear()
            values.add(value1)

            printout(values)



        }




        



    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_tip_tracker, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {

             R.id.vibrate ->  {
                vibr = !vibr

                 if (vibr == true){
                     item.setIcon(R.drawable.vibrate)
                 }else{
                     item.setIcon(R.drawable.vibratenull)
                 }
                 return true
             }

            R.id.sound -> {
                snd = !snd

                if(snd == true){
                    item.setIcon(R.drawable.speakero)

                }else{
                    item.setIcon(R.drawable.speaker)
                }
                return true
            }



            R.id.recall -> {
                readFi()
                return true
            }

            R.id.clear ->{
                var fis = File(filesDir, f)
                fis.delete()
                return true
            }

            R.id.share -> {
                picDialog()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }



    private fun goal() {

        if(vibr == true){
            val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(1000)}
        if(snd ==true) {
            val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
            tone.startTone(ToneGenerator.TONE_SUP_RINGTONE, 1000)
        }
        val dia1 = AlertDialog.Builder(this)



        dia1.setTitle("Congradulations!!!")
        dia1.setMessage("That's $50 \n Keep Going!!")
        dia1.setNeutralButton("OK!") {_,_->

        }

        val dialog: AlertDialog = dia1.create()

        dialog!!.show()



    }

    private fun printout(x: ArrayList<Double>) {
        totalbox = findViewById(R.id.calc)
        totalbox.setText("")
    for( i in 0..x.size-1){
        if(i != x.size-1) {
            totalbox.append("          $" + x[i] + "\n + \n")
        }
        else{
            totalbox.append("          $" + x[i] + "\n")
        }
    }
        totalbox.append("= \n" )
        totalbox.append("          $$total" )

    }

    fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && !str.isEmpty())
            return false
        return true
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        stop@



        if (event != null) {

            var x = event.values[0]
            var y = event.values[1]
            var z = event.values[2]

            var gx = x/SensorManager.GRAVITY_EARTH
            var gy = y/SensorManager.GRAVITY_EARTH
            var gz = z/SensorManager.GRAVITY_EARTH

            var  gforce = Math.sqrt((gx*gx+gy*gy+gz*gz).toDouble())
            //Log.i("event change:","GF: $gforce")

            if(gforce>2.7f){


                    event.timestamp = 0
                    Log.i("BEFORE change:", "GF: $values")

                if(values.size ==0){
                    Toast.makeText(this,"There is nothing left to delete!",Toast.LENGTH_SHORT).show()
                    return
                }
                    values.removeAt(values.size - 1)
                    total = 0.00
                    for (i in 0..values.size - 1) {
                        total = total + values[i]
                    }
                if(total<50){
                    time1 = false
                }else if(total<100){
                    time2 = false
                }else if(total<150){
                    time3 = false
                }else if(total<200){
                    time4 = false
                }else if(total<250){
                    time5 = false
                }

                    printout(values)
                    Log.i("AFTER change:", "GF: $values")
                sm!!.unregisterListener(this)

                if(vibr == true){
                val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    v.vibrate(1000)}
                if(snd ==true) {
                    val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                    tone.startTone(ToneGenerator.TONE_DTMF_3, 1000)
                }
                sm!!.registerListener(this, sa, 100500)

                }



            //Log.i("event change:", "x:${event.values[0]} , y: ${event.values[1]}, z: ${event.values[2]}")

        }
        return
    }



    override fun onResume() {
        super.onResume()
        sm!!.registerListener(this, sa, 10000)
    }

    override fun onPause() {
        super.onPause()
        sm!!.unregisterListener(this)
    }


    fun writeFi() {

        val c = Calendar.getInstance().time
        val sdt = SimpleDateFormat("MM/dd/yyyy, hh:mm")
        val currentDateandTime2 = sdt.format(c)
        var fis = File(filesDir, f)
        var line =  "$currentDateandTime2:  $$total\n"
        fis.appendText(line)
        Toast.makeText(this," Saved: $line", Toast.LENGTH_SHORT).show()



    }

    fun readFi(){
       valuess.clear()
        val fi = File(filesDir, f)
        if (!fi.exists()){
            Toast.makeText(this,"No values yet, save a value", Toast.LENGTH_SHORT).show()
        }else {
            var s = Scanner(fi)


            while (s.hasNextLine()) {


                var ddate = s.next() + s.next()
                Log.i("DDATE=:","$ddate")
                var va = s.next().replace('$', ' ').toDouble()
                Log.i("VA=:","$va")

                s.nextLine().trim()
                var diaLine = ("$ddate $ $va")
                valuess.add(diaLine)
            }
            showTipList()
        }

    }

    private fun showTipList() {
        Log.i("array =:","${valuess.toString()}")

        val tipArray: Array<CharSequence> = valuess.toTypedArray<CharSequence>()

        Log.i("TIPARRAY=:","$tipArray")

        val dia = AlertDialog.Builder(this).setTitle("Pick a date to return to")


                .setSingleChoiceItems(tipArray,-1, DialogInterface.OnClickListener{
                    _, element:Int ->
                    tipPicked(element)
                }).create()


        dia!!.show()






    }

    private fun picDialog() {



        val dia3 = AlertDialog.Builder(this)
        val cont: Array<CharSequence> = contacts.toTypedArray<CharSequence>()


        dia3.setTitle("Pick contact")

        dia3.setSingleChoiceItems(cont,-1,DialogInterface.OnClickListener{
            _, element:Int ->
            Log.i("Before DEV=:","$dev")


            when(element){
                0 ->{
                 dev = true
                }
                1->{
                dev = false
            }
            }

            Log.i("After DEV=:","$dev")
            Log.i("After Rissa=:","$rissa")

        }).setPositiveButton("send", DialogInterface.OnClickListener{ _, _->


            if(dev == true) {
                val sndit: Intent = Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", "3036815904", null))
                sndit.putExtra("sms_body", "Hey!! I made $$total, at work today!!!")
                startActivity(sndit)

            }else{
                val sndit: Intent = Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", "7202247392", null))
                sndit.putExtra("sms_body", "Hey!! I made $$total, at work today!!!")
                startActivity(sndit)
            }
        }).create()

       dia3!!.show()





    }



    private fun tipPicked(element: Int) {

        date = findViewById(R.id.date)
        var picked = this.valuess.get(element)
        val c = Scanner(picked)

        var x = c.next()
        Log.i("XXXXXX:", "$x")
        var dte1 = ""
        for (i in 0..9) {
            dte1 = dte1 + x[i]
        }
        Log.i("DTE1:", "$dte1")
            date.setText("Date: " + dte1)

            c.next()

            total = c.nextDouble()
            values.clear()
            values.add(total)
            printout(values)


        }



}
