package com.example.stopwatch

import android.app.Dialog
import android.os.Bundle
import android.os.SystemClock
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.NumberPicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stopwatch.databinding.ActivityMainBinding
import kotlin.math.min

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var isRunning = false
    private var minutes:String? = "00:00:00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //laps List
        val lapsList = ArrayList<String>()
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lapsList)
        binding.lapList.adapter = arrayAdapter
        binding.lapBtn.setOnClickListener {
            if (isRunning){
                lapsList.add(binding.countDown.text.toString())
                arrayAdapter.notifyDataSetChanged()
            }
        }

        //Show Dialog to set time
        binding.watchIMG.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.set_time_dialog)

            //setting value of number picker
            val numberPicker = dialog.findViewById<NumberPicker>(R.id.numberPicker)
            numberPicker.minValue = 0
            numberPicker.maxValue = 5

            //Set Time from dialog to Time text
            val setTimeBtn = dialog.findViewById<Button>(R.id.setTime)
            setTimeBtn.setOnClickListener {
                minutes = numberPicker.value.toString()
                binding.setedTime.text = buildString {
                    append(numberPicker.value.toString())
                    append(" Minutes")
                }
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.stoper.setOnClickListener {
            if (!isRunning){
                isRunning = false
                if (!minutes.equals("00:00:00")){
                    val totalMin = minutes!!.toInt() * 60 *1000L  //60 sec and 1000 milisec
                    binding.countDown.base = SystemClock.elapsedRealtime() + totalMin  //Set Timer Countdown Base
                    binding.countDown.format = "%S %S"
                    binding.countDown.onChronometerTickListener = Chronometer.OnChronometerTickListener {
                        val elapsedTime = SystemClock.elapsedRealtime() - binding.countDown.base //Decrease time by 1 sec
                        //stop when set time reaches
                        if (elapsedTime>=totalMin){
                            binding.countDown.stop()
                            isRunning = false
                            binding.stoper.text = "RUN"
                        }
                    }
                }else{
                    isRunning = true
                    binding.countDown.base = SystemClock.elapsedRealtime() //stop decreasing/increasing beyond limit
                    binding.stoper.text = "STOP"
                    binding.countDown.start()
                }
            }else{
                isRunning = true
                binding.countDown.base = SystemClock.elapsedRealtime() //stop decreasing/increasing beyond limit
                binding.stoper.text = "STOP"
            }

            binding.countDown.start()
        }

        //laps List
        binding.lapList.adapter = arrayAdapter
        binding.lapBtn.setOnClickListener {
            if (isRunning){
                lapsList.add(binding.countDown.text.toString())
                arrayAdapter.notifyDataSetChanged()
            }
        }


    }
}