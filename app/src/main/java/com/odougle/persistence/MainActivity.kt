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
        val type = binding.rgType.checkedRadioButtonId
        when(type){
            R.id.rbInternal -> loadFromInternal()
            R.id.rbExternalPriv -> loadFromExternal(true)
            R.id.rbExternalPub -> loadFromExternal(false)
        }
    }

    private fun btnSaveClick() {
        val type = binding.rgType.checkedRadioButtonId
        when(type){
            R.id.rbInternal -> saveToInternal()
            R.id.rbExternalPriv -> saveToExternal(true)
            R.id.rbExternalPub -> saveToExternal(false)
        }
    }

    private fun saveToExternal(privateDir: Boolean) {
        TODO("Not yet implemented")
    }

    private fun saveToInternal() {
        TODO("Not yet implemented")
    }

    private fun loadFromExternal(privateDir: Boolean) {
        TODO("Not yet implemented")
    }

    private fun loadFromInternal() {
        TODO("Not yet implemented")
    }
}