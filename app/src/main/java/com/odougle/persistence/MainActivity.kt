package com.odougle.persistence

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.odougle.persistence.databinding.ActivityMainBinding
import java.io.*
import java.lang.StringBuilder

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

    private fun saveToInternal() {
        try {
            val fos = openFileOutput("arquivo.txt",Context.MODE_PRIVATE)
            save(fos)
        }catch (e: Exception){
            Log.e("NGVL", "Erro ao salvar o arquivo", e)
        }
    }

    private fun loadFromInternal() {
        try {
            val fis = openFileInput("arquivo.txt")
            load(fis)
        }catch (e: Exception){
            Log.e("NGVL", "Erro ao carregar o arquivo", e)
        }
    }

    private fun saveToExternal(privateDir: Boolean) {
        TODO("Not yet implemented")
    }

    private fun loadFromExternal(privateDir: Boolean) {
        TODO("Not yet implemented")
    }

    private fun save(fos: FileOutputStream){
        val lines = TextUtils.split(binding.edtText.text.toString(), "\n")
        val writer = PrintWriter(fos)
        for(line in lines){
            writer.println(line)
        }
        writer.flush()
        writer.close()
        fos.close()
    }

    private fun load(fis: FileInputStream){
        val reader = BufferedReader(InputStreamReader(fis))
        val sb = StringBuilder()
        do{
            val line = reader.readLine() ?: break
            if(sb.isNotEmpty()) sb.append('\n')
            sb.append(line)
        }while (true)
        reader.close()
        fis.close()
        binding.txtText.text = sb.toString()
    }
}