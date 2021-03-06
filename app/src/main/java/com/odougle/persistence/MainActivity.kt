package com.odougle.persistence

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import com.odougle.persistence.databinding.ActivityMainBinding
import java.io.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setListeners()
    }

    private fun setListeners() {
        binding.btnRead.setOnClickListener {
            btnReadClick()
        }
        binding.btnSave.setOnClickListener {
            btnSaveClick()
        }

        binding.btnOpenPref.setOnClickListener {
            startActivity(Intent(this, ConfigActivity::class.java))
        }

        binding.btnReadPref.setOnClickListener {
            readPrefs()
        }


    }

    private fun readPrefs() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val city = prefs.getString(
            getString(R.string.pref_city),
            getString(R.string.pref_city_default)
        )

        val socialNetworks = prefs.getString(
            getString(R.string.pref_social_network),
            getString(R.string.pref_social_network_default)
        )

        val messages = prefs.getBoolean(
            getString(R.string.pref_messages),
            false
        )

        val msg = String.format(
            "%s = %s\n%s = %s\n%s = %s",
            getString(R.string.title_city),
            city,
            getString(R.string.title_social_network), socialNetworks,
            getString(R.string.title_messages), messages.toString()
        )
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun btnReadClick() {
        val type = binding.rgType.checkedRadioButtonId
        when (type) {
            R.id.rbInternal -> loadFromInternal()
            R.id.rbExternalPriv -> loadFromExternal(true)
            R.id.rbExternalPub -> loadFromExternal(false)
        }
    }

    private fun btnSaveClick() {
        val type = binding.rgType.checkedRadioButtonId
        when (type) {
            R.id.rbInternal -> saveToInternal()
            R.id.rbExternalPriv -> saveToExternal(true)
            R.id.rbExternalPub -> saveToExternal(false)
        }
    }

    private fun saveToInternal() {
        try {
            val fos = openFileOutput("arquivo.txt", Context.MODE_PRIVATE)
            save(fos)
        } catch (e: Exception) {
            Log.e("NGVL", "Erro ao salvar o arquivo", e)
        }
    }

    private fun loadFromInternal() {
        try {
            val fis = openFileInput("arquivo.txt")
            load(fis)
        } catch (e: Exception) {
            Log.e("NGVL", "Erro ao carregar o arquivo", e)
        }
    }

    private fun saveToExternal(privateDir: Boolean) {

        val hasPermission = checkStoragePermission(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            RC_STORAGE_PERMISSION
        )
        if (!hasPermission) {
            return
        }

        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            val myDir = getExternalDir(privateDir)
            try {
                if (myDir?.exists() == false) {
                    myDir.mkdir()
                }
                val txtFile = File(myDir, "arquivo.txt")
                if (!txtFile.exists()) {
                    txtFile.createNewFile()
                }
                val fos = FileOutputStream(txtFile)
                save(fos)
            } catch (e: IOException) {
                Log.e("NGVL", "Erro ao salvar o arquivo", e)
            }
        } else {
            Log.e("NGVL", "N??o ?? poss??vel escrever no SD Card")
        }
    }

    private fun loadFromExternal(privateDir: Boolean) {

        val hasPermission = checkStoragePermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            RC_STORAGE_PERMISSION
        )
        if (!hasPermission) {
            return
        }

        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state) {
            val myDir = getExternalDir(privateDir)
            if (myDir?.exists() == true) {
                val txtFile = File(myDir, "arquivo.txt")
                if (txtFile.exists()) {
                    try {
                        txtFile.createNewFile()
                        val fis = FileInputStream(txtFile)
                        load(fis)
                    } catch (e: IOException) {
                        Log.e("NGVL", "Erro ao carregar o arquivo", e)
                    }
                }
            }
        } else {
            Log.e("NGVL", "SD card indisponivel")
        }
    }

    private fun save(fos: FileOutputStream) {
        val lines = TextUtils.split(binding.edtText.text.toString(), "\n")
        val writer = PrintWriter(fos)
        for (line in lines) {
            writer.println(line)
        }
        writer.flush()
        writer.close()
        fos.close()
    }

    private fun load(fis: FileInputStream) {
        val reader = BufferedReader(InputStreamReader(fis))
        val sb = StringBuilder()
        do {
            val line = reader.readLine() ?: break
            if (sb.isNotEmpty()) sb.append('\n')
            sb.append(line)
        } while (true)
        reader.close()
        fis.close()
        binding.txtText.text = sb.toString()
    }

    private fun getExternalDir(privateDir: Boolean) =
        if (privateDir) getExternalFilesDir(null)
        else Environment.getExternalStorageDirectory()

    private fun checkStoragePermission(
        permission: String,
        requestCode: Int
    ): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                Toast.makeText(this, R.string.message_permission_requested, Toast.LENGTH_SHORT)
                    .show()
            }
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RC_STORAGE_PERMISSION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.message_permission_granted, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this, R.string.message_permission_denied, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    companion object {
        const val RC_STORAGE_PERMISSION = 0
    }
}