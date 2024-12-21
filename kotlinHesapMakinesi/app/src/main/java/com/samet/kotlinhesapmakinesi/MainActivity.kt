package com.samet.kotlinhesapmakinesi

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.samet.kotlinhesapmakinesi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var firstNumber : Double? = null
    var secondNumber : Double? = null
    var result : Double? = null
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


    }
    fun sum(view: View ){
        firstNumber = binding.firstNumber.text.toString().toDoubleOrNull()
        secondNumber = binding.SecondNumber.text.toString().toDoubleOrNull()
        if(firstNumber !=null && secondNumber !=null){
            result= firstNumber!!+secondNumber!!
            binding.textView.setText("Result: ${result}")
        }else{
            binding.textView.setText("Enter Number")
        }
    }
    fun subtraction(view: View){
        firstNumber = binding.firstNumber.text.toString().toDoubleOrNull()
        secondNumber = binding.SecondNumber.text.toString().toDoubleOrNull()
        if(firstNumber !=null && secondNumber !=null){
            result= firstNumber!!-secondNumber!!
            binding.textView.setText("Result: ${result}")
        }else{
            binding.textView.setText("Enter Number")
        }
    }
    fun multiply(view: View){
        firstNumber = binding.firstNumber.text.toString().toDoubleOrNull()
        secondNumber = binding.SecondNumber.text.toString().toDoubleOrNull()
        if(firstNumber !=null && secondNumber !=null){
            result= firstNumber!!*secondNumber!!
            binding.textView.setText("Result: ${result}")
        }else{
            binding.textView.setText("Enter Number")
        }
    }
    fun divide(view: View){
        firstNumber = binding.firstNumber.text.toString().toDoubleOrNull()
        secondNumber = binding.SecondNumber.text.toString().toDoubleOrNull()
        if(firstNumber !=null && secondNumber !=null){
            result= firstNumber!!/secondNumber!!
            binding.textView.setText("Result: ${result}")
        }else{
            binding.textView.setText("Enter Number")
        }
    }
}