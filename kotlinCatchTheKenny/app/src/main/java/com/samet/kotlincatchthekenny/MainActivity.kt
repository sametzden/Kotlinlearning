package com.samet.kotlincatchthekenny

import android.app.Activity
import android.content.DialogInterface
import android.os.Binder
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.renderscript.ScriptGroup.Binding
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.samet.kotlincatchthekenny.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var score =0
    var imageArray = ArrayList<ImageView>()
    var runnable = Runnable {  }
    var handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageArray.add(binding.imageView)
        imageArray.add(binding.imageView2)
        imageArray.add(binding.imageView3)
        imageArray.add(binding.imageView4)
        imageArray.add(binding.imageView5)
        imageArray.add(binding.imageView6)
        imageArray.add(binding.imageView7)
        imageArray.add(binding.imageView8)
        imageArray.add(binding.imageView9)
        hideKenny()

        //countdowntimer
        object :CountDownTimer(15000,1000){
            override fun onTick(p0: Long) {
                binding.timeText.text="Time: ${p0/1000}"
            }

            override fun onFinish() {
                binding.timeText.text ="Time: 0"
                handler.removeCallbacks(runnable)
                for (image in imageArray){
                    image.visibility= View.INVISIBLE
                }

                // alert dialog
                val alert = AlertDialog.Builder(this@MainActivity)
                alert.setTitle("GAME OVER !!")
                alert.setMessage("Restart the game ??")
                alert.setPositiveButton("Yes",DialogInterface.OnClickListener { dialogInterface, i ->
                    //restart
                    // kendine intent yapıp activity baştan başlatır
                    val intentFromMain = intent
                    finish()//bir önceki activity kapatır
                    startActivity(intentFromMain)
                })
                alert.setNegativeButton("No",DialogInterface.OnClickListener { dialogInterface, i ->
                    Toast.makeText(this@MainActivity,"Game over",Toast.LENGTH_LONG).show()
                }).show()
            }

        }.start()
    }
     private fun hideKenny(){
        runnable = object :Runnable{
            override fun run() {
                for (image in imageArray){
                    image.visibility= View.INVISIBLE
                }
                val random = Random.nextInt(0,9)
                imageArray[random].visibility =View.VISIBLE
                handler.postDelayed(runnable,500)
            }

        }
        handler.post(runnable)


    }


     fun increaseScore(view : View){
        score+=1
        binding.scoreText.text= "Score ${score}"
    }
}