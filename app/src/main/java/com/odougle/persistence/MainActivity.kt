package com.odougle.persistence

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.odougle.persistence.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setListeners()
    }

    private fun setListeners(){
        binding.btnRead.setOnClickListener {
            btnReadClick()
        }
        binding.btnSave.setOnClickListener {
            btnSaveClick()
        }

    }

    private fun btnReadClick() {
        TODO("Not yet implemented")
    }

    private fun btnSaveClick() {
        TODO("Not yet implemented")
    }
}